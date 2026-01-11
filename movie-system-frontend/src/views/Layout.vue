<script setup>
import { Monitor, User, Search, Bell, ArrowDown } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElNotification, ElMessageBox } from 'element-plus';

const router = useRouter()
let socket = null

const user = JSON.parse(localStorage.getItem('user') || '{}')
const msgCount = ref(0)
const msgList = ref([])

const logout = () => {
    if (socket) socket.close()
    localStorage.removeItem('user')
    router.push('/login')
}

const searchKeyword = ref('')
const handleSearch = () => {
  if (searchKeyword.value) {
    router.push({ path: '/search', query: { q: searchKeyword.value } })
  }
}

// WebSocket 逻辑优化
const initWebSocket = () => {
    const socketUrl = `ws://localhost:8080/ws/${user.userId}`
    if (typeof (WebSocket) === "undefined") return

    socket = new WebSocket(socketUrl)
    socket.onmessage = (msg) => {
        msgCount.value++
        msgList.value.push(msg.data)
        ElNotification({
            title: '系统通知',
            message: msg.data,
            type: 'info',
            position: 'bottom-right',
            customClass: 'movie-notification'
        })
    }
}

const showMsgBox = () => {
    const content = msgList.value.length > 0 ? msgList.value.join('<br>') : '暂无新消息'
    msgCount.value = 0
    ElMessageBox.alert(content, '历史消息', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '我知道了',
        customClass: 'movie-message-box'
    })
}

onMounted(() => { 
    if (user.userId) initWebSocket()
})

onUnmounted(() => {
    if (socket) socket.close()
})
</script>

<template>
  <div class="layout-container">
    <el-header class="navbar">
      <div class="nav-content">
        <div class="logo" @click="router.push('/')">
          <span class="logo-emoji">🎬</span>
          <span class="logo-text">电影评分系统</span>
        </div>

        <div class="search-section">
          <el-input 
            v-model="searchKeyword" 
            placeholder="搜电影、影人、导演..." 
            :prefix-icon="Search" 
            class="custom-search"
            clearable
            @keyup.enter="handleSearch" 
          />
        </div>

        <div class="actions">
          <el-badge :value="msgCount" :hidden="msgCount === 0" class="msg-badge">
            <el-button link class="action-btn" @click="showMsgBox">
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>

          <el-dropdown trigger="click">
            <div class="user-info">
              <el-avatar :size="32" class="avatar-placeholder">
                {{ (user.nickname || user.username || 'U').charAt(0).toUpperCase() }}
              </el-avatar>
              <span class="username">{{ user.nickname || user.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="custom-dropdown">
                <el-dropdown-item @click="$router.push('/')">
                    <el-icon><HomeFilled /></el-icon>
                    返回首页
                </el-dropdown-item>
                <el-dropdown-item v-if="user.role === 'admin'" @click="router.push('/admin')" class="admin-item">
                  <el-icon><Monitor /></el-icon>后台管理
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/user-center')">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item divided @click="logout" class="logout-item">
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <el-main class="main-body">
      <div class="content-wrapper">
        <router-view v-slot="{ Component }">
          <transition name="fade-page" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </el-main>
  </div>
</template>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #0f172a; /* 延续登录页的深色背景 */
  color: #f1f5f9;
}

/* 磨砂玻璃导航栏 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 64px;
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  padding: 0 5%;
}

.nav-content {
  max-width: 1400px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* Logo 样式 */
.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 10px;
}
.logo-emoji { font-size: 24px; }
.logo-text {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1px;
  background: linear-gradient(90deg, #60a5fa, #3b82f6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 搜索框美化 */
.search-section {
  flex: 0 1 400px;
  margin: 0 20px;
}
:deep(.custom-search .el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.08) !important;
  box-shadow: none !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  border-radius: 20px;
  transition: all 0.3s;
}
:deep(.custom-search .el-input__wrapper.is-focus) {
  background-color: rgba(255, 255, 255, 0.12) !important;
  border-color: #3b82f6 !important;
}
:deep(.custom-search .el-input__inner) {
  color: #fff !important;
}

/* 用户区 */
.actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.action-btn {
  font-size: 20px;
  color: #94a3b8;
  transition: color 0.3s;
}
.action-btn:hover { color: #f1f5f9; }

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #f1f5f9;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.3s;
}
.user-info:hover { background: rgba(255, 255, 255, 0.05); }

.avatar-placeholder {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  font-weight: bold;
}

/* 主体内容区 */
.main-body {
  padding-top: 84px; /* 64px navbar + 20px gap */
  min-height: 100vh;
}
.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面切换动画 */
.fade-page-enter-active, .fade-page-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-page-enter-from {
  opacity: 0;
  transform: translateY(10px);
}
.fade-page-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* --- 下拉菜单容器美化 --- */
:deep(.el-dropdown-menu) {
  background-color: #1e293b; /* 保持深色底 */
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 8px 0;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.5); /* 增加投影，使其有浮起感 */
}

/* --- 下拉项文字优化 --- */
:deep(.el-dropdown-menu__item) {
  color: #000 !important; /* 将文字调亮为 slate-200，更接近白色但又不刺眼 */
  font-size: 14px;
  padding: 10px 20px;
  display: flex;
  align-items: center;
  gap: 12px; /* 图标和文字的间距 */
  transition: all 0.2s;
}

/* 统一图标颜色，增加存在感 */
:deep(.el-dropdown-menu__item .el-icon) {
  font-size: 16px;
  color: #60a5fa; /* 默认图标用柔和的蓝色 */
}

/* --- Hover 交互美化 --- */
:deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(59, 130, 246, 0.15) !important; /* 使用浅蓝色透明背景 */
  color: #1aafaf !important; /* 悬浮时文字全白 */
}

:deep(.el-dropdown-menu__item:hover .el-icon) {
  color: #93c5fd; /* 悬浮时图标也稍微变亮 */
}

/* --- 特殊项目颜色强化 --- */
.admin-item { 
  color: #93c5fd !important; /* 后台管理使用更浅的蓝色 */
  font-weight: 500;
}
.admin-item .el-icon {
  color: #60a5fa !important;
}

.logout-item { 
  color: #fca5a5 !important; /* 退出登录使用更亮的淡红色 */
}
.logout-item:hover {
  background-color: rgba(239, 68, 68, 0.15) !important; /* 退出悬浮时变红 */
  color: #ef4444 !important;
}
</style>