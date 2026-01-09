import request from "../utils/request";

export function getCaptchaApi() {
    return request.get('/captcha/image')
}