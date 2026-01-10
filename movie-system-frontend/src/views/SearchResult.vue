<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import request from '../utils/request'

const route = useRoute()
const keyword = ref('')
const result = ref({})

const doSearch = () => { 
    keyword.value = route.query.q
    if (keyword.value) {
        request.get('/search/all', { params: { keyword: keyword.value } }).then(res => {
            if (res.code === 200) {
                result.value = res.data
            }
        })
    }
}

onMounted(() => { 
    doSearch()
})

// 监听路由变化
watch(() => route.query.q, () => { 
    doSearch()
})
</script>

<template>
  <div style="max-width: 1200px; margin: 20px auto;">
    <h2>搜索结果: "{{ keyword }}"</h2>

    <!-- 电影结果 -->
    <el-card shadow="never" style="margin-bottom: 20px;">
      <template #header><span style="font-weight: bold">电影</span></template>
      <div v-if="result.movies && result.movies.length">
        <el-row :gutter="20">
          <el-col :span="6" v-for="m in result.movies" :key="m.id">
            <el-card :body-style="{ padding: '0px' }" shadow="hover" @click="$router.push('/movie/' + m.id)" style="cursor: pointer">
              <el-image :src="m.coverUrl" style="width: 100%; height: 200px;" fit="cover" />
              <div style="padding: 10px; text-align: center;">{{ m.title }}</div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <el-empty v-else description="没找到相关电影" />
    </el-card>

    <!-- 演员结果 -->
    <el-card shadow="never" style="margin-bottom: 20px;">
      <template #header><span style="font-weight: bold">演员</span></template>
      <div v-if="result.actors && result.actors.length">
        <el-row :gutter="20">
          <el-col :span="4" v-for="a in result.actors" :key="a.id" style="text-align: center;">
            <div @click="$router.push('/actor/' + a.id)" style="cursor: pointer">
              <el-avatar :size="80" :src="a.avatarUrl" />
              <div style="margin-top: 5px;">{{ a.name }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-empty v-else description="没找到相关演员" />
    </el-card>

    <!-- 导演结果 -->
    <el-card shadow="never">
      <template #header><span style="font-weight: bold">导演</span></template>
      <div v-if="result.directors && result.directors.length">
        <el-row :gutter="20">
          <el-col :span="4" v-for="d in result.directors" :key="d.id" style="text-align: center;">
             <div @click="$router.push('/director/' + d.id)" style="cursor: pointer">
              <el-avatar :size="80" :src="d.avatarUrl" />
              <div style="margin-top: 5px;">{{ d.name }}</div>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-empty v-else description="没找到相关导演" />
    </el-card>
  </div>
</template>