<script setup>
import { Monitor, User } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue'
import { onMounted, onUnmounted } from 'vue'
import { ElNotification } from 'element-plus';

const router = useRouter()

let socket = null

// 从缓存获取用户信息，如果没有则为空对象
const user = JSON.parse(localStorage.getItem('user') || '{}')
const logout = () => {
    if (socket) socket.close() // 退出时断开
    localStorage.removeItem('user')
    router.push('/login')
}

const searchKeyword = ref('')
const handleSearch = () => {
  if (searchKeyword.value) {
    router.push({ path: '/search', query: { q: searchKeyword.value } })
  }
}

const socketUrl = `ws://localhost:8080/ws/${user.userId}`

onMounted(() => { 
    // 只有登录用户才连接 WebSocket
    if (user.userId) {
        initWebSocket()
    }
})

onUnmounted(() => {
  if (socket) {
    socket.close()
  }
})

const initWebSocket = () => {
    if (typeof (WebSocket) === "undefined") {
        console.log("您的浏览器不支持WebSocket")
        return
    }

    socket = new WebSocket(socketUrl)

    socket.onopen = () => {
        console.log("WebSocket连接成功")
    }

    const msgCount = ref(0)
    const msgList = ref([]) // 存历史消息

    socket.onmessage = (msg) => {
        msgCount.value++
        msgList.value.push(msg.data)
        console.log("收到消息:", msg.data)
        // 收到消息后，弹出 ElementPlus 的通知框
        ElNotification({
        title: '系统通知',
        message: msg.data,
        type: 'info',
        duration: 5000 // 5秒后自动关闭
        })
    }

    socket.onclose = () => {
        console.log("WebSocket连接关闭")
    }

    socket.onerror = () => {
        console.log("WebSocket连接发生错误")
    }

    const showMsgBox = () => {
        // 点击铃铛，清空数字，展示消息列表（可以用 Drawer 或 Dialog）
        msgCount.value = 0
        ElMessageBox.alert(msgList.value.join('<br>'), '历史消息', {
            dangerouslyUseHTMLString: true
        })
    }
}
</script>

<template>
    <div>
        <!--头部导航-->
        <el-header style="border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between;">
            <div style="font-size: 20px; font-weight: bold; color: #409EFF">🎬 电影评分系统</div>
            <div style="flex: 1; margin: 0 40px;">
                <el-input 
                    v-model="searchKeyword" 
                    placeholder="搜电影、影人..." 
                    prefix-icon="Search" 
                    style="width: 300px" 
                    @keyup.enter="handleSearch" 
                />
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
                            <el-dropdown-item @click="$router.push('/user-center')"><el-icon><User/></el-icon>个人中心</el-dropdown-item>
                            <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            <div style="margin-right: 20px;">
                <el-badge :value="msgCount" :hidden="msgCount === 0" class="item">
                    <el-button circle icon="Bell" @click="showMsgBox" />
                </el-badge>
            </div>
        </el-header>

        <!--主体内容-->
        <el-main>
            <router-view/>
        </el-main>
    </div>
</template>