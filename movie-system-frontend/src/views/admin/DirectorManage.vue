<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, User, Female, Male, VideoCamera } from '@element-plus/icons-vue'
import ImageUpload from '../../components/ImageUpload.vue'
import { getAllDirectors, addDirector, updateDirector, deleteDirector } from '../../api/director'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({})

onMounted(() => {
  load()
})

const load = () => {
  loading.value = true
  getAllDirectors().then(res => {
    if (res.code === 200) {
      tableData.value = res.data
    }
    loading.value = false
  })
}

const handleAdd = () => {
  Object.keys(form).forEach(key => delete form[key])
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const save = () => {
  const apiMethod = form.directorId ? updateDirector : addDirector
  apiMethod(form).then(res => {
    if (res.code === 200) {
      ElMessage.success('导演档案同步成功')
      dialogVisible.value = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要从数据库注销这位导演吗？', '系统警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
    customClass: 'dark-message-box'
  }).then(() => {
    deleteDirector(id).then(res => {
      if (res.code === 200) {
        ElMessage.success('数据已成功移除')
        load()
      }
    })
  })
}
</script>

<template>
  <div class="management-page">
    <div class="action-bar-glass">
      <div class="bar-left">
        <el-button type="primary" class="glow-btn" :icon="Plus" @click="handleAdd">
          新增导演成员
        </el-button>
      </div>
      <div class="bar-right">
        <span class="stat-text">在库导演总数: {{ tableData.length }}</span>
      </div>
    </div>

    <div class="table-glass-wrapper">
      <el-table 
        :data="tableData" 
        v-loading="loading"
        row-class-name="dark-table-row"
        style="width: 100%"
      >
        <el-table-column prop="directorId" label="ID" width="70" align="center" />
        
        <el-table-column label="导演头像" width="120" align="center">
          <template #default="scope">
            <div class="avatar-box">
              <el-avatar 
                :size="60" 
                :src="scope.row.avatarUrl" 
                class="custom-avatar"
              >
                <el-icon><User /></el-icon>
              </el-avatar>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="姓名" min-width="120">
          <template #default="scope">
            <span class="name-highlight">{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column label="性别" width="100" align="center">
          <template #default="scope">
            <div class="gender-tag" :class="scope.row.gender === 'M' ? 'male' : 'female'">
              <el-icon v-if="scope.row.gender === 'M'"><Male /></el-icon>
              <el-icon v-else><Female /></el-icon>
              <span>{{ scope.row.gender === 'M' ? '男' : '女' }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="nationality" label="国籍 / 地区" align="center">
          <template #default="scope">
            <el-tag effect="plain" class="nation-tag">{{ scope.row.nationality || '未知' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="管理" width="150" fixed="right" align="center">
          <template #default="scope">
            <div class="action-btns">
              <el-button class="edit-btn" :icon="Edit" circle @click="handleEdit(scope.row)" />
              <el-button class="del-btn" :icon="Delete" circle @click="handleDelete(scope.row.directorId)" />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog 
      v-model="dialogVisible" 
      :title="form.directorId ? '导演资料更新' : '新导演入驻'" 
      width="450px"
      class="dark-dialog"
    >
      <el-form :model="form" label-position="top" class="dark-form">
        <el-form-item label="头像上传">
          <div class="avatar-upload-container">
            <ImageUpload v-model="form.avatarUrl" />
          </div>
        </el-form-item>
        
        <el-form-item label="导演姓名">
          <el-input v-model="form.name" placeholder="请输入导演姓名" />
        </el-form-item>

        <el-form-item label="性别设定">
          <el-radio-group v-model="form.gender" class="dark-radio-group">
            <el-radio-button label="M">
              <el-icon><Male /></el-icon> 男性
            </el-radio-button>
            <el-radio-button label="F">
              <el-icon><Female /></el-icon> 女性
            </el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="国籍/籍贯">
          <el-input v-model="form.nationality" placeholder="例如：中国" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer-glass">
          <el-button class="cancel-btn" @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" class="glow-btn" @click="save">确认保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 保持与演员管理页面完全一致的 CSS */
.management-page {
  padding: 10px;
  background: #0f172a;
  min-height: 80vh;
}

.action-bar-glass {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 25px;
  background: rgba(30, 41, 59, 0.7);
  backdrop-filter: blur(12px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  margin-bottom: 25px;
}

.stat-text { color: #94a3b8; font-size: 14px; }

.glow-btn {
  background: linear-gradient(135deg, #3b82f6, #6366f1) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
  transition: all 0.3s;
}
.glow-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.6);
}

.table-glass-wrapper {
  background: rgba(30, 41, 59, 0.5);
  border-radius: 20px;
  padding: 15px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

:deep(.el-table) {
  background: transparent !important;
  color: #cbd5e1 !important;
}
:deep(.el-table tr) { background: transparent !important; }
:deep(.el-table th.el-table__cell) {
  background: rgba(15, 23, 42, 0.5) !important;
  color: #94a3b8;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}

/* 统一 hover 高亮效果 */
:deep(.el-table__row.dark-table-row:hover > td) {
    background-color: rgba(255, 255, 255, 0.05) !important;
    transition: background-color 0.2s ease;
}

.avatar-box {
  display: flex;
  justify-content: center;
}
.custom-avatar {
  border: 2px solid rgba(59, 130, 246, 0.3);
  transition: all 0.3s;
}
:deep(.dark-table-row:hover .custom-avatar) {
  transform: scale(1.1);
  border-color: #3b82f6;
}

.gender-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 10px;
  border-radius: 6px;
  font-size: 12px;
}
.male { background: rgba(59, 130, 246, 0.15); color: #60a5fa; }
.female { background: rgba(236, 72, 153, 0.15); color: #f472b6; }

.name-highlight { color: #f8fafc; font-weight: 600; }
.nation-tag { border-color: rgba(148, 163, 184, 0.3) !important; color: #94a3b8 !important; background: transparent; }

.action-btns { display: flex; gap: 10px; justify-content: center; }
.edit-btn { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); color: #cbd5e1; }
.edit-btn:hover { background: #3b82f6; color: white; }
.del-btn { background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.2); color: #ef4444; }
.del-btn:hover { background: #ef4444; color: white; }

/* 弹窗样式 */
:deep(.dark-dialog) { background: #1e293b !important; border: 1px solid rgba(255,255,255,0.1); }
:deep(.el-dialog__title) { color: #f1f5f9; font-weight: bold; }
:deep(.el-form-item__label) { color: #94a3b8 !important; }

:deep(.el-input__wrapper) { background: #0f172a !important; box-shadow: none !important; border: 1px solid #334155 !important; }
:deep(.el-input__inner) { color: #f1f5f9 !important; }

:deep(.el-radio-button__inner) {
  background: #0f172a !important;
  color: #64748b !important;
  border: 1px solid #334155 !important;
}
:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: #3b82f6 !important;
  color: white !important;
  border-color: #3b82f6 !important;
}

.dialog-footer-glass { padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.05); }
.cancel-btn { background: transparent; border: 1px solid #334155; color: #94a3b8; }
</style>