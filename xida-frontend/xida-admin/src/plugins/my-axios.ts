import axios from "axios";
import { ElMessage } from 'element-plus'
export const URL = '116.198.233.53:8080/api';

let myAxios = axios.create({
    baseURL: 'http://' + URL
});
myAxios.defaults.withCredentials = true

let token = sessionStorage.getItem("token");
myAxios.interceptors.request.use(function (config) {
    if (token) {
        config.headers['authorization'] = token
    }
    return config;
}, function (error) {
    return Promise.reject(error);
});

myAxios.interceptors.response.use(function (response) {
    if (response?.data?.code === 40100) {
        ElMessage.error('登录状态失效,请重新登录')
        window.location.href = '/login'
    }
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response;
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
});
export default myAxios
