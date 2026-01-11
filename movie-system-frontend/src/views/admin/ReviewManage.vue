<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, ChatDotSquare, StarFilled, Film } from '@element-plus/icons-vue'
import { getAllReviews, deleteReviewByAdmin } from '../../api/review';

const tableData = ref([])
const loading = ref(false)

onMounted(() => {
    load()
})

function load() {
    loading.value = true
    getAllReviews().then(res => {
        tableData.value = res.data
        loading.value = false
    })
}

const handleDelete = (id) => {
    ElMessageBox.confirm('确定删除这条评论吗？此操作会导致该电影的平均分重新计算。', '系统警告', {
        confirmButtonText: '确认删除',
        cancelButtonText: '保留评论',
        type: 'warning',
        customClass: 'dark-message-box'
    }).then(() => {
        deleteReviewByAdmin(id).then(res => {
            if (res.code === 200) {
                ElMessage.success('评论已成功移除')
                load()
            } else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(() => {
        ElMessage.info('操作已撤销')
    })
}
</script>

<template> 
    <div class="management-page">
        <div class="action-bar-glass">
            <div class="bar-left">
                <div class="page-title">
                    <el-icon><ChatDotSquare /></el-icon>
                    <span>舆情监控中心</span>
                </div>
            </div>
            <div class="bar-right">
                <span class="stat-tag">当前累计评论: {{ tableData.length }} 条</span>
            </div>
        </div>

        <div class="table-glass-wrapper">
            <el-table 
                :data="tableData" 
                v-loading="loading"
                row-class-name="dark-table-row"
                style="width: 100%"
            >
                <el-table-column prop="reviewId" label="ID" width="70" align="center" />
                
                <el-table-column label="关联电影" width="160">
                    <template #default="scope">
                        <div class="movie-tag">
                            <el-icon><Film /></el-icon>
                            <span>{{ scope.row.movieTitle }}</span>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="username" label="发布者" width="100">
                    <template #default="scope">
                        <span class="user-highlight">@{{ scope.row.username }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="评分等级" width="160">
                    <template #default="scope">
                        <div class="score-container">
                            <el-rate
                                :model-value="scope.row.score / 2"
                                disabled
                                show-score
                                text-color="#ff9900"
                                :max="5"
                                score-template="{value}"
                            />
                            
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="content" label="评论深度内容" min-width="250">
                    <template #default="scope">
                        <div class="content-bubble">
                            {{ scope.row.content }}
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="createTime" label="发布时间序列" width="180" align="center" />

                <el-table-column label="操作指令" width="100" fixed="right" align="center">
                    <template #default="scope">
                        <el-button 
                            class="del-btn"
                            :icon="Delete" 
                            circle
                            @click="handleDelete(scope.row.reviewId)" 
                        />
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<style scoped>
/* 核心暗蓝背景 */
.management-page {
    padding: 10px;
    background: #0f172a;
    min-height: 80vh;
}

/* 顶部操作栏玻璃态 */
.action-bar-glass {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 18px 25px;
    background: rgba(30, 41, 59, 0.7);
    backdrop-filter: blur(12px);
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, 0.08);
    margin-bottom: 25px;
}

.page-title {
    color: #f59e0b; /* 橙黄色系，代表评价 */
    display: flex;
    align-items: center;
    gap: 10px;
    font-weight: bold;
    font-size: 18px;
}

.stat-tag {
    background: rgba(245, 158, 11, 0.1);
    color: #fbbf24;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    border: 1px solid rgba(245, 158, 11, 0.3);
}

/* 表格容器样式 */
.table-glass-wrapper {
    background: rgba(30, 41, 59, 0.5);
    border-radius: 20px;
    padding: 15px;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

:deep(.el-table) {
    background: transparent !important;
    color: #cbd5e1 !important;
}

:deep(.el-table tr) { background: transparent !important; }

:deep(.el-table th.el-table__cell) {
    background: rgba(15, 23, 42, 0.5) !important;
    color: #94a3b8;
    border-bottom: 1px solid rgba(255,255,255,0.05);
}

/* 电影标题标签 */
.movie-tag {
    display: flex;
    align-items: center;
    gap: 6px;
    color: #3b82f6;
    font-weight: 500;
}

.user-highlight {
    color: #94a3b8;
    font-family: 'Courier New', Courier, monospace;
}

/* 评分区域美化 */
.score-container :deep(.el-rate__text) {
    font-size: 16px;
    font-weight: bold;
    margin-left: 8px;
    text-shadow: 0 0 8px rgba(255, 153, 0, 0.4);
}

/* 评论内容气泡感 */
.content-bubble {
    background: rgba(255, 255, 255, 0.03);
    padding: 8px 12px;
    border-radius: 8px;
    color: #e2e8f0;
    line-height: 1.5;
    border-left: 3px solid #f59e0b;
}

/* 交互高亮 */
:deep(.el-table__row.dark-table-row:hover > td) {
    background-color: rgba(255, 255, 255, 0.05) !important;
    transition: 0.2s;
}

/* 删除按钮 */
.del-btn {
    background: rgba(239, 68, 68, 0.1);
    border: 1px solid rgba(239, 68, 68, 0.2);
    color: #ef4444;
}
.del-btn:hover {
    background: #ef4444;
    color: white;
    box-shadow: 0 0 15px rgba(239, 68, 68, 0.5);
}

/* 解决 Element PlusRate 在暗色背景下的默认灰度 */
:deep(.el-rate__item .el-icon) {
    font-size: 14px;
}
</style>