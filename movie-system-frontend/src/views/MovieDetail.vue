<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getMovieDetail } from '../api/movie';
import { getReviewList, addReview } from '../api/review';

const route = useRoute()

const movieId = route.params.id // 从网址获取ID
console.log("当前电影ID:", movieId) 

const user = JSON.parse(localStorage.getItem('user') || '{}')//获取当前登录用户

const movie = ref({})
const reviews = ref({})

//评分相关
const myScore = ref(0)
const myContent = ref('')

// 计算属性：将数据库的 10分制 转为前端显示的 5星制
const displayRating = computed(() => {
    return movie.value.rating ? movie.value.rating / 2 : 0
})

onMounted(() => {
    loadMovieInfo()
    loadReviews()
})

//1.加载电影详情
const loadMovieInfo = () => {
    getMovieDetail(movieId).then(res => {
        if (res.code === 200) {
            movie.value = res.data
        } else {
            ElMessage.error("获取电影详情失败：" + res.msg)
        }
    })
}

//2.加载评论列表(调用存储过程接口)
const loadReviews = () => {
    getReviewList(movieId).then(res => {
        if (res.code === 200) {
            reviews.value = res.data
        }
    })
}

//3.提交评论
const submitReview = () => {
    if (myScore.value === 0) {
        ElMessage.warning('请先打分')
        return
    }
    if (!myContent.value.trim()) {
        ElMessage.warning('请输入评论内容')
        return
    } 
    const data = {
        userId: user.userId,
        movieId: movieId,
        score: myScore.value * 2,
        content: myContent.value
    }

    addReview(data).then(res => {
        if (res.code === 200) {
            ElMessage.success('评论成功！')
            myContent.value = ''
            myScore.value = 0
            //刷新列表和电影分数
            loadReviews()
            loadMovieInfo()
        } else {
            ElMessage.error(res.msg || '提交失败，可能你已经评过分了')
        }
    })
}
</script>

<template>
    <div style="max-width: 1200px; margin: 0 auto; padding: 20px;">
        <!--1.顶部电影信息区域-->
        <el-card shadow="never">
            <el-row :gutter="40">
                <!--左侧海报-->
                <el-col :span="6">
                    <el-image 
                        :src="movie.coverUrl || 'https://via.placeholder.com/300x400'"
                        style="width: 100%; border-radius: 8px;"
                        fit="cover"
                    />
                </el-col>
                <!--右侧详细信息-->
                <el-col :span="18">
                    <h1 style="margin-top: 0;">{{ movie.title }} 
                        <small style="font-size: 16px; color: #888">
                            ({{ movie.releaseYear }})
                        </small>
                    </h1>
                    <div style="margin-bottom: 20px;">
                        <el-tag effect="dark">{{ movie.genre }}</el-tag>
                        <el-tag type="info" style="margin-left: 10px;">{{ movie.country }}</el-tag>
                        <el-tag type="warning" style="margin-left: 10px;">{{ movie.duration }} 分钟</el-tag>
                    </div>
                    <div style="display: flex; align-items: center; margin-bottom: 20px;">
                        <span style="font-size: 14px; color: #666; margin-right: 10px;">综合评分：</span>
                        <el-rate
                            :model-value="displayRating"
                            disabled
                            show-score
                            text-color="#ff9900"
                            score-template="{value} 分"
                        />
                        <span style="font-size: 12px; color: #999; margin-left: 10px;">({{ movie.ratingCount }} 人参与)</span>
                    </div>

                    <div style="color: #555; line-height: 1.8; font-size: 15px;">
                        <strong>简介：</strong>{{ movie.synopsis || '暂无简介' }}
                    </div>
                </el-col>
            </el-row>
        </el-card>

        <!--2.打分与评论输入框-->
        <el-card style="margin-top: 20px;" shadow="never">
            <template #header>
                <span style="font-weight: bold;">我要评价</span>
            </template>

            <div v-if="user.userId">
                <div style="margin-bottom: 15px; display: flex; align-items: center;">
                    <span style="margin-right: 10px;">给个分吧：</span>
                    <el-rate v-model="myScore" :max="5" allow-half show-text :texts="['极差', '失望', '一般', '满意', '神作']"/>
                </div>

                <el-input
                    v-model="myContent"
                    type="textarea"
                    :rows="3"
                    placeholder="写下你的观影感受..."
                    maxlength="500"
                    show-word-limit
                />
                <div style="text-align: right; margin-top: 10px;">
                    <el-button type="primary" @click="submitReview">发表评论</el-button>
                </div>
            </div>

            <div v-else style="text-align: center; padding: 20px; color: #999;">
                请<el-link type="primary" @click="$router.push('/login')">登录</el-link>后参与评论
            </div>
        </el-card>

        <!--3.评论列表-->
        <div style="margin-top: 30px;">
            <h3 style="border-left: 4px solid #409eff; padding-left: 10px;">热门评论</h3>
            <el-empty v-if="reviews.length === 0" description="暂无评论，快来抢沙发！"/>
            <div v-for="item in reviews" :key="item.reviewId" style="border-bottom: 1px solid #f0f0f0; padding: 20px 0;">
                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                    <el-avatar :size="30" :src="item.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"/>
                    <span style="margin-left: 10px; font-weight: bold; font-size: 14px;">{{ item.username }}</span>
                    <span style="margin-left: auto; color: #999; font-size: 12px;">{{ item.createTime.replace('T', ' ') }}</span>
                </div>

                <div style="margin-bottom: 8px;">
                    <el-rate :model-value="item.score / 2" disabled size="small"/>
                    <span style="color: #ff9900; font-weight: bold; margin-left: 5px;">{{ item.score }} 分</span>
                </div>

                <div style="color: #333; line-height: 1.6;">
                    {{ item.content }}
                </div>
            </div>
        </div>
    </div>
</template>