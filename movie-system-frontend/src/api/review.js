import request from "../utils/request";

//获取某电影的评论列表
export function getReviewList(movieId) {
    return request.get('/review/list/' + movieId)
}

//提交评论
export function addReview(data) {
    return request.post('/review/add', data)
}

//用户删除自己的评论
export function deleteReview(reviewId) {
    return request.delete('/review/delete/' + reviewId)
}

//管理员获取所有评论
export function getAllReviews() {
    return request.get('/review/listAll')
}

export function updateReview(data) {
    return request.post('/review/update', data)
}

//管理员强制删除评论
export function deleteReviewByAdmin(reviewId) {
    return request.delete('/review/admin/delete/' + reviewId)
}

export function toggleLike(reviewId) {
    return request.post('/review/like/' + reviewId)
}