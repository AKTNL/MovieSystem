<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { login, register } from '../api/user';
import { getCaptchaApi } from '../api/captcha';
import { getMovieList } from '../api/movie';
import { User, Lock, Key } from '@element-plus/icons-vue'

const router = useRouter()
const isLogin = ref(true) //控制登录注册切换

//表单数据
const loginForm = reactive({
    username: '',
    password: '',
    code: '',
    uuid: ''
})

const captchaImg = ref('') 
//页面加载时获取验证码
onMounted(() => {
    getCaptcha()
    getPosters() // 页面加载时请求海报
})

const getCaptcha = () => { 
    getCaptchaApi().then(res => { 
        if (res.code === 200) {
            captchaImg.value = res.data.image
            loginForm.uuid = res.data.uuid
        }
    })
}

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
            localStorage.setItem('token', res.data.token) // 单独存 token
            // 把用户信息存到浏览器缓存
            localStorage.setItem('user', JSON.stringify(res.data.user))
            router.push('/') // 跳转到首页
        } else {
            ElMessage.error(res.msg)
            getCaptcha()
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

const moviePosters = ref([])

// 获取海报列表
const getPosters = () => {
    getMovieList({ pageSize: 24 }).then(res => { 
        if (res.code === 200) {
            console.log('海报数据:', moviePosters.value)
            // 确保后端返回的对象里有 avatarUrl 字段
            moviePosters.value = res.data.list || res.data
        }
    })
}
  
</script>

<template>
  <div class="login-page">
    <div class="movie-wall">
        <div class="wall-track">
            <div v-for="group in 2" :key="group" class="track-group">
                <div v-for="(movie, index) in moviePosters" :key="index" class="movie-card">
                    <img 
                    v-if="movie.coverUrl" 
                    :src="movie.coverUrl" 
                    class="poster-img"
                    loading="lazy"
                    >
                        <div v-else class="poster-empty">
                        <span>🎬</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="wall-mask"></div>
    </div>

    <div class="login-aside">
      <div class="login-box">
        <div class="header">
          <span class="logo-icon">🎬</span>
          <h2>电影评分系统</h2>
          <p class="subtitle">{{ isLogin ? '探索精彩电影世界' : '加入影迷大家庭' }}</p>
        </div>

        <transition name="fade-transform" mode="out-in">
          <div v-if="isLogin" key="login">
            <el-form :model="loginForm" @keydown.enter="handleLogin">
              <el-form-item>
                <el-input v-model="loginForm.username" placeholder="用户名" :prefix-icon="User" size="large"/>
              </el-form-item>
              <el-form-item>
                <el-input v-model="loginForm.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password size="large"/>
              </el-form-item>
              <el-form-item>
                <div class="captcha-container">
                  <el-input v-model="loginForm.code" placeholder="验证码" :prefix-icon="Key" size="large"/>
                  <img :src="captchaImg" @click="getCaptcha" class="captcha-img" title="点击刷新"/>
                </div>
              </el-form-item>
              <el-button type="primary" class="submit-btn" size="large" @click="handleLogin">立即登录</el-button>
              <div class="footer-links">
                <el-link :underline="false" @click="isLogin = false">没有账号？点击注册</el-link>
              </div>
            </el-form>
          </div>

          <div v-else key="register">
            <el-form :model="registerForm">
              <el-form-item>
                <el-input v-model="registerForm.username" placeholder="设置用户名" :prefix-icon="User" size="large"/>
              </el-form-item>
              <el-form-item>
                <el-input v-model="registerForm.password" type="password" placeholder="设置密码" :prefix-icon="Lock" size="large"/>
              </el-form-item>
              <el-form-item>
                <el-input v-model="registerForm.confirmPass" type="password" placeholder="确认密码" :prefix-icon="Lock" size="large" @keyup.enter="handleRegister"/>
              </el-form-item>
              <el-button type="success" class="submit-btn" size="large" @click="handleRegister">提交注册</el-button>
              <div class="footer-links">
                <el-link :underline="false" @click="isLogin = true">返回登录</el-link>
              </div>
            </el-form>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全屏容器 */
.login-page {
  height: 100vh;
  display: flex;
  overflow: hidden;
  background-color: #0f172a; /* 深色底色，更有电影感 */
}

/* --- 左侧电影墙动画 --- */
.movie-wall {
  flex: 1;
  position: relative;
  background: #050505;
  overflow: hidden;
  display: flex;
  justify-content: flex-start; /* 改为左对齐 */
}

.wall-track {
  display: flex;
  flex-direction: column;
  /* 确保 gap 在两组之间也是一致的 */
  gap: 30px; 
  /* padding-top 会影响计算，建议改用 margin 或加在 track-group 内部 */
  padding-top: 0; 
  transform: skewX(-10deg); 
  animation: scrollWall 40s linear infinite;
  /* 必须保证动画是 linear（线性），如果是 ease 会有停顿感 */
}
.track-group {
  display: grid;
  /* 增加到 3 列或者更多，根据你左侧空间决定 */
  grid-template-columns: repeat(6, 220px); 
  gap: 30px;
  padding: 20px;
  /* 这里的 margin-left 可以抵消倾斜带来的左侧空隙 */
  margin-left: -50px; 
}

.movie-card {
  width: 220px;
  height: 310px;
  background: #111827;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  /* 增加一个淡淡的投影，让海报更有立体感 */
  box-shadow: 0 10px 20px rgba(0,0,0,0.5);
  border: 1px solid rgba(255,255,255,0.05);
}

/* 图片充满卡片 */
.poster-img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 强制图片填满且不压缩 */
  transition: transform 0.5s ease, opacity 0.5s ease;
}

/* 悬停时的文字遮罩 */
.card-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 半透明黑 */
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
  border-radius: 12px;
}

.movie-card:hover .card-overlay {
  opacity: 1;
}

/* 悬停效果：海报放大一点点 */
.movie-card:hover .poster-img {
  transform: scale(1.1);
}

.card-overlay span {
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  transform: skewX(10deg); /* 抵消外层容器的倾斜，让文字变正 */
}

.poster-placeholder {
    writing-mode: vertical-lr; /* 竖排文字更有设计感 */
    letter-spacing: 5px;
}

@keyframes scrollWall {
  0% { 
    /* 从原始位置开始 */
    transform: skewX(-10deg) translateY(0); 
  }
  100% { 
    /* 位移正好是一组内容的高度。
       因为我们渲染了两组（group in 2），
       所以位移 -50% 理论上正好是第一组内容结束、第二组内容开头的位置。
    */
    transform: skewX(-10deg) translateY(-50%); 
  }
}

/* 遮罩，让墙面边缘淡出 */
.wall-mask {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  /* 混合遮罩：左侧深蓝色淡入，右侧向背景色融合 */
  background: linear-gradient(225deg, rgba(37, 99, 235, 0.1) 0%, transparent 40%),
              linear-gradient(90deg, transparent 60%, #0f172a 100%);
  pointer-events: none; /* 确保不挡住鼠标点击 */
}
/* --- 右侧表单区域 --- */
.login-aside {
  width: 450px;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0f172a;
  box-shadow: -20px 0 50px rgba(0,0,0,0.5);
}

.login-box {
  width: 340px;
  padding: 40px;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-icon {
  font-size: 40px;
  display: block;
  margin-bottom: 10px;
}

.header h2 {
  color: #f8fafc;
  font-size: 24px;
  margin: 0;
}

.subtitle {
  color: #64748b;
  font-size: 14px;
  margin-top: 8px;
}

/* 表单美化 */
:deep(.el-input__wrapper) {
  background-color: #1e293b !important;
  box-shadow: none !important;
  border: 1px solid #334155 !important;
}

:deep(.el-input__inner) {
  color: #fff !important;
  height: 45px;
}

.captcha-container {
  display: flex;
  width: 100%;
  gap: 12px;
}

.captcha-img {
  height: 45px;
  width: 110px;
  cursor: pointer;
  border-radius: 8px;
  transition: opacity 0.3s;
}

.captcha-img:hover { opacity: 0.8; }

.submit-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 8px;
  margin-top: 10px;
}

.footer-links {
  text-align: center;
  margin-top: 20px;
}

/* 过渡动画 */
.fade-transform-enter-active, .fade-transform-leave-active {
  transition: all 0.3s ease;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(20px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}
</style>