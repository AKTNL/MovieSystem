import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(config => {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    const token = localStorage.getItem('token')
    if (token) {
        config.headers['token'] = token // 把 token 放入请求头
    }
    if (user.userId) {
        config.headers['userId'] = user.userId
    }
    if (user.role) {
        config.headers['userRole'] = user.role
    }
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(response => {
    let res = response.data;
    //如果返回的是文件流，直接返回
    if (response.config.responseType === 'blob') {
        return res
    }
    // 兼容服务端返回的字符串数据
    if (typeof res === 'string') {
        res = res ? JSON.parse(res) : res
    }
    if (res.code === 401) {
        // 清除缓存
        localStorage.removeItem('user')
        localStorage.removeItem('token')
        // 跳转登录页
        location.href = '/login'
    }
    return res;
}, error => {
    console.log('err' + error)
    return Promise.reject(error)
})

export default request