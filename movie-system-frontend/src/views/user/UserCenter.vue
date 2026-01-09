<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { userUpdate, getUserInfo, updatePassword } from '../../api/user';
import { useRouter } from 'vue-router';
import ImageUpload from '../../components/ImageUpload.vue';

const user = JSON.parse(localStorage.getItem('user') || '{}');
const form = reactive({})

const router = useRouter()

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
                    <el-button type="danger" @click="openPassDialog">修改密码</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-dialog v-model="passDialogVisible" title="修改密码" width="400px">
            <el-form :model="passForm" :rules="passRules" ref="passRef" label-width="80px"> 
                <el-form-item label="旧密码" prop="oldPassword">
                    <el-input v-model="passForm.oldPassword" type="password" placeholder="请输入旧密码" show-password/>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="passForm.newPassword" type="password" placeholder="请输入新密码" show-password/>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="passForm.confirmPassword" type="password" placeholder="请输入确认密码" show-password/>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="passDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitPass">确认修改</el-button>
            </template>
        </el-dialog>
    </div>
</template>