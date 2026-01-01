<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { login, register } from '../api/user';

const router = useRouter()
const isLogin = ref(true) //控制登录注册切换

//表单数据
const loginForm = reactive({
    username: '',
    password: ''
})

const registerForm = reactive({
    username: '',
    password: '',
    confirmPass: ''
})

//验证规则
const rules = {
    username: [{
        required: true,
        message: '请输入用户名',
        trigger: 'blur'
    }],
    password: [{
        required: true,
        message: '请输入密码',
        trigger: 'blur'
    }]
}

//登录逻辑
const handleLogin = () => {
    login(loginForm).then(res => {
        if (res.code === 200) {
            ElMessage.success('登录成功')
            // 把用户信息存到浏览器缓存
            localStorage.setItem('user', JSON.stringify(res.data))
            router.push('/') // 跳转到首页
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const handleRegister = () => {
    if (registerForm.password !== registerForm.confirmPass) {
        ElMessage.error('两次密码输入不一致')
        return
    }
    register(registerForm).then(res => {
        if (res.code === 200) {
            ElMessage.success('注册成功，请登录')
            isLogin.value = true //切换回登录页
        } else {
            ElMessage.error(res.msg)
        }
    })
}
</script>

<template>
    <div class="login-container">
        <div class="login-box">
            <div style="text-align: center; margin-bottom: 30px;">
                <h2>🎬 电影评分系统</h2>
            </div>

            <!--登录表单-->
            <el-form v-if="isLogin" :model="loginForm" :rules="rules" ref="loginRef" @keydown.enter="handleLogin">
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User"/>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password/>
                </el-form-item>
                <el-button type="primary" style="width: 100%" @click="handleLogin">登 录</el-button>
                <div style="text-align: right; margin-top: 10px;">
                    <el-link type="primary" @click="isLogin = false">没有账号？去注册</el-link>
                </div>
            </el-form>

            <!--注册表单-->
            <el-form v-else :model="registerForm" :rules="rules" ref="registerRef">
                <el-form-item prop="username">
                    <el-input v-model="registerForm.username" placeholder="设置用户名" prefix-icon="User"/>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="registerForm.password" type="password" placeholder="设置密码" prefix-icon="Lock"/>
                </el-form-item>
                <el-form-item prop="confirmPass">
                    <el-input v-model="registerForm.confirmPass" type="password" placeholder="确认密码" prefix-icon="Lock" @keyup.enter="handleRegister"/>
                </el-form-item>
                <el-button type="success" style="width: 100%" @click="handleRegister">注 册</el-button>
                <div style="text-align: right; margin-top: 10px;">
                    <el-link type="primary" @click="isLogin = true">已有账号？去登录</el-link>
                </div>
            </el-form>
        </div>
    </div>
</template>

<style scoped>
.login-container{
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f0f2f5;
    background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
}
.login-box{
    width: 350px;
    padding: 40px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(0,0,0,0.1);
}
</style>