<script setup>
import { ref, onMounted, computed, reactive } from 'vue'
import { useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getMovieDetail, getMovieActors, getMovieDirectors } from '../api/movie';
import { getReviewList, addReview, deleteReview, updateReview } from '../api/review';

const route = useRoute()

const movieId = route.params.id // 从网址获取ID

const user = JSON.parse(localStorage.getItem('user') || '{}')//获取当前登录用户

const movie = ref({})
const reviews = ref([])

const actors = ref([])
const directors = ref([])

//评分相关
const myScore = ref(0)
const myContent = ref('')

// 计算属性：将数据库的 10分制 转为前端显示的 5星制
const displayRating = computed(() => {
    return movie.value.rating ? movie.value.rating / 2 : 0
})

// 编辑相关变量
const editDialogVisible = ref(false)
const editForm = reactive({
  reviewId: null,
  userId: null,
  score: 0,
  content: ''
})

// 排序类型
const sortType = ref('newest') // 默认最新

// 排序逻辑 (使用 computed 自动计算)
const sortedReviews = computed(() => {
    // 复制一份数组，防止直接修改原数组导致副作用
    const list = [...reviews.value]
    
    if (sortType.value === 'newest') {
        // 按时间倒序 (字符串比较即可，或者转Date)
        return list.sort((a, b) => b.createTime.localeCompare(a.createTime))
    } else if (sortType.value === 'oldest') {
        // 按时间正序
        return list.sort((a, b) => a.createTime.localeCompare(b.createTime))
    } else if (sortType.value === 'highest') {
        // 按分数从高到低
        return list.sort((a, b) => b.score - a.score)
    } else if (sortType.value === 'lowest') {
        // 按分数从低到高
        return list.sort((a, b) => a.score - b.score)
    }
    return list
})

onMounted(() => {
    loadMovieInfo()
    loadReviews()
    loadCast()
})

//加载电影详情
const loadMovieInfo = () => {
    getMovieDetail(movieId).then(res => {
        if (res.code === 200) {
            movie.value = res.data
        } else {
            ElMessage.error("获取电影详情失败：" + res.msg)
        }
    })
}

//加载演职人员
const loadCast = () => {
  getMovieActors(movieId).then(res => {
    if (res.code === 200) actors.value = res.data
  }).catch(err => {
        console.error("演员信息加载失败:", err)
  })
  getMovieDirectors(movieId).then(res => {
    if (res.code === 200) directors.value = res.data
  }).catch(err => {
        console.error("导演信息加载失败:", err)
  })
}

//加载评论列表(调用存储过程接口)
const loadReviews = () => {
    getReviewList(movieId).then(res => {
        if (res.code === 200) {
            reviews.value = res.data
        }
    })
}

//提交评论
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
            ElMessage.error('提交失败，可能你已经评过分了')
        }
    })
}

// 点击删除
const handleDeleteReview = (reviewId) => {
  ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
    type: 'warning'
  }).then(() => {
    deleteReview(reviewId).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除成功')
        // 刷新评论列表和电影分数
        loadReviews()
        loadMovieInfo()
      }
    })
  })
}

// 点击编辑 (回显数据)
const handleEditReview = (item) => {
  editForm.reviewId = item.reviewId
  // 注意：item.score 是 10分制，前端 rate 组件是 5分制，所以要除以 2
  editForm.score = item.score / 2 
  editForm.content = item.content
  // 这里的 user 是当前登录用户
  editForm.userId = user.userId 
  
  editDialogVisible.value = true
}

// 提交修改
const submitEditReview = () => {
  if (editForm.score === 0) return ElMessage.warning('评分不能为空')
  
  const data = {
    reviewId: editForm.reviewId,
    userId: editForm.userId,
    score: editForm.score * 2, // 转回 10分制
    content: editForm.content
  }

  updateReview(data).then(res => {
    if (res.code === 200) {
      ElMessage.success('修改成功')
      editDialogVisible.value = false
      loadReviews()
      loadMovieInfo() // 刷新电影总分
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<template>
  <div class="detail-page-container">
    <div class="movie-banner">
      <div class="banner-bg" :style="{ backgroundImage: `url(${movie.coverUrl})` }"></div>
      <div class="banner-content">
        <el-row :gutter="40" class="main-info">
          <el-col :md="6" :sm="8" class="poster-col">
            <el-image :src="movie.coverUrl" class="main-poster" fit="cover">
              <template #placeholder><div class="image-slot">加载中...</div></template>
            </el-image>
          </el-col>
          <el-col :md="18" :sm="16" class="info-col">
            <h1 class="movie-title">
              {{ movie.title }} 
              <span class="movie-year">({{ movie.releaseYear }})</span>
            </h1>
            
            <div class="tags-row">
              <el-tag effect="plain" class="custom-tag">{{ movie.genre }}</el-tag>
              <el-tag effect="plain" class="custom-tag">{{ movie.country }}</el-tag>
              <el-tag effect="plain" class="custom-tag">{{ movie.duration }} min</el-tag>
            </div>

            <div class="rating-box">
              <div class="score-num">{{ movie.rating?.toFixed(1) || '0.0' }}</div>
              <div class="score-meta">
                <el-rate :model-value="displayRating" disabled text-color="#ff9900" />
                <div class="count-text">{{ movie.ratingCount }} 人评过</div>
              </div>
            </div>

            <p class="synopsis">{{ movie.synopsis || '该影片暂无简介。' }}</p>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="detail-body">
      <el-row :gutter="30">
        <el-col :span="18">
          <section class="section-card">
            <h3 class="section-title">演职人员</h3>
            <div class="cast-list">
              <div v-for="d in directors" :key="d.directorId" class="cast-item" @click="$router.push('/director/' + d.directorId)">
                <el-avatar :size="90" :src="d.avatarUrl" class="hover-avatar" />
                <div class="cast-name">{{ d.name }}</div>
                <div class="cast-role">导演</div>
              </div>
              <div v-for="a in actors" :key="a.actorId" class="cast-item" @click="$router.push('/actor/' + a.actorId)">
                <el-avatar :size="90" :src="a.avatarUrl" class="hover-avatar" />
                <div class="cast-name">{{ a.name }}</div>
                <div class="cast-role">饰 {{ a.roleName }}</div>
              </div>
            </div>
          </section>

          <section class="section-card reviews-section">
            <div class="reviews-header">
              <h3 class="section-title">热门影评</h3>
                <el-select 
                    v-model="sortType" 
                    size="small" 
                    class="ghost-select" 
                    style="width: 110px"
                >
                    <template #prefix>
                        <el-icon><Sort /></el-icon>
                    </template>
                    <el-option label="最新发布" value="newest" />
                    <el-option label="最高评分" value="highest" />
                </el-select>
            </div>

            <el-empty v-if="reviews.length === 0" description="期待你的精彩点评" />
            
            <div v-for="item in sortedReviews" :key="item.reviewId" class="review-item">
              <div class="review-user">
                <el-avatar :size="40" :src="item.avatar" />
                <div class="user-meta">
                  <div class="name">{{ item.username }}</div>
                  <div class="time">{{ item.createTime?.replace('T', ' ') }}</div>
                </div>
                <div class="item-rating">
                  <el-rate :model-value="item.score / 2" disabled size="small" />
                  <span>{{ item.score }}分</span>
                </div>
              </div>
              <div class="review-content">{{ item.content }}</div>
              <div v-if="user.userId && item.username === user.username" class="review-actions">
                <el-button link type="primary" icon="Edit" @click="handleEditReview(item)">修改</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteReview(item.reviewId)">删除</el-button>
              </div>
            </div>
          </section>
        </el-col>

        <el-col :span="6">
          <div class="sticky-side">
            <div class="my-review-card">
              <h4>我的评价</h4>
              <div v-if="user.userId">
                <el-rate v-model="myScore" allow-half class="large-rate" />
                <el-input
                  v-model="myContent"
                  type="textarea"
                  :rows="4"
                  placeholder="感受如何？写点什么吧..."
                  class="dark-input"
                />
                <el-button type="primary" class="submit-btn" @click="submitReview">发布评价</el-button>
              </div>
              <div v-else class="login-tip">
                请 <el-link type="primary" @click="$router.push('/login')">登录</el-link> 后评价
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <el-dialog 
      v-model="editDialogVisible" 
      title="修改评价" 
      width="500px"
      class="dark-dialog" 
    >
      <div style="margin-bottom: 20px;">
        <span style="color: #94a3b8; margin-right: 10px;">修改评分：</span>
        <el-rate 
          v-model="editForm.score" 
          :max="5" 
          allow-half 
          show-text 
          :texts="['极差', '失望', '一般', '满意', '神作']" 
        />
      </div>
      <el-input
        v-model="editForm.content"
        type="textarea"
        :rows="4"
        maxlength="500"
        show-word-limit
        placeholder="修改你的评论..."
      />
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditReview">保存修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 1. 沉浸式 Banner */
.movie-banner {
  position: relative;
  min-height: 450px;
  color: white;
  overflow: hidden;
  display: flex;
  align-items: center;
}

.banner-bg {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  filter: blur(40px) brightness(0.4);
  transform: scale(1.1);
  z-index: 0;
}

.banner-content {
  position: relative;
  z-index: 1;
  max-width: 1440px;
  margin: 0 auto;
  padding: 40px;
  width: 100%;
}

.main-poster {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
  border: 1px solid rgba(255,255,255,0.1);
}

.movie-title {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 15px;
}

.movie-year {
  font-size: 24px;
  color: rgba(255,255,255,0.6);
  font-weight: 400;
}

.tags-row {
  margin-bottom: 25px;
}

.custom-tag {
  background: rgba(255,255,255,0.1) !important;
  border-color: rgba(255,255,255,0.2) !important;
  color: #fff !important;
  margin-right: 10px;
}

.rating-box {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.score-num {
  font-size: 48px;
  font-weight: bold;
  color: #fbbf24;
}

.count-text {
  font-size: 13px;
  color: rgba(255,255,255,0.5);
  margin-top: 4px;
}

.synopsis {
  font-size: 16px;
  line-height: 1.8;
  color: rgba(255,255,255,0.9);
  max-width: 800px;
}

/* 2. 主体布局 */
.detail-body {
  max-width: 1440px;
  margin: 0 auto;
  padding: 40px;
}

.section-card {
  background: #1e293b;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 30px;
  border: 1px solid rgba(255,255,255,0.05);
}

.section-title {
  color: #f1f5f9;
  font-size: 20px;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 4px solid #3b82f6;
}

/* 演职员表横向滚动 */
.cast-list {
  display: flex;
  overflow-x: auto;
  gap: 25px;
  padding-bottom: 10px;
}

.cast-list::-webkit-scrollbar { height: 6px; }
.cast-list::-webkit-scrollbar-thumb { background: #334155; border-radius: 10px; }

.cast-item {
  text-align: center;
  min-width: 100px;
  cursor: pointer;
}

.hover-avatar {
  transition: transform 0.3s;
  border: 2px solid transparent;
}

.cast-item:hover .hover-avatar {
  transform: scale(1.05);
  border-color: #3b82f6;
}

.cast-name {
  color: #f1f5f9;
  font-weight: bold;
  margin-top: 10px;
  font-size: 14px;
}

.cast-role {
  color: #94a3b8;
  font-size: 12px;
}

/* 3. 评论区美化 */
.reviews-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.review-item {
  padding: 20px 0;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}

.review-user {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.user-meta .name { color: #f1f5f9; font-weight: bold; font-size: 15px; }
.user-meta .time { color: #64748b; font-size: 12px; }

.item-rating {
  margin-left: auto;
  text-align: right;
}

.item-rating span { color: #fbbf24; font-weight: bold; font-size: 14px; display: block; }

.review-content {
  color: #cbd5e1;
  line-height: 1.7;
  font-size: 15px;
  padding-left: 52px;
}

.review-actions { text-align: right; margin-top: 10px; }

/* 4. 右侧评分栏 */
.sticky-side {
  position: sticky;
  top: 100px;
}

.my-review-card {
  background: #1e293b;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #3b82f633;
}

.my-review-card h4 { color: #f1f5f9; margin-top: 0; }

.large-rate { margin: 15px 0; }

.dark-input :deep(.el-textarea__inner) {
  background: #0f172a;
  border: 1px solid #334155;
  color: #f1f5f9;
}

.submit-btn { width: 100%; margin-top: 15px; font-weight: bold; }

.login-tip { color: #94a3b8; text-align: center; padding: 20px 0; }

/* 1. 基础输入框美化：使其更扁平、深色 */
:deep(.el-select .el-input__wrapper) {
    background-color: rgba(15, 23, 42, 0.8) !important; /* 深蓝黑色，带一点透明 */
    box-shadow: none !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    border-radius: 8px !important;
    transition: all 0.3s ease;
}

/* 2. Hover 和 Focus 时的状态：增加一点点科技感 */
:deep(.el-select .el-input__wrapper:hover),
:deep(.el-select.is-focus .el-input__wrapper) {
    border-color: #3b82f6 !important; /* 蓝色高亮 */
    background-color: rgba(15, 23, 42, 1) !important;
}

/* 3. 这里的文字颜色 */
:deep(.el-select .el-input__inner) {
    color: #cbd5e1 !important;
    font-size: 13px;
}

/* 4. 关键：美化弹出后的下拉面板 (Popper) */
/* 注意：由于 Popper 默认挂载在 body 下，如果你用了 scoped，可能需要全局样式或特定的 popper-class */
:deep(.el-select__popper.el-popper) {
    background: #1e293b !important;
    border: 1px solid #334155 !important;
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.5) !important;
}

/* 5. 下拉选项 (Option) 的美化 */
:deep(.el-select-dropdown__item) {
    color: #94a3b8;
}

:deep(.el-select-dropdown__item.hover),
:deep(.el-select-dropdown__item:hover) {
    background-color: rgba(59, 130, 246, 0.15) !important;
    color: #3b82f6;
}

:deep(.el-select-dropdown__item.selected) {
    color: #3b82f6 !important;
    font-weight: bold;
}

.ghost-select :deep(.el-input__wrapper) {
    background-color: transparent !important; /* 背景透明 */
    border: none !important; /* 去掉边框 */
    padding-right: 0;
}
.ghost-select :deep(.el-input__inner) {
    color: #64748b !important; /* 使用较淡的灰色 */
    font-weight: 500;
}
</style>