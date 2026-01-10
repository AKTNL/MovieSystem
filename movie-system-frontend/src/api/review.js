import request from "../utils/request";

//获取某电影的评论列表
export function getReviewList(movieId) {
    return request.get('/review/list/' + movieId)
}

//提交评论
export function addReview(data) {
    return request.post('/review/add', data)
}

//删除评论
export function deleteReview(reviewId) {
    return request.delete('/review/delete/' + reviewId)
}

//管理员获取所有评论
export function getAllReviews() {
    return request.get('/review/listAll')
}