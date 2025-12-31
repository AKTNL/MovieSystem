<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const movieList = ref([])

// 页面加载时调用
onMounted(() => {
  loadMovies()
})

const loadMovies = () => {
  // 请求后端接口 /movie/list
  request.get('/movie/list').then(res => {
    if (res.code === 200) {
      movieList.value = res.data
      console.log("获取电影成功：", res.data)
    } else {
      console.error("获取失败：", res.msg)
    }
  })
}
</script>

<template>
  <div style="padding: 20px;">
    <h1>🎬 电影评分系统</h1>
    <!-- 电影列表卡片 -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="movie in movieList" :key="movie.movieId" style="margin-bottom: 20px;">
        <el-card :body-style="{ padding: '0px' }" shadow="hover">
          <img src="https://via.placeholder.com/300x400" class="image" style="width: 100%; display: block;"/>
          <div style="padding: 14px;">
            <span>{{ movie.title }}</span>
            <div class="bottom">
              <el-tag size="small">{{ movie.genre }}</el-tag>
              <span class="rating">⭐ {{ movie.rating }}</span>
            </div>
            <div class="desc">
              {{ movie.releaseYear }} 年 | {{ movie.duration }} 分钟
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>

<style scoped>
.bottom{
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rating{
  color: #ff9900;
  font-weight: bold;
}

.desc{
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}
</style>