import { pa } from "element-plus/es/locales.mjs";
import request from "../utils/request";

//登录
export function login(data) {
    return request.post('/user/login', data)
}

//注册
export function register(data) {
    return request.post('/user/register', data)
}

export function userUpdate(from) {
    return request.put('/user/update', from)
}

export function getUserInfo(userId) {
    return request.get('/user/' + userId)
}

export function updatePassword(params) {
    return request.post('/user/password', params)
}

export function getAllUsers() {
    return request.get('/user/list')
}

export function deleteUser(userId) {
    return request.delete('/user/' + userId)
}