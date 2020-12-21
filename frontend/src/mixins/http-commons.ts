import axios from "axios";

export const httpClient = axios.create({
  timeout: 8000,
  baseURL: "https://covid-sentry.ddns.net"
  // headers: {
  //    'Access-Control-Allow-Origin': 'http://localhost:8081'
  // }
});
