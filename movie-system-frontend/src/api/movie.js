import request from "../utils/request";

//获取电影列表
export function getMovieList() {
    return request.get('/movie/list')
}

//获取电影详情
export function getMovieDetail(id) {
    return request.get('/movie/' + id)
}