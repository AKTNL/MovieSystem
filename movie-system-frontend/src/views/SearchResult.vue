<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import request from '../utils/request'
import { Film, User, VideoCamera } from '@element-plus/icons-vue'

const route = useRoute()
const keyword = ref('')
const result = ref({
  movies: [],
  actors: [],
  directors: []
})
const loading = ref(false)

const doSearch = () => { 
    keyword.value = route.query.q
    if (keyword.value) {
        loading.value = true
        request.get('/search/all', { params: { keyword: keyword.value } }).then(res => {
            if (res.code === 200) {
                result.value = res.data
            }
        }).finally(() => {
            loading.value = false
        })
    }
}

onMounted(() => { doSearch() })
watch(() => route.query.q, () => { doSearch() })
</script>

<template>
  <div class="search-page" v-loading="loading" element-loading-background="rgba(15, 23, 42, 0.8)">
    <header class="search-header">
      <div class="container">
        <h2 class="search-title">
          <span class="label">搜索结果:</span>
          <span class="keyword">“{{ keyword }}”</span>
        </h2>
        <div class="result-stats">
          共找到 {{ (result.movies?.length || 0) + (result.actors?.length || 0) + (result.directors?.length || 0) }} 个相关结果
        </div>
      </div>
    </header>

    <div class="container main-content">
      <section class="result-section">
        <div class="section-header">
          <el-icon><Film /></el-icon>
          <h3>相关电影</h3>
          <span class="count">{{ result.movies?.length || 0 }}</span>
        </div>
        
        <div v-if="result.movies && result.movies.length">
          <el-row :gutter="20">
            <el-col :md="6" :sm="8" :xs="12" v-for="m in result.movies" :key="m.id">
              <div class="movie-card-mini" @click="$router.push('/movie/' + m.id)">
                <div class="poster-box">
                  <el-image :src="m.coverUrl" fit="cover" lazy />
                  <div class="hover-mask">查看详情</div>
                </div>
                <div class="movie-info">
                  <div class="title">{{ m.title }}</div>
                  <div class="meta">{{ m.releaseYear }} · {{ m.genre }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
        <el-empty v-else :image-size="80" description="未找到相关电影" />
      </section>

      <el-row :gutter="30">
        <el-col :span="12">
          <section class="result-section person-section">
            <div class="section-header">
              <el-icon><User /></el-icon>
              <h3>相关演员</h3>
            </div>
            <div v-if="result.actors && result.actors.length" class="person-grid">
              <div v-for="a in result.actors" :key="a.id" class="person-item" @click="$router.push('/actor/' + a.id)">
                <el-avatar :size="60" :src="a.avatarUrl" />
                <div class="name">{{ a.name }}</div>
              </div>
            </div>
            <el-empty v-else :image-size="60" description="未找到相关演员" />
          </section>
        </el-col>

        <el-col :span="12">
          <section class="result-section person-section">
            <div class="section-header">
              <el-icon><VideoCamera /></el-icon>
              <h3>相关导演</h3>
            </div>
            <div v-if="result.directors && result.directors.length" class="person-grid">
              <div v-for="d in result.directors" :key="d.id" class="person-item" @click="$router.push('/director/' + d.id)">
                <el-avatar :size="60" :src="d.avatarUrl" />
                <div class="name">{{ d.name }}</div>
              </div>
            </div>
            <el-empty v-else :image-size="60" description="未找到相关导演" />
          </section>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.search-page {
  min-height: 100vh;
  padding-bottom: 60px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 头部样式 */
.search-header {
  background: rgba(30, 41, 59, 0.5);
  padding: 40px 0;
  margin-bottom: 30px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.search-title {
  font-size: 28px;
  margin: 0;
  color: #f1f5f9;
}

.search-title .label { color: #94a3b8; font-weight: normal; margin-right: 10px; }
.search-title .keyword { color: #3b82f6; }

.result-stats {
  color: #64748b;
  margin-top: 10px;
  font-size: 14px;
}

/* 板块通用样式 */
.result-section {
  background: #1e293b;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 30px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 25px;
  color: #f1f5f9;
}

.section-header .el-icon { font-size: 20px; color: #3b82f6; }
.section-header h3 { margin: 0; font-size: 18px; flex: 1; }
.section-header .count {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
}

/* 电影卡片美化 */
.movie-card-mini {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.poster-box {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 2/3;
}

.poster-box .el-image { width: 100%; height: 100%; transition: scale 0.5s; }

.hover-mask {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.3s;
}

.movie-card-mini:hover { transform: translateY(-5px); }
.movie-card-mini:hover .el-image { scale: 1.1; }
.movie-card-mini:hover .hover-mask { opacity: 1; }

.movie-info { padding: 10px 0; }
.movie-info .title {
  color: #f1f5f9;
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 4px;
}
.movie-info .meta { color: #64748b; font-size: 12px; }

/* 人物列表美化 */
.person-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 15px;
}

.person-item {
  text-align: center;
  cursor: pointer;
  padding: 10px;
  border-radius: 12px;
  transition: background 0.3s;
}

.person-item:hover { background: rgba(255,255,255,0.03); }
.person-item .name {
  color: #cbd5e1;
  font-size: 13px;
  margin-top: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 覆盖 el-empty 样式 */
:deep(.el-empty__description p) { color: #64748b !important; }
</style>