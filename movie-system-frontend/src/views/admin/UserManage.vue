<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { deleteUser, getAllUsers } from '../../api/user';

const tableData = ref([])

onMounted(() => {
    loadUsers()
})

const loadUsers = () => { 
    getAllUsers().then(res => {
        tableData.value = res.data
    })
}

const handleDelete = (id) => { 
    ElMessageBox.confirm('删除用户会导致该用户的所有评论消失，确定吗？', '警告', {
        type: 'warning'
    }).then(() => {
        deleteUser(id).then(res => { 
            if (res.code === 200) {
                ElMessage.success('删除成功')
                loadUsers()
            } else {
                ElMessage.error(res.msg)
            }
        })
    })
}
</script>

<template>
    <div>
        <el-table :data="tableData" border stripe>
            <el-table-column prop="userId" label="ID" width="60" />
            <el-table-column label="头像" width="80">
                <template #default="scope">
                    <el-avatar :src="scope.row.avatar" />
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="phone" label="手机" />
            <el-table-column prop="role" label="角色">
                <template #default="scope">
                    <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'success'">
                        {{ scope.row.role === 'admin' ? '管理员' : '用户' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="注册时间"/>
            <el-table-column label="操作">
                <template #default="scope">
                    <!--不允许删除自己-->
                    <el-button
                        v-if="scope.row.role !== 'admin'"
                        type="danger"
                        size="small"
                        @click="handleDelete(scope.row.userId)"
                    >删除用户</el-button>
                </template>
            </el-table-column>    
        </el-table>
    </div>
</template>