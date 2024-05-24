import axios from "axios";
import {showFailToast} from "vant";

export const URL = 'localhost:8080/api';

let myAxios = axios.create({
    baseURL: 'http://' + URL
});
myAxios.defaults.withCredentials = true

let token = sessionStorage.getItem("token");

export default myAxios