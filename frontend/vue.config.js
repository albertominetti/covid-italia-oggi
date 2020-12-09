module.exports = {
    // Change build paths to make them Maven compatible
    // see https://cli.vuejs.org/config/
    outputDir: 'dist',

    assetsDir: 'assets',

    devServer: {
        proxy: {
            '/api': {
                //target: 'http://localhost:8080',
                target: 'https://covid-sentry.ddns.net',
                ws: true,
                changeOrigin: true
            }
        },
        port: 8081,
    },

    chainWebpack: config => {
        config
            .plugin('html')
            .tap(args => {
                args[0].title = 'Covid Sentry'
                return args
            })
    },

    pluginOptions: {
      i18n: {
        locale: 'it',
        fallbackLocale: 'en',
        localeDir: 'locales',
        enableInSFC: false
      }
    }
}
