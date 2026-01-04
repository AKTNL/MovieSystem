<script setup>
import { ElMessage } from 'element-plus';
import { defineProps, defineEmits } from 'vue'

// 接收父组件传来的 v-model 值 (即图片URL)
const props = defineProps({
    modelValue: String
})

// 定义事件，用于更新 v-model
const emit = defineEmits(['update:modelValue'])

// 上传成功的回调
const handleSuccess = (response, uploadFile) => {
    if (response.code === 200) {
        // response.data 就是后端返回的 MinIO 图片 URL
        emit('update:modelValue', response.data)
        ElMessage.success('上传成功')
    } else {
        ElMessage.error('上传失败：' + response.msg)
    }
}

//上传前的校验
const beforeUpload = (rawFile) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        ElMessage.error('图片必须是 JPG 或 PNG 格式!')
        return false
    } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('图片大小不能超过2MB！')
        return false
    }
    return true
}
</script>

<template>
    <el-upload
        class="avatar-uploader"
        action="http://localhost:8080/file/upload"
        :show-file-list="false"
        :on-success="handleSuccess"
        :before-upload="beforeUpload"
    >
        <!--如果有图片URL，显示图片；否则显示加号图标-->
        <img v-if="modelValue" :src="modelValue" class="avatar"/>
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
</template>

<style scoped>
.avatar-uploader .avatar{
    width: 178px;
    height: 240px;
    display: block;
    object-fit: cover;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 240px;
  text-align: center;
}
</style>