import axios from "axios";
import {showFailToast} from "vant";

export const URL = 'localhost:8080/api';

let myAxios = axios.create({
    baseURL: 'http://' + URL
});
myAxios.defaults.withCredentials = true

let token = sessionStorage.getItem("token");
// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    if (token) {
        config.headers['authorization'] = token
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});

export default myAxios