import request from "../utils/request";

//获取某电影的评论列表
export function getReviewList(movieId) {
    return request.get('/review/list/' + movieId)
}

//提交评论
export function addReview(data) {
    return request.post('/review/add', data)
}