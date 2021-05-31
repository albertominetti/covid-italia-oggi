
# Install the app

## Requirements

The spring-boot application needs to run in a `jvm` this is the only mandatory requirement. The graph images are generated only if `R` is available in the server. Optionally if you run in on a Virtual Private Server machine you may need to hide the application using a reverse proxy solution like `Apache` or `Nginx` and for a little more security you can expose it using https. From the technical point of view, `spring-boot` itself can run in https mode with small adjustments in the `yml` (or `properties`) configuration. In this way you can have a single microservice that holds all the code, but I prefer the separation of concerns as described below, especially when you have to deal with multiple services or with a frontend.

## Installation of dependencies

The following part will cover the installation of `R`, `Nginx` and `letsencrypt` using a standard `Ubuntu` server. You can install on other distro using a different package installer, like `yum` for CentOS.

First, update your system. This step is not a requirement, but it is a good practice.

```
sudo apt-get -y update
```

Install the latest jvm:

```
sudo apt install default-jre
```

Also curl can be useful for some automation:

```
sudo apt install curl
```

### Install `R`

You can install R, and check the version. While writing this document the latest version is the `3.6.2`:

```
sudo apt-get -y install r-base r-cran-ggplot2
which R
R --version
```

### Install `letsencrypt` (optional)

Then install `letsencrypt`, last version is `certbot 0.27.0`; skip it if you do not need an authority-issued certificate, or if you already have your own.

```
sudo apt-get -y install letsencrypt
letsencrypt --version
```

### Install `nginx` (optional)

The last package you need is nginx, last version is `nginx/1.14.0`; skip if you prefer to use the https feature provided by spring-boot.

```
sudo apt-get -y install nginx
sudo service nginx stop
sudo service nginx start
```

### Configure the dependencies (optional)

We can now configure the nginx that works as a reverse proxy and enable the SSL connectivity without any change to the application itself.

First, we need a valid certificate; for development purpose you can generate it a self-signed one with openssl in the following way:

```
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -subj '/CN=localhost'
```

For production purpose you probably need a certificate issued by an authority, here the simple command you have to run on the production server. It is mandatory to run on the production server after the dns configuration because the authority validates it doing a request to the port 80, and it expects that certbot is listening on that port.

```
sudo service nginx stop
sudo letsencrypt certonly --standalone -d example.org -d www.example.org
sudo service nginx start
```

We can now move to the configuration of nginx in the file `/etc/nginx/sites-enabled/example.org`. The following is a sample configuration using the previously generated certificate and key, and it redirects any traffic to the unsecured port 80 to the 443 port using https.

```
server {
    listen 443 ssl;
    server_name example.org www.example.org;
    ssl_certificate /etc/letsencrypt/live/example.org/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/example.org/privkey.pem;
    ssl_stapling on;
    ssl_stapling_verify on;
    add_header Strict-Transport-Security "max-age=31536000";
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    
    location /swagger-ui/ {
        auth_basic "Authentication required";
        auth_basic_user_file /etc/nginx/.htpasswd;
        proxy_pass http://localhost:8080/swagger-ui/;
        proxy_set_header Host $host;
    }
	
    location /admin/ {
        auth_basic "Authentication required";
        auth_basic_user_file /etc/nginx/.htpasswd;
        proxy_pass http://localhost:8080/admin/;
        proxy_set_header Host $host;
    }
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
    }
}
server {
    listen 80;
    server_name example.org;
    rewrite ^ https://$host$request_uri? permanent;
}
```

As you probably noticed the previous configuration deny the access to some urls that are for administrative purpose, like `/swagger-ui` and `/admin` endpoints. To configure the basic access with a username and password you can run the following commands.

```
sudo sh -c "echo -n 'admin:' >> /etc/nginx/.htpasswd"
sudo sh -c "openssl passwd -apr1 >> /etc/nginx/.htpasswd"
```

Once the configuration has been completed you can test it with the `-t` option and reload it.

```
sudo nginx -t && sudo service nginx reload
```

## App as a service

The simplest way to run a spring-boot application, or a generic java application in a jar package is with the command `java -jar app.jar`, here the idea is to run as a service taking advantage of systemd. From this point the documentation I will use the `app` as the name of the application.

Add a new file that contains the configuration for the service in `/etc/systemd/system/app.service`.

```
[Unit]
Description=Spring Boot application
After=syslog.target
StartLimitIntervalSec=0

[Service]
Restart=always
RestartSec=1
Environment='JAVA_OPTS=-Djava.security.egd=file:/dev/./urandom -Xmx256m'
Environment='SPRING_OPTS=--server.address=127.0.0.1 --server.port=8080'
User=root
WorkingDirectory=/var/app/
ExecStart=/usr/bin/java $JAVA_OPTS -jar /var/app/app.jar $SPRING_OPTS
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=app
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Then you can install the service and enable the application.

```
systemctl enable app
service app start
```

## Logging in linux environment

There are several tools that are quite useful for logging purposes and to maintain the Spring application as simple as possible. One of these is rsyslogd that allows to log the messages caught by syslog in a file. To achieve this behavior you need a new file like `/etc/rsyslog.d/60-app.conf` with the following content.

```
$template onlyMessageTemplate,"%msg%\n"
$ActionFileDefaultTemplate onlyMessageTemplate
if $programname == 'app' then /var/log/app/spring.log
& stop
```

And setup the directory for logging.

```
mkdir /var/log/app
chown syslog /var/log/app
chgrp adm /var/log/app
chmod 775 /var/log/app
```

Do not forget to reload the service `service rsyslog restart`.

### Logging rotation

For log rotation we can use `logrotate` with the following settings in a new file `/etc/logrotate.d/app`.

```
/var/log/app/*.log
{
    rotate 7
    daily
    su syslog adm
    create 0640 syslog adm 
    missingok
    notifempty
    compress
    delaycompress
    postrotate
        /usr/lib/rsyslog/rsyslog-rotate || reload rsyslog || true
    endscript
}
```

You can check the rotation of the logs using `logrotate -d /etc/logrotate.d/app` or you can force with `logrotate -f /etc/logrotate.d/app`.

Note: if you are interested to a centralized logging solution with Spring Boot you take a look [here](https://github.com/albertominetti/java-discovery)

### Logging on a remote system (papertrail)

You can log to a remote system, like [Papertrail](https://papertrailapp.com/) that provides a nice user interface for search among logs collected from several sources. In this way, syslog will send the logs to a papertrail log destination, Papertrail simply store the logs in its system and allows you to search on them. Once the registration is complete, you need to create a new log destination. You get an url like this `logs9.papertrailapp.com:12345`, and you have to include at the end of `/etc/rsyslog.conf`. Follow the [link](https://documentation.solarwinds.com/en/Success_Center/papertrail/Content/kb/configuration/configuring-remote-syslog-from-unixlinux-and-bsdos-x.htm) for all the details. Do not forget to remove the trailing `& stop` line from `/etc/rsyslog.d/60-app.conf`.

#### TODO tcpwrapper
#### TODO automated deploy when new artifact on github or new push in a branch

Credits:
* https://fowlie.github.io/2017/08/24/deploy-spring-boot-on-digitialocean/
* https://itectec.com/ubuntu/ubuntu-how-to-configure-logrotate-without-having-etc-logrotate-d-rsyslog/
* https://www.tecmint.com/install-logrotate-to-manage-log-rotation-in-linux/
* https://tjgreco.com/posts/spring-boot-startup/




