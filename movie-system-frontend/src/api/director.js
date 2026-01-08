import request from "../utils/request";

export function getAllDirectors() {
    return request.get('/directors/list')
}