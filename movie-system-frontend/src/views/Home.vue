<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getMovieList, getHotMovies } from '../api/movie';
import { Trophy } from '@element-plus/icons-vue';

const movieList = ref([])
const hotMovies = ref([])

const searchForm = reactive({
    title: '',
    genre: '',
})

onMounted(() => {
    loadMovies()
    loadHot()
})

// 加载电影列表
const loadMovies = () => {
    getMovieList(searchForm).then(res => {
        if (res.code === 200) {
            movieList.value = res.data
        }
    })
}

// 加载热门电影
const loadHot = () => {
    getHotMovies().then(res => {
        if (res.code === 200) {
            hotMovies.value = res.data
        }
    })
}
</script>

<template>
    <div style="max-width: 1200px; margin: 0 auto; padding: 20px;">
        <el-row :gutter="20">
            <!--左侧主要内容区-->
            <el-col :span="18">
                <!--搜索筛选栏-->
                <el-card shadow="never" style="margin-bottom: 20px;">
                    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
                        <el-form-item label="电影名称">
                            <el-input v-model="searchForm.title" placeholder="请输入电影名称" clearable @clear="loadMovies" />
                        </el-form-item>
                        <el-form-item label="类型">
                            <el-select v-model="searchForm.genre" placeholder="全部类型" clearable @clear="loadMovies" style="width: 120px;">
                                <el-option label="剧情" value="剧情"></el-option>
                                <el-option label="动作" value="动作"></el-option>
                                <el-option label="喜剧" value="喜剧"></el-option>
                                <el-option label="爱情" value="爱情"></el-option>
                                <el-option label="科幻" value="科幻"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" icon="Search" @click="loadMovies">搜索</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>

                <!--电影列表-->
                <div v-if="movieList.length > 0">
                    <el-row :gutter="20"> 
                        <el-col :span="8" v-for="movie in movieList" :key="movie.movieId" style="margin-bottom: 20px;">
                            <el-card :body-style="{ padding: '0px' }" shadow="hover" style="cursor: pointer;" @click="$router.push('/movie/' + movie.movieId)">
                                <div style="position: relative; overflow: hidden; height: 300px;">
                                    <el-image :src="movie.coverUrl" style="width: 100%; height: 100%;" fit="cover"/>
                                    <!--评分角标-->
                                    <div style="position: absolute; top: 10px; right: 10px; background: rgba(0, 0, 0, 0.7); color: #ff9900; padding: 2px 8px; border-radius: 4px; font-weight: bold;">
                                        {{ movie.rating }}
                                    </div>
                                </div>
                                <div style="padding: 14px;"> 
                                    <div style="font-weight: bold; font-size: 16px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                        {{ movie.title }}
                                    </div>
                                    <div class="bottom">
                                        <el-tag size="small" effect="plain">
                                            {{ movie.genre || '未知' }}
                                        </el-tag>
                                        <div class="year">{{ movie.releaseYear }}</div>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                </div>
                <el-empty v-else description="暂无符合条件的电影"/>
            </el-col>

            <!--右侧热门电影-->
            <el-col :span="6">
                <el-card shadow="never">
                    <template #header>
                        <div style="font-weight: bold; display: flex; align-items: center;">
                            <el-icon color="#f56c6c" style="margin-right: 5px;"><Trophy/></el-icon>
                            热门榜单
                        </div>
                    </template>

                    <div v-for="(movie, index) in hotMovies" :key="movie.movieId"
                        style="margin-bottom: 15px; display: flex; align-items: center; cursor: pointer;"
                        @click="$router.push('/movie/' + movie.movieId)">
                        <!--排名数字-->
                        <div style="width: 25px; font-weight: bold; font-style: italic;"
                            :style="{color: index < 3 ? '#f56c6c' : '#999'}">
                            {{ index + 1 }}
                        </div>
                        <!--电影名-->
                        <div style="flex: 1; font-size: 14px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; margin-right: 10px;">
                            {{ movie.title }}
                        </div>
                        <!-- 分数 -->
                        <div style="font-size: 12px; color: #ff9900; font-weight: bold;">
                            {{ movie.rating }}
                        </div>
                    </div>
                </el-card>
            </el-col>        
        </el-row>
    </div>
</template>

<style scoped>
.bottom{
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.year{
    font-size: 12px;
    color: #999;
}
</style>