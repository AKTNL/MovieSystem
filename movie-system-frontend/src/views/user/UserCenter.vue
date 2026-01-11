<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { userUpdate, getUserInfo, updatePassword } from '../../api/user';
import { useRouter } from 'vue-router';
import ImageUpload from '../../components/ImageUpload.vue';

const user = JSON.parse(localStorage.getItem('user') || '{}');

const form = reactive({
    username: '',
    nickname: '',
    email: '',
    phone: '',
    avatar: '',
    bio: '' // 【新增】个人简介
})

const router = useRouter()

const stats = reactive({
    reviewCount: 0, // 评论数
    avgScore: 0,    // 平均给分
    watchedCount: 0 // 看过 (暂时可以用评论数代替)
})

onMounted(() => {
    loadInfo()
    loadUserStats()
})

const loadInfo = () => {
    getUserInfo(user.userId).then(res => {
        if (res.code === 200) {
            // 1. 填充表单数据 (nickname, bio, etc.)
            Object.assign(form, res.data)

            // 2. 填充统计数据
            stats.reviewCount = res.data.reviewCount
            stats.avgScore = res.data.avgScore
            stats.watchedCount = res.data.watchedCount
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

const passDialogVisible = ref(false)
const passRef = ref(null)
const passForm = reactive({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
})

const validatePass2 = (rule, value, callback) => { 
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if(value !== passForm.newPassword){
        callback(new Error('两次输入密码不一致!'))
    } else {
        callback()
    }
}

const passRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
}

const openPassDialog = () => {
  // 清空表单
  passForm.oldPassword = ''
  passForm.newPassword = ''
  passForm.confirmPassword = ''
  passDialogVisible.value = true
}

const submitPass = () => {
  passRef.value.validate((valid) => {
    if (valid) {
      updatePassword({
        userId: user.userId,
        oldPassword: passForm.oldPassword,
        newPassword: passForm.newPassword
      }).then(res => {
        if (res.code === 200) {
          passDialogVisible.value = false
          ElMessageBox.alert('密码修改成功，请重新登录', '提示', {
            confirmButtonText: '去登录',
            callback: () => {
              // 清除缓存并跳转登录页
              localStorage.removeItem('user')
              router.push('/login')
            }
          })
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}


const loadUserStats = () => {
    
}
</script>

<template>
  <div class="user-center-container">
    <el-row :gutter="30">
      <el-col :md="8" :sm="10">
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar-wrapper">
              <el-avatar :size="120" :src="form.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" class="main-avatar" />
              <div class="role-badge">{{ form.role === 'admin' ? '管理员' : '影迷' }}</div>
            </div>
            <h2 class="profile-name">{{ form.nickname || '未设置昵称' }}</h2>
            <p class="profile-bio">{{ form.bio || '这个人很懒，什么都没写。' }}</p>
          </div>
          
          <div class="profile-stats">
            <!-- 【修改】绑定动态数据 -->
            <div class="stat-item">
              <!-- 暂时用评论数代表看过，或者你有收藏功能就用收藏数 -->
              <div class="stat-num">{{ stats.reviewCount }}</div>
              <div class="stat-label">看过</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">{{ stats.reviewCount }}</div>
              <div class="stat-label">评论</div>
            </div>
            <div class="stat-item">
              <div class="stat-num">{{ stats.avgScore }}</div>
              <div class="stat-label">平均给分</div>
            </div>
          </div>

          <el-divider />
          
          <div class="profile-menu">
            <div class="menu-item active"><el-icon><User /></el-icon> 基本信息</div>
            <div class="menu-item" @click="openPassDialog"><el-icon><Lock /></el-icon> 安全设置</div>
          </div>
        </div>
      </el-col>

      <el-col :md="16" :sm="14">
        <div class="settings-panel">
          <div class="panel-header">
            <h3 class="panel-title">账号设置</h3>
            <p class="panel-subtitle">更新您的个人资料和联系方式</p>
          </div>

          <el-form :model="form" label-position="top" class="custom-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名 (唯一标识)">
                  <el-input v-model="form.username" disabled class="dark-input" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="昵称">
                  <el-input v-model="form.nickname" placeholder="想个响亮的名字" class="dark-input" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="电子邮箱">
                  <el-input v-model="form.email" placeholder="example@mail.com" class="dark-input" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码">
                  <el-input v-model="form.phone" placeholder="输入11位手机号" class="dark-input" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="个性签名" >
              <el-input 
                v-model="form.bio" 
                type="text" 
                :rows="3" 
                placeholder="介绍一下你自己，比如喜欢的电影类型..." 
                class="dark-input" 
                show-word-limit
              />
            </el-form-item>

            <el-form-item label="修改头像">
              <div class="upload-section">
                <ImageUpload v-model="form.avatar" />
                <span class="upload-tip">建议尺寸 200x200，支持 jpg/png 格式</span>
              </div>
            </el-form-item>

            <el-divider border-style="dashed" />

            <div class="form-actions">
              <el-button type="primary" size="large" @click="save" class="gradient-btn">
                保存所有更改
              </el-button>
            </div>
          </el-form>
        </div>
      </el-col>
    </el-row>

    <el-dialog v-model="passDialogVisible" title="安全验证" width="450px" class="dark-dialog">
      <p class="dialog-desc">为了您的账户安全，修改密码后需要重新登录。</p>
      <el-form :model="passForm" :rules="passRules" ref="passRef" label-position="top">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input v-model="passForm.oldPassword" type="password" show-password class="dark-input" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passForm.newPassword" type="password" show-password class="dark-input" />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="passForm.confirmPassword" type="password" show-password class="dark-input" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passDialogVisible = false" link>取消</el-button>
        <el-button type="primary" @click="submitPass" class="gradient-btn">更新密码</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-center-container {
  padding: 40px 0;
}

/* 左侧头像卡片 */
.profile-card {
  background: #1e293b;
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.05);
  text-align: center;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.main-avatar {
  border: 4px solid #334155;
  box-shadow: 0 10px 25px rgba(0,0,0,0.3);
}

.role-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 20px;
  border: 2px solid #1e293b;
}

.profile-name {
  color: #f1f5f9;
  margin: 10px 0 5px;
}

.profile-bio {
  color: #94a3b8;
  font-size: 14px;
}

/* --- 2. 统计项增加分隔感 --- */
.profile-stats {
  display: flex;
  justify-content: space-around;
  margin-top: 25px;
  padding: 15px 0;
  background: rgba(15, 23, 42, 0.3); /* 加个极淡的底色聚拢数据 */
  border-radius: 12px;
}

.stat-num {
  color: #3b82f6; /* 数字换成蓝色，更有视觉重点 */
  font-size: 20px;
  font-weight: 700;
}

.stat-label {
  color: #64748b;
  font-size: 12px;
}

.profile-menu {
  text-align: left;
  margin-top: 20px;
}

.menu-item {
  padding: 12px 15px;
  border-radius: 10px;
  color: #94a3b8;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;
}

.menu-item:hover {
  background: rgba(255,255,255,0.03);
  color: #f1f5f9;
}

.menu-item.active {
  background: rgba(59, 130, 246, 0.12) !important;
  color: #60a5fa !important;
  /* 加一个左侧的小指示条 */
  border-left: 3px solid #3b82f6;
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
}

/* 右侧面板 */
.settings-panel {
  background: #1e293b;
  border-radius: 20px;
  padding: 40px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.panel-header {
  margin-bottom: 30px;
}

.panel-title {
  color: #f1f5f9;
  font-size: 22px;
  margin: 0;
}

.panel-subtitle {
  color: #64748b;
  font-size: 14px;
  margin: 5px 0 0;
}

/* 表单定制 */
:deep(.el-form-item__label) {
  color: #94a3b8 !important;
  font-weight: 500;
}

.dark-input :deep(.el-input__wrapper) {
  background: #0f172a !important;
  border: 1px solid #334155 !important;
  box-shadow: none !important;
  padding: 8px 15px;
  transition: all 0.3s;
}

.dark-input :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6 !important;
  background: rgba(15, 23, 42, 0.8) !important;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15) !important;
}

.dark-input.readonly :deep(.el-input__wrapper) {
  background: #1e293b !important;
  opacity: 0.6;
}

.dark-input :deep(.el-input__inner) {
  color: #f1f5f9 !important;
}

.upload-section {
  display: flex;
  align-items: center;
  gap: 20px;
}

.upload-tip {
  color: #64748b;
  font-size: 12px;
}

.gradient-btn {
  background: linear-gradient(135deg, #3b82f6, #2563eb) !important;
  border: none !important;
  padding: 12px 35px !important;
  height: auto;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s !important;
  /* 关键：蓝色发光阴影 */
  box-shadow: 0 4px 15px rgba(37, 99, 235, 0.3);
}

.gradient-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(37, 99, 235, 0.5);
  opacity: 0.9;
}

.form-actions {
  margin-top: 20px;
}

/* 弹窗样式 */
.dialog-desc {
  color: #94a3b8;
  margin-bottom: 20px;
}

:deep(.el-dialog) {
  background: #1e293b !important;
  border-radius: 16px;
}

:deep(.el-dialog__title) {
  color: #f1f5f9 !important;
}

.stat-item {
  position: relative;
  flex: 1;
}

/* 给中间的项加左右边框 */
.stat-item:not(:last-child)::after {
  content: "";
  position: absolute;
  right: 0;
  top: 20%;
  height: 60%;
  width: 1px;
  background: rgba(255, 255, 255, 0.05);
}
</style>