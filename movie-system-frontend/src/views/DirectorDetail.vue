<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDirectorDetail } from '../api/director';

const route = useRoute()
const director = ref({})
const movies = ref([])

onMounted(() => {
    const id = route.params.id 
    getDirectorDetail(id).then(res => {
        if (res.code === 200) {
            director.value = res.data.director
            movies.value = res.data.movies
        }
    })
})
</script>

<template> 
    <div style="max-width: 1000px; margin: 20px auto;">
        <el-card shadow="never">
            <div style="display: flex;">
                <!-- 左侧头像 -->
                <el-image :src="director.avatarUrl" style="width: 200px; height: 280px; border-radius: 8px;" fit="cover" />
                
                <!-- 右侧信息 -->
                <div style="margin-left: 30px; flex: 1;">
                    <h1 style="margin-top: 0;">{{ director.name }}</h1>
                    <div style="color: #666; line-height: 2;">
                        <p><strong>性别：</strong> {{ director.gender === 'M' ? '男' : '女' }}</p>
                        <p><strong>国籍：</strong> {{ director.nationality }}</p>
                        <p><strong>出生日期：</strong> {{ director.birthDate || '暂无' }}</p>
                    </div>
                </div>
            </div>
        </el-card>

        <!-- 参演电影列表 -->
        <div style="margin-top: 30px;">
            <h3 style="border-left: 4px solid #409EFF; padding-left: 10px;">导演作品</h3>
            <el-row :gutter="20">
                <el-col :span="6" v-for="movie in movies" :key="movie.movieId" style="margin-bottom: 20px;">
                <el-card :body-style="{ padding: '0px' }" shadow="hover" style="cursor: pointer" @click="$router.push('/movie/' + movie.movieId)">
                    <el-image :src="movie.coverUrl" style="width: 100%; height: 250px;" fit="cover" />
                    <div style="padding: 10px; text-align: center;">
                    <div style="font-weight: bold; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">{{ movie.title }}</div>
                    <div style="color: #ff9900; font-size: 12px;">⭐ {{ movie.rating }}</div>
                    </div>
                </el-card>
                </el-col>
            </el-row>
            <el-empty v-if="movies.length === 0" description="暂无相关作品" />
        </div>
    </div>
</template>