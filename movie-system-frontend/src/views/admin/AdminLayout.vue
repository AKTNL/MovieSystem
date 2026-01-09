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
    <div class="common-layout">
        <el-container>
            <!--左侧侧边栏-->
            <el-aside width="200px" style="background-color: #304156; min-height: 100vh;">
                <div style="height: 60px; line-height: 60px; text-align: center; color: white; font-weight: bold; font-size: 20px;">
                    后台管理
                </div>
                <el-menu
                    active-text-color="#409EFF"
                    background-color="#304156"
                    text-color="#fff"
                    router
                    :default-active="$route.path"
                >
                    <el-menu-item index="/admin/dashboard">
                        <el-icon><Odometer /></el-icon>
                        <span>数据大屏</span>
                    </el-menu-item>
                    <el-menu-item index="/admin/movie">
                        <el-icon><Film /></el-icon>
                        <span>电影管理</span>
                    </el-menu-item>
                    <el-menu-item index="/admin/actor">
                        <el-icon><User /></el-icon>
                        <span>演员管理</span>
                    </el-menu-item>
                    <el-menu-item index="/admin/director">
                        <el-icon><VideoCamera /></el-icon>
                        <span>导演管理</span>
                    </el-menu-item>
                    <el-menu-item index="/admin/user">
                        <el-icon><Avatar /></el-icon>
                        <span>用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="/">
                        <el-icon><HomeFilled /></el-icon>
                        <span>返回前台</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <!--右侧主体-->
            <el-container>
                <el-header style="text-align: right; font-size: 12px; border-bottom: 1px solid #eee; line-height: 60px;">
                    <span>管理员：{{ user.nickname || user.username }}</span>
                    <el-button type="danger" link @click="logout" style="margin-left: 15px;">退出</el-button>
                </el-header>
                <el-main>
                    <!--子路由出口-->
                    <router-view />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
.el-menu {
    border-right: none;
}
</style>