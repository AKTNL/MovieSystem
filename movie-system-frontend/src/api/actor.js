import request from "../utils/request";

export function getAllActors() {
    return request.get('/actors/list')
}

export function addActor(actor) {
    return request.post('/actors/add', actor)
}

export function updateActor(actor) {
    return request.put('/actors/update', actor)
}

export function deleteActor(id) {
    return request.delete('/actors/delete/' + id)
}

export function getActorDetail(id) {
    return request.get('/actors/detail/' + id)
}