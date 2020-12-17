import axios from 'axios'

export const httpClient = axios.create({
    timeout: 8000,
    // baseURL: 'http://localhost:8080',
    // headers: {
    //    'Access-Control-Allow-Origin': 'http://localhost:8081'
    // }
})