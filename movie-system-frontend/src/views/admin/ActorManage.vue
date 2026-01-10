<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ImageUpload from '../../components/ImageUpload.vue'
import { getAllActors, addActor,updateActor, deleteActor } from '../../api/actor'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({})

onMounted(() => {
  load()
})

const load = () => {
    getAllActors().then(res => {
        if (res.code === 200) {
            tableData.value = res.data
        }
    })
}

//点击新增
const handleAdd = () => {
    //清空表单
    Object.keys(form).forEach(key => delete form[key])
    dialogVisible.value = true
}

//点解编辑
const handleEdit = (row) => {
    //复制到当前数据到表单
    Object.assign(form, row)
    dialogVisible.value = true
}

//提交保存
const save = () => {
    //根据是否有ID判断是新增还是修改
    console.log('提交的数据：', form)
    const apiMethod = form.actorId ? updateActor : addActor

    apiMethod(form).then(res => {
        if (res.code === 200) {
            ElMessage.success('操作成功')
            dialogVisible.value = false
            load() //刷新列表
        } else {
            ElMessage.error(res.msg)
        }
    })
}

//删除
const handleDelete = (id) => {
    ElMessageBox.confirm('确定要删除这位演员吗？', '提示', {
        type: 'warning'
    }).then(() => {
        deleteActor(id).then(res => {
            if (res.code === 200) {
                ElMessage.success('删除成功')
                load()
            } else {
                ElMessage.error(res.msg)
            }
        })
    })
}
</script>

<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-button type="primary" icon="Plus" @click="handleAdd">新增演员</el-button>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="actorId" label="ID" width="60" />
      <el-table-column label="头像" width="80">
        <template #default="scope">
          <el-avatar shape="square" :size="50" :src="scope.row.avatarUrl" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" />
      <!-- 演员特有字段 -->
      <el-table-column prop="gender" label="性别" width="60">
        <template #default="scope">
          <el-tag :type="scope.row.gender === 'M' ? '' : 'danger'">
            {{ scope.row.gender === 'M' ? '男' : '女' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nationality" label="国籍" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.actorId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.actorId ? '编辑' : '新增'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="头像">
          <ImageUpload v-model="form.avatarUrl" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="M">男</el-radio>
            <el-radio label="F">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="国籍">
          <el-input v-model="form.nationality" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>