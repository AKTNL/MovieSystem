<script setup>
import { reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { userUpdate, getUserInfo } from '../../api/user';
import ImageUpload from '../../components/ImageUpload.vue';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const form = reactive({})

onMounted(() => {
    loadInfo()
})

const loadInfo = () => {
    getUserInfo(user.userId).then(res => { 
        if (res.code === 200) {
            Object.assign(form, res.data)
        }
    })
}

const save = () => {
    userUpdate(form).then(res => { 
        if (res.code === 200) {
            ElMessage.success('修改成功')
            // 更新本地缓存的用户信息
            const newUser = { ...user, ...form }
            localStorage.setItem('user', JSON.stringify(newUser))

            // 刷新页面让 Header 里的名字也变过来
            setTimeout(() => location.reload(), 500)
        }else {
            ElMessage.error(res.msg)
        }
    })
}


</script>

<template>
    <div style="max-width: 600px; margin: 20px auto;">
        <el-card header="个人中心">
            <div style="text-align: center; margin-bottom: 30px;">
                <!--头像展示-->
                <el-avatar :size="100" :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"/>
                <h2 style="margin: 10px 0;">{{ form.nickname }}</h2>
                <el-tag>{{ form.role === 'admin' ? '管理员' : '普通用户' }}</el-tag>
            </div>

            <el-form :model="form" label-width="80px"> 
                <el-form-item label="用户名">
                    <el-input v-model="form.username" disabled/>
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="form.nickname"/>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="form.email"/>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input v-model="form.phone"/>
                </el-form-item>
                <el-form-item label="头像">
                    <ImageUpload v-model="form.avatar"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="save">保存</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>