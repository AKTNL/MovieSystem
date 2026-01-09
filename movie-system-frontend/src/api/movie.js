import request from "../utils/request";

//获取电影列表
export function getMovieList(params) {
    return request.get('/movie/list', { params: params })
}

//获取电影详情
export function getMovieDetail(id) {
    return request.get('/movie/' + id)
}

// 获取演员列表
export function getMovieActors(movieId) {
    return request.get('/actors/' + movieId)
}

// 获取导演列表
export function getMovieDirectors(movieId) {
    return request.get('/directors/' + movieId)
}

//删除电影
export function deleteMovie(movieId) {
    return request.get('/delete/' + movieId)
}

export function getHotMovies() {
    return request.get('/movie/hot')
}