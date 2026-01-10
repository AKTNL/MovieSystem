<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { getAllReviews, deleteReview } from '../../api/review';
import { id } from 'element-plus/es/locales.mjs';

const tableData = ref([])

onMounted(() => {
    load()
})

function load() {
    getAllReviews().then(res => {
        tableData.value = res.data
    })
}

const handleDelete = (id) => {
    ElMessageBox.confirm('确定删除这条评论吗？电影分数将自动更新。', '提示', {
        type: 'warning'
    }).then(() => {
        deleteReview(id).then(res => {
            if (res.code === 200) {
                ElMessage.success('删除成功')
                load()
            } else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(() => {
        ElMessage.info('已取消删除')
    })
}
</script>

<template> 
    <div>
        <el-table :data="tableData" border stripe>
            <el-table-column prop="reviewId" label="ID" width="60" />
            <el-table-column prop="movieTitle" label="电影" width="150" show-overflow-tooltip />
            <el-table-column prop="username" label="用户" width="120" />
            <el-table-column prop="score" label="评分" width="80">
                <template #default="scope">
                <span style="color: #ff9900; font-weight: bold">{{ scope.row.score }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="content" label="评论内容" show-overflow-tooltip />
            <el-table-column prop="createTime" label="时间" width="180" />
            <el-table-column label="操作" width="100">
                <template #default="scope">
                <el-button type="danger" size="small" icon="Delete" @click="handleDelete(scope.row.reviewId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
  </div>
</template>