<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getMovieList, getHotMovies, getMovieGenres } from '../api/movie';
import { Trophy, Search, Filter, Sort } from '@element-plus/icons-vue';

const movieList = ref([])
const hotMovies = ref([])
// 定义一个变量存类型列表
const genreOptions = ref([]) 

const searchForm = reactive({
    title: '',
    genre: '',
    sort: 'newest' // 默认按最新排序
})

onMounted(() => {
    loadMovies()
    loadHot()
    loadGenres()
})

const loadMovies = () => {
    getMovieList(searchForm).then(res => {
        if (res.code === 200) {
            movieList.value = res.data
        }
    })
}

const loadHot = () => {
    getHotMovies().then(res => {
        if (res.code === 200) {
            hotMovies.value = res.data
        }
    })
}

const loadGenres = () => {
    getMovieGenres().then(res => {
        if (res.code === 200) {
            genreOptions.value = res.data
        }
    })
}
</script>

<template>
    <div class="home-container">
        <el-row :gutter="30">
            <el-col :span="18">
                <div class="filter-section">
                    <el-form :inline="true" :model="searchForm" class="custom-form">
                        <!-- 1. 类型筛选 -->
                        <el-form-item label="类型">
                            <el-select 
                                v-model="searchForm.genre" 
                                placeholder="全部类型" 
                                clearable 
                                filterable 
                                @change="loadMovies" 
                                style="width: 120px;"
                            >
                                <el-option 
                                    v-for="item in genreOptions" 
                                    :key="item" 
                                    :label="item" 
                                    :value="item" 
                                />
                            </el-select>
                        </el-form-item>

                        <!-- 2. 【新增】排序方式 -->
                        <el-form-item label="排序">
                            <el-select v-model="searchForm.sort" @change="loadMovies" style="width: 120px;">
                                <template #prefix><el-icon><Sort /></el-icon></template>
                                <el-option label="最新发布" value="newest" />
                                <el-option label="评分最高" value="rating" />
                            </el-select>
                        </el-form-item>

                        <!-- 3. 电影名称 (作为辅助) -->
                        <el-form-item>
                            <el-input 
                                v-model="searchForm.title" 
                                placeholder="在结果中查找..." 
                                clearable 
                                @clear="loadMovies"
                                @keyup.enter="loadMovies"
                                :prefix-icon="Search"
                            />
                        </el-form-item>
                        
                        <el-form-item>
                            <el-button type="primary" @click="loadMovies" round>筛选</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <div v-if="movieList.length > 0" class="movie-grid">
                    <el-row :gutter="20"> 
                        <el-col :span="6" v-for="movie in movieList" :key="movie.movieId">
                            <div class="movie-card-v2" @click="$router.push('/movie/' + movie.movieId)">
                                <div class="poster-wrapper">
                                    <el-image :src="movie.coverUrl" fit="cover" loading="lazy" />
                                    <div class="card-mask">
                                        <div class="play-icon">▶</div>
                                    </div>
                                    <div class="rating-badge">{{ movie.rating }}</div>
                                </div>
                                <div class="info-wrapper"> 
                                    <div class="movie-title">{{ movie.title }}</div>
                                    <div class="movie-meta">
                                        <span class="genre-tag">{{ movie.genre || '未知' }}</span>
                                        <span class="year-text">{{ movie.releaseYear }}</span>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <el-empty v-else description="暂无符合条件的电影" />
            </el-col>

            <el-col :span="6">
                <div class="ranking-card">
                    <div class="ranking-header">
                        <el-icon color="#f56c6c"><Trophy/></el-icon>
                        <span>热门榜单 TOP 10</span>
                    </div>

                    <div class="ranking-list">
                        <div v-for="(movie, index) in hotMovies" :key="movie.movieId"
                             class="ranking-item" @click="$router.push('/movie/' + movie.movieId)">
                            <span class="rank-num" :class="{'top-three': index < 3}">{{ index + 1 }}</span>
                            <span class="rank-name">{{ movie.title }}</span>
                            <span class="rank-score">{{ movie.rating }}</span>
                        </div>
                    </div>
                </div>
            </el-col>         
        </el-row>
    </div>
</template>

<style scoped>
/* 容器布局 */
.home-container {
    max-width: 1440px; 
    margin: 0 auto; 
    padding: 20px 40px;
}

/* 筛选栏美化 */
.filter-section {
    background: rgba(30, 41, 59, 0.5);
    border-radius: 12px;
    padding: 20px 20px 5px 20px;
    margin-bottom: 30px;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

:deep(.custom-form .el-form-item__label) {
    color: #94a3b8;
}

:deep(.el-input__wrapper), :deep(.el-select .el-input__wrapper) {
    background-color: #0f172a !important;
    box-shadow: none !important;
    border: 1px solid #334155 !important;
}

/* 电影卡片 V2 */
.movie-card-v2 {
    background: linear-gradient(145deg, #1e293b, #111827);
    border-radius: 12px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    margin-bottom: 25px;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

.movie-card-v2:hover {
    transform: translateY(-10px);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
    border-color: #3b82f6;
}

.poster-wrapper {
    position: relative;
    height: 280px;
    overflow: hidden;
}

.poster-wrapper .el-image {
    width: 100%;
    height: 100%;
    transition: transform 0.6s;
}

.movie-card-v2:hover .el-image {
    transform: scale(1.1);
}

/* 悬浮遮罩 */
.card-mask {
    position: absolute;
    inset: 0;
    background: rgba(0,0,0,0.4);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.movie-card-v2:hover .card-mask {
    opacity: 1;
}

.play-icon {
    font-size: 40px;
    color: white;
    border: 2px solid white;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding-left: 5px;
}

.rating-badge {
    position: absolute;
    top: 12px;
    right: 12px;
    background: #fbbf24;
    color: #000;
    padding: 2px 8px;
    border-radius: 6px;
    font-weight: 800;
    font-size: 14px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.info-wrapper {
    padding: 15px;
}

.movie-title {
    color: #f1f5f9;
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 8px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.movie-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.genre-tag {
    color: #3b82f6;
    font-size: 12px;
    background: rgba(59, 130, 246, 0.1);
    padding: 2px 6px;
    border-radius: 4px;
}

.year-text {
    font-size: 12px;
    color: #64748b;
}

/* 右侧榜单美化 */
.ranking-card {
    background: #1e293b;
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

.ranking-header {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 10px;
    color: #f1f5f9;
}

.ranking-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    cursor: pointer;
    transition: background 0.3s;
}

.ranking-item:hover {
    background: rgba(255, 255, 255, 0.02);
}

.rank-num {
    width: 30px;
    font-weight: bold;
    font-style: italic;
    color: #64748b;
}

.rank-num.top-three {
    color: #f56c6c;
    font-size: 20px;
    text-shadow: 0 0 10px rgba(245, 108, 108, 0.4);
}

.rank-name {
    flex: 1;
    font-size: 14px;
    color: #cbd5e1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.rank-score {
    color: #fbbf24;
    font-weight: bold;
    font-size: 14px;
}
</style>