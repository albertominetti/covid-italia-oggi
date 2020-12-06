import axios from 'axios'

export const AXIOS = axios.create({
    timeout: 1500,
    // baseURL: 'http://localhost:8080',
    // headers: {
    //    'Access-Control-Allow-Origin': 'http://localhost:8081'
    // }
})