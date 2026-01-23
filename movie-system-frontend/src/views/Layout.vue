<script setup>
import { Monitor, User, Search, Bell, ArrowDown, Promotion, CloseBold, Loading } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElNotification, ElMessageBox } from 'element-plus';
import request from '../utils/request';

const router = useRouter()
let socket = null

const user = JSON.parse(localStorage.getItem('user') || '{}')
const msgCount = ref(0)
const msgList = ref([])

const getUserAvatar = () => {
  return user.avatar || '';
}

const logout = () => {
    if (socket) socket.close()
    localStorage.removeItem('user')
    localStorage.removeItem('token')
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

// --- AI 聊天逻辑 ---
const showChat = ref(false)
const userQuestion = ref('')
const aiLoading = ref(false)
const chatBodyRef = ref(null)
const chatHistory = ref([
  { role: 'ai', content: '你好！我是小影，想看什么电影？我可以帮你推荐哦~' }
])

const toggleChat = () => {
  showChat.value = !showChat.value
}

const sendToAi = () => {
  if (!userQuestion.value.trim() || aiLoading.value) return

  const question = userQuestion.value
  chatHistory.value.push({ role: 'user', content: question })
  userQuestion.value = ''
  aiLoading.value = true
  
  scrollToBottom()

  request.get('/ai/chat', {
    params: { message: question },
    timeout: 500000
  }).then(res => {
    if (res.code === 200) {
      chatHistory.value.push({ role: 'ai', content: res.data })
    } else {
      chatHistory.value.push({ role: 'ai', content: '通讯链路故障，请稍后再试。' })
    }
  }).finally(() => {
    aiLoading.value = false
    scrollToBottom()
  })
}

// 自动滚动到底部
const scrollToBottom = () => {
  setTimeout(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  }, 100)
}
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
          <el-badge v-if="user.userId" :value="msgCount" :hidden="msgCount === 0" class="msg-badge">
            <el-button link class="action-btn" @click="showMsgBox">
              <el-icon><Bell /></el-icon>
            </el-button>
          </el-badge>

          <el-dropdown v-if="user.userId" trigger="click">
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
          <div v-else class="guest-login" @click="$router.push('/login')">
            <el-avatar :size="32" class="guest-avatar">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="guest-text">登录 / 注册</span>
          </div>
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
    
    <!-- AI 悬浮球 -->
    <div class="ai-pulse-wrapper" @click="toggleChat">
        <div class="pulse-ring"></div>
        <div class="ai-float-btn">
            <span class="ai-icon">🤖</span>
        </div>
    </div>

    <transition name="el-zoom-in-bottom">
        <div v-if="showChat" class="chat-window-premium">
          <div class="chat-header-modern">
            <div class="title-area">
                <span class="status-dot"></span>
                <span>智能影评助手</span>
            </div>
            <el-icon class="close-icon" @click="showChat = false"><CloseBold /></el-icon>
          </div>
          
          <div class="chat-body-modern" ref="chatBodyRef">
            <div v-for="(msg, index) in chatHistory" :key="index" :class="['chat-msg', msg.role]">
              <div class="msg-avatar">
                <template v-if="msg.role === 'user'">
                    <el-avatar :size="32" :src="user.avatar" class="user-avatar-shadow">
                        {{ (user.nickname || user.username || 'U').charAt(0).toUpperCase() }}
                    </el-avatar>
                </template>
                <div v-else class="ai-avatar-icon">🤖</div>
              </div>
              <div class="msg-content-wrapper">
                  <div class="msg-content">{{ msg.content }}</div>
              </div>
            </div>

            <div v-if="aiLoading" class="chat-msg ai">
              <div class="msg-avatar"><div class="ai-avatar-icon">🤖</div></div>
              <div class="msg-content-wrapper">
                  <div class="typing-loader">
                      <span></span><span></span><span></span>
                  </div>
              </div>
            </div>
          </div>

          <div class="chat-footer-modern">
            <el-input 
              v-model="userQuestion" 
              placeholder="输入你想探讨的电影问题..." 
              @keyup.enter="sendToAi"
              :disabled="aiLoading"
            >
              <template #suffix>
                <el-icon 
                    class="send-icon" 
                    :class="{'is-active': userQuestion.length > 0}"
                    @click="sendToAi"
                ><Promotion /></el-icon>
              </template>
            </el-input>
          </div>
        </div>
    </transition>
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
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.3);
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
  background-clip: text;
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

/* --- AI 悬浮球呼吸灯 --- */
.ai-pulse-wrapper {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 2001;
  cursor: pointer;
}
.ai-float-btn {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.5);
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.ai-pulse-wrapper:hover .ai-float-btn {
  transform: scale(1.1) translateY(-5px);
}
.pulse-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  background: #3b82f6;
  border-radius: 50%;
  animation: pulse-animation 2s infinite;
  z-index: -1;
}
@keyframes pulse-animation {
  0% { transform: scale(0.95); opacity: 0.7; }
  100% { transform: scale(1.6); opacity: 0; }
}

/* --- 聊天窗口美化 --- */
.chat-window-premium {
  position: fixed;
  bottom: 100px;
  right: 30px;
  width: 380px;
  height: 580px;
  background: #1e293b;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  flex-direction: column;
  z-index: 2000;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.6);
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.chat-header-modern {
  padding: 18px 20px;
  background: rgba(255, 255, 255, 0.03);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title-area {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    color: #f8fafc;
}
.status-dot {
    width: 8px;
    height: 8px;
    background: #10b981;
    border-radius: 50%;
    box-shadow: 0 0 10px #10b981;
}
.close-icon {
    cursor: pointer;
    color: #94a3b8;
    transition: 0.3s;
}
.close-icon:hover { color: #f1f5f9; transform: rotate(90deg); }

.chat-body-modern {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  scrollbar-width: thin;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 消息气泡设计 */
.chat-msg {
  display: flex;
  gap: 12px;
  max-width: 90%;
  animation: fadeIn 0.4s ease;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.chat-msg.user { align-self: flex-end; flex-direction: row-reverse; }
.chat-msg.ai { align-self: flex-start; }

.ai-avatar-icon {
    width: 32px;
    height: 32px;
    background: #334155;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    border: 1px solid rgba(255,255,255,0.1);
}

.msg-content {
  padding: 12px 16px;
  border-radius: 15px;
  font-size: 14px;
  line-height: 1.6;
  position: relative;
}
.ai .msg-content { 
    background: rgba(255, 255, 255, 0.05); 
    color: #e2e8f0;
    border-top-left-radius: 2px;
}
.user .msg-content { 
    background: linear-gradient(135deg, #3b82f6, #2563eb); 
    color: white; 
    border-top-right-radius: 2px;
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

/* 输入框区域 */
.chat-footer-modern {
  padding: 20px;
  background: rgba(15, 23, 42, 0.5);
}
:deep(.el-input__wrapper) {
    background: rgba(255, 255, 255, 0) !important;
    border-radius: 12px !important;
    padding: 8px 15px !important;
}
:deep(.chat-footer-modern .el-input__inner) {
  color: #fff !important;
}
.send-icon {
    font-size: 20px;
    color: #475569;
    cursor: pointer;
    transition: 0.3s;
}
.send-icon.is-active {
    color: #3b82f6;
}

/* 打字动画 */
.typing-loader {
    display: flex;
    padding: 10px;
    gap: 4px;
}
.typing-loader span {
    width: 6px;
    height: 6px;
    background: #94a3b8;
    border-radius: 50%;
    animation: typing 1.4s infinite;
}
.typing-loader span:nth-child(2) { animation-delay: 0.2s; }
.typing-loader span:nth-child(3) { animation-delay: 0.4s; }
@keyframes typing {
    0%, 100% { transform: translateY(0); opacity: 0.4; }
    50% { transform: translateY(-4px); opacity: 1; }
}
</style>