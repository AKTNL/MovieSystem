<script setup>
import { Monitor } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter()
// 从缓存获取用户信息，如果没有则为空对象
const user = JSON.parse(localStorage.getItem('user') || '{}')
const logout = () => {
    localStorage.removeItem('user')
    router.push('/login')
}
</script>

<template>
    <div>
        <!--头部导航-->
        <el-header style="border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between;">
            <div style="font-size: 20px; font-weight: bold; color: #409EFF">🎬 电影评分系统</div>
            <div style="flex: 1; margin: 0 40px;">
                <el-input placeholder="搜索电影..." prefix-icon="Search" style="width: 300px"/>
            </div>
            <div>
                <el-dropdown>
                    <span class="el-dropdown-link" style="cursor: pointer; display: flex; align-items: center;">
                        {{ user.nickname || user.username }}
                        <el-icon class="el-icon--right"><arrow-down/></el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item
                                v-if="user.role === 'admin'"
                                @click="$router.push('/admin')"
                                style="color: #409EFF; font-weight: bold;"
                            >
                                <el-icon><monitor/></el-icon>
                                后台管理
                            </el-dropdown-item>
                        </el-dropdown-menu>
                        <el-dropdown-menu>
                            <el-dropdown-item>个人中心</el-dropdown-item>
                            <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </el-header>

        <!--主体内容-->
        <el-main>
            <router-view/>
        </el-main>
    </div>
</template>