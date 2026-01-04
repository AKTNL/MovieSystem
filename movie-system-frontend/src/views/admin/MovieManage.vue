<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMovieList, deleteMovie } from '../../api/movie';
import { ElMessage, ElMessageBox } from 'element-plus';
import ImageUpload from '../../components/ImageUpload.vue';
import request from '../../utils/request';

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({})

onMounted(() => {
    load()
})

const load = () => {
    getMovieList().then(res => {
        if (res.code === 200) {
            tableData.value = res.data
        }
    })
}

//点击新增
const handleAdd = () => {
    //清空表单
    Object.keys(form).forEach(key => delete from[key])
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
    const url = form.movieId ? '/movie/update' : '/movie/add'
    const method = form.movieId ? 'put' : 'post'

    request[method](url, form).then(res => {
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
    ElMessageBox.confirm('确定要删除这部电影吗？', '提示', {
        type: 'warning'
    }).then(() => {
        deleteMovie().then(res => {
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
        <!--顶部操作栏-->
        <div style="margin-bottom: 20px;">
            <el-button type="primary" icon="Plus" @click="handleAdd">新增电影</el-button>
        </div>
        
        <!--数据表格-->
        <el-table :data="tableData" border stripe style="width: 100%">
            <el-table-column prop="movieId" label="ID" width="60" />
            <el-table-column label="封面" width="100">
                <template #default="scope">
                    <el-image :src="scope.row.coverUrl" style="width: 50px; height: 70px" fit="cover"/>
                </template>
            </el-table-column>
            <el-table-column prop="title" label="电影名称"/>
            <el-table-column prop="releaseYear" label="年份" width="80"/>
            <el-table-column prop="genre" label="类型" width="100"/>
            <el-table-column prop="rating" label="评分" width="80" sortable/>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="handleDelete(scope.row.movieId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--新增/编辑 弹窗-->
        <el-dialog v-model="dialogVisible" :title="form.movieId ? '编辑电影' : '新增电影'" width="500px">
            <el-form :model="form" label-width="80px">
                <el-form-item label="电影名称">
                    <el-input v-model="form.title"/>
                </el-form-item>
                <el-form-item label="封面图">
                    <ImageUpload v-model="form.coverUrl"/>
                </el-form-item>
                <el-form-item label="年份">
                    <el-input-number v-model="form.releaseYear" :min="1900" :max="2030"/>
                </el-form-item>
                <el-form-item label="时长（分）">
                    <el-input-number v-model="form.duration"/>
                </el-form-item>
                <el-form-item label="类型">
                    <el-input v-model="form.genre" placeholder="如：剧情，动作"/>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input v-model="form.synopsis" type="textarea"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="save">确定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>