import request from "../utils/request";

export function getAllDirectors() {
    return request.get('/directors/list')
}

export function addDirector(data) {
    return request.post('/directors/add', data)
}

export function updateDirector(data) {
    return request.put('/directors/update', data)
}

export function deleteDirector(directorId) {
    return request.delete('/directors/delete', { directorId })
}