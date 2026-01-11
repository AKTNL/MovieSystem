<script setup>
import { HomeFilled, VideoCamera } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')

const logout = () => {
    localStorage.removeItem('user')
    router.push('/login')
}
</script>

<template>
  <div class="admin-layout">
    <el-container class="main-container">
      <el-aside width="240px" class="aside-menu">
        <div class="admin-logo">
          <div class="logo-icon">M</div>
          <span class="logo-text">电影系统管理</span>
        </div>
        
        <el-menu
          :default-active="$route.path"
          class="custom-menu"
          router
          background-color="transparent"
          text-color="#94a3b8"
          active-text-color="#ffffff"
        >
          <div class="menu-group-label">核心数据</div>
          <el-menu-item index="/admin/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>数据大屏看板</span>
          </el-menu-item>

          <div class="menu-group-label">内容管理</div>
          <el-menu-item index="/admin/movie">
            <el-icon><Film /></el-icon>
            <span>电影资源库</span>
          </el-menu-item>
          <el-menu-item index="/admin/actor">
            <el-icon><User /></el-icon>
            <span>演员演职表</span>
          </el-menu-item>
          <el-menu-item index="/admin/director">
            <el-icon><VideoCamera /></el-icon>
            <span>导演工作室</span>
          </el-menu-item>

          <div class="menu-group-label">系统维护</div>
          <el-menu-item index="/admin/user">
            <el-icon><Avatar /></el-icon>
            <span>用户权限管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/review">
            <el-icon><Message /></el-icon>
            <span>影评风控审核</span>
          </el-menu-item>

          <el-divider class="menu-divider" />
          
          <el-menu-item index="/">
            <el-icon><HomeFilled /></el-icon>
            <span>返回前台首页</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container class="content-container">
        <el-header class="admin-header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>管理中心</el-breadcrumb-item>
              <el-breadcrumb-item>{{ $route.meta.title || '当前页面' }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <div class="admin-info">
              <el-avatar :size="32" class="admin-avatar">
                {{ user.nickname?.charAt(0) || 'A' }}
              </el-avatar>
              <span class="admin-name">{{ user.nickname || user.username }}</span>
              <span class="role-tag">管理员</span>
            </div>
            <el-divider direction="vertical" />
            <el-button class="logout-btn" link @click="logout">
              <el-icon><SwitchButton /></el-icon>退出登录
            </el-button>
          </div>
        </el-header>

        <el-main class="admin-main">
          <div class="main-wrapper">
            <router-view v-slot="{ Component }">
              <transition name="fade-transform" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
/* 容器基础样式 */
.admin-layout {
  height: 100vh;
  background-color: #0f172a;
  color: #f1f5f9;
}

.main-container {
  height: 100%;
}

/* --- 左侧侧边栏美化 --- */
.aside-menu {
  background-color: #1e293b;
  border-right: 1px solid rgba(255, 255, 255, 0.05);
  display: flex;
  flex-direction: column;
  transition: all 0.3s;
}

.admin-logo {
  height: 80px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  gap: 12px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 18px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1px;
  color: #f1f5f9;
}

.menu-group-label {
  padding: 20px 24px 8px;
  font-size: 12px;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.custom-menu {
  border-right: none !important;
  flex: 1;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  margin: 4px 12px;
  border-radius: 8px;
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
  color: #3b82f6 !important;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(59, 130, 246, 0.15), transparent) !important;
  border-left: 3px solid #3b82f6;
  color: #3b82f6 !important;
}

.menu-divider {
  margin: 20px 0;
  border-color: rgba(255, 255, 255, 0.05);
}

/* --- 右侧 Header 美化 --- */
.admin-header {
  background-color: #0f172a;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  height: 64px !important;
}

:deep(.el-breadcrumb__inner) {
  color: #64748b !important;
}
:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #f1f5f9 !important;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.admin-avatar {
  background: #3b82f6;
  font-weight: bold;
}

.admin-name {
  font-weight: 500;
  color: #f1f5f9;
}

.role-tag {
  font-size: 10px;
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
  padding: 2px 6px;
  border-radius: 4px;
  border: 1px solid rgba(59, 130, 246, 0.2);
}

.logout-btn {
  color: #94a3b8 !important;
  font-size: 14px;
}
.logout-btn:hover {
  color: #f87171 !important;
}

/* --- 主内容区 --- */
.admin-main {
  background-color: #0b1120; /* 略深于 header 的颜色 */
  padding: 24px;
}

.main-wrapper {
  max-width: 1400px;
  margin: 0 auto;
}

/* 页面切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-15px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(15px);
}
</style>