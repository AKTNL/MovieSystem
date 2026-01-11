<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, User, Search, Monitor } from '@element-plus/icons-vue' // 引入图标
import { deleteUser, getAllUsers } from '../../api/user';

const tableData = ref([])
const loading = ref(false)

onMounted(() => {
    loadUsers()
})

const loadUsers = () => { 
    loading.value = true
    getAllUsers().then(res => {
        // 如果 res.data 是空或者 null，赋值为空数组 []
        tableData.value = res.data || [] 
    }).catch(err => {
        console.error("加载失败", err)
        tableData.value = [] // 发生异常也要保证它是数组
    }).finally(() => {
        loading.value = false
    })
}

const handleDelete = (id) => { 
    ElMessageBox.confirm('注销用户将清除其所有历史评论及关联数据，此操作不可逆，确定吗？', '严正警告', {
        confirmButtonText: '确定抹除',
        cancelButtonText: '保留用户',
        type: 'warning',
        customClass: 'dark-message-box'
    }).then(() => {
        deleteUser(id).then(res => { 
            if (res.code === 200) {
                ElMessage.success('用户权限已安全移除')
                loadUsers()
            } else {
                ElMessage.error(res.msg)
            }
        })
    })
}
</script>

<template>
    <div class="management-page">
        <div class="action-bar-glass">
            <div class="bar-left">
                <div class="page-title">
                    <el-icon><Monitor /></el-icon>
                    <span>用户权限中心</span>
                </div>
            </div>
            <div class="bar-right">
                <span class="stat-tag">活跃终端: {{ tableData.length || 0 }}</span>
            </div>
        </div>

        <div class="table-glass-wrapper">
            <el-table 
                :data="tableData" 
                v-loading="loading"
                row-class-name="dark-table-row"
                style="width: 100%"
            >
                <el-table-column prop="userId" label="ID" width="70" align="center" />
                
                <el-table-column label="数字形象" width="100" align="center">
                    <template #default="scope">
                        <div class="user-avatar-container">
                            <el-avatar 
                                :size="45" 
                                :src="scope.row.avatar" 
                                class="user-avatar"
                            >
                                <el-icon><User /></el-icon>
                            </el-avatar>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="账户信息" min-width="180">
                    <template #default="scope">
                        <div class="user-info-cell">
                            <div class="username">{{ scope.row.username }}</div>
                            <div class="nickname">{{ scope.row.nickname }}</div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="email" label="联络邮箱" min-width="180" show-overflow-tooltip />
                
                <el-table-column prop="phone" label="通讯号" width="140" align="center" />

                <el-table-column label="权限等级" width="120" align="center">
                    <template #default="scope">
                        <div class="role-badge" :class="scope.row.role === 'admin' ? 'admin' : 'user'">
                            {{ scope.row.role === 'admin' ? '系统核心' : '普通终端' }}
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="createTime" label="初始序列(注册时间)" width="180" align="center" />

                <el-table-column label="指令" width="120" fixed="right" align="center">
                    <template #default="scope">
                        <el-button
                            v-if="scope.row.role !== 'admin'"
                            class="del-btn"
                            :icon="Delete"
                            circle
                            @click="handleDelete(scope.row.userId)"
                        />
                        <span v-else class="lock-text">锁定</span>
                    </template>
                </el-table-column>    
            </el-table>
        </div>
    </div>
</template>

<style scoped>
/* 核心暗黑背景 */
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
    color: #3b82f6;
    display: flex;
    align-items: center;
    gap: 10px;
    font-weight: bold;
    font-size: 18px;
    letter-spacing: 1px;
}

.stat-tag {
    background: rgba(59, 130, 246, 0.1);
    color: #60a5fa;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    border: 1px solid rgba(59, 130, 246, 0.3);
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

/* 账户信息单元格布局 */
.user-info-cell {
    display: flex;
    flex-direction: column;
}
.username { color: #f8fafc; font-weight: bold; }
.nickname { color: #64748b; font-size: 12px; }

/* 头像动效 */
.user-avatar-container {
    display: flex;
    justify-content: center;
}
.user-avatar {
    border: 2px solid rgba(59, 130, 246, 0.2);
    transition: 0.3s;
}
:deep(.dark-table-row:hover .user-avatar) {
    transform: scale(1.15);
    border-color: #3b82f6;
}

/* 权限勋章 */
.role-badge {
    display: inline-block;
    padding: 2px 10px;
    border-radius: 6px;
    font-size: 12px;
    font-weight: 600;
}
.role-badge.admin {
    background: rgba(239, 68, 68, 0.15);
    color: #f87171;
    border: 1px solid rgba(239, 68, 68, 0.3);
}
.role-badge.user {
    background: rgba(16, 185, 129, 0.15);
    color: #34d399;
    border: 1px solid rgba(16, 185, 129, 0.3);
}

/* 交互高亮 */
:deep(.el-table__row.dark-table-row:hover > td) {
    background-color: rgba(255, 255, 255, 0.05) !important;
    transition: 0.2s;
}

/* 操作按钮 */
.del-btn {
    background: rgba(239, 68, 68, 0.1);
    border: 1px solid rgba(239, 68, 68, 0.2);
    color: #ef4444;
}
.del-btn:hover {
    background: #ef4444;
    color: white;
    box-shadow: 0 0 15px rgba(239, 68, 68, 0.4);
}

.lock-text {
    font-size: 12px;
    color: #475569;
    font-style: italic;
}
</style>