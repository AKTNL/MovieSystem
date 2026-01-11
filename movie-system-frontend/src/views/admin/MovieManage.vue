<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMovieList, deleteMovie, getMovieActors, getMovieDirectors } from '../../api/movie';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete, Search, VideoCamera, StarFilled } from '@element-plus/icons-vue';
import ImageUpload from '../../components/ImageUpload.vue';
import request from '../../utils/request';
import { getAllActors } from '../../api/actor';
import { getAllDirectors } from '../../api/director';

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const searchText = ref('')

const form = reactive({
    movieId: null,
    title: '',
    coverUrl: '',
    releaseYear: new Date().getFullYear(),
    duration: 120,
    genre: '',
    language: '',
    country: '',
    synopsis: '',
    rating: 0,
    directorIds: [],
    actorList: [],
})

const allActors = ref([])
const allDirectors = ref([])

onMounted(() => {
    load()
    loadOptions()
})

const load = () => {
    loading.value = true
    getMovieList().then(res => {
        if (res.code === 200) {
            tableData.value = res.data
        }
        loading.value = false
    })
}

const loadOptions = () => { 
    getAllActors().then(res => allActors.value = res.data)
    getAllDirectors().then(res => allDirectors.value = res.data)
}

const addActorRow = () => {
    if (!form.actorList) form.actorList = []
    form.actorList.push({ actorId: null, roleName: '' })
}

const removeActorRow = (index) => {
    form.actorList.splice(index, 1)
}

const handleAdd = () => {
    Object.assign(form, {
        movieId: null,
        title: '',
        coverUrl: '',
        releaseYear: new Date().getFullYear(),
        duration: 120,
        genre: '',
        language: '',
        country: '',
        synopsis: '',
        rating: 0,
        directorIds: [],
        actorList: [],
    })
    dialogVisible.value = true
}

const handleEdit = (row) => {
    Object.assign(form, row)
    getMovieDirectors(row.movieId).then(res => {
        form.directorIds = res.data.map(item => item.directorId)
    })
    getMovieActors(row.movieId).then(res => {
        form.actorList = res.data.map(item => ({
            actorId: item.actorId,
            roleName: item.roleName
        }))
    })
    dialogVisible.value = true
}

const save = () => {
    const url = form.movieId ? '/movie/update' : '/movie/add'
    const method = form.movieId ? 'put' : 'post'
    request[method](url, form).then(res => {
        if (res.code === 200) {
            ElMessage.success('保存成功')
            dialogVisible.value = false
            load()
        } else {
            ElMessage.error(res.msg)
        }
    })
}

const handleDelete = (id) => {
    ElMessageBox.confirm('确定要从库中永久删除这部电影吗？', '系统警告', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error',
        customClass: 'dark-message-box' // 后面有样式定义
    }).then(() => {
        deleteMovie(id).then(res => {
            if (res.code === 200) {
                ElMessage.success('数据已成功移除')
                load()
            }
        })
    })
}

const getRatingColor = (score) => {
    if (score >= 8.5) return '#fbbf24' // 金色
    if (score >= 7) return '#10b981'   // 绿色
    return '#94a3b8'                  // 灰色
}
</script>

<template>
    <div class="management-page">
        <div class="action-bar-glass">
            <div class="bar-left">
                <el-button type="primary" class="glow-btn" :icon="Plus" @click="handleAdd">
                    录入新电影
                </el-button>
            </div>
            <div class="bar-right">
                <el-input 
                    v-model="searchText" 
                    placeholder="搜索电影名称/导演..." 
                    class="dark-input"
                    :prefix-icon="Search"
                />
                <el-button class="refresh-btn" @click="load" :icon="VideoCamera">同步数据库</el-button>
            </div>
        </div>
        
        <div class="table-glass-wrapper">
            <el-table 
                :data="tableData" 
                v-loading="loading"
                row-class-name="dark-table-row"
                style="width: 100%"
            >
                <el-table-column prop="movieId" label="ID" width="70" align="center" />
                
                <el-table-column label="海报" width="100">
                    <template #default="scope">
                        <div class="movie-poster-box">
                            <el-image :src="scope.row.coverUrl" class="poster-img" fit="cover">
                                <template #error><div class="img-err">NO IMG</div></template>
                            </el-image>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="电影核心详情" min-width="250">
                    <template #default="scope">
                        <div class="info-group">
                            <div class="info-title">{{ scope.row.title }}</div>
                            <div class="info-sub">
                                <span>{{ scope.row.releaseYear }}</span>
                                <i class="dot"></i>
                                <span>{{ scope.row.duration }}min</span>
                                <i class="dot"></i>
                                <el-tag size="small" class="type-tag">{{ scope.row.genre }}</el-tag>
                            </div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="制片信息" width="180">
                    <template #default="scope">
                        <div class="region-info">
                            <div class="region-text">{{ scope.row.country }}</div>
                            <div class="lang-text">{{ scope.row.language }}</div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="评分指数" width="120" align="center">
                    <template #default="scope">
                        <div class="score-badge" :style="{ borderColor: getRatingColor(scope.row.rating) }">
                            <el-icon :style="{ color: getRatingColor(scope.row.rating) }"><StarFilled /></el-icon>
                            <span class="score-num">{{ scope.row.rating }}</span>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="管理" width="140" fixed="right" align="center">
                    <template #default="scope">
                        <div class="action-btns">
                            <el-button class="edit-btn" :icon="Edit" circle @click="handleEdit(scope.row)" />
                            <el-button class="del-btn" :icon="Delete" circle @click="handleDelete(scope.row.movieId)" />
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog 
            v-model="dialogVisible" 
            :title="form.movieId ? '电影档案修改' : '新影片入库'" 
            width="750px"
            class="dark-dialog"
            destroy-on-close
        >
            <el-form :model="form" label-position="top" class="dark-form">
                <el-row :gutter="30">
                    <el-col :span="9">
                        <el-form-item label="封面海报">
                            <div class="upload-area">
                                <ImageUpload v-model="form.coverUrl" />
                            </div>
                        </el-form-item>
                        
                    </el-col>

                    <el-col :span="15">
                        <el-form-item label="片名">
                            <el-input v-model="form.title" placeholder="请输入完整电影名称" />
                        </el-form-item>
                        
                        <el-row :gutter="15">
                            <el-col :span="12">
                                <el-form-item label="上映年份">
                                    <el-input-number v-model="form.releaseYear" :min="1895" style="width: 100%"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="时长 (分钟)">
                                    <el-input-number v-model="form.duration" style="width: 100%"/>
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row :gutter="15">
                            <el-col :span="12">
                                <el-form-item label="类型标签">
                                    <el-input v-model="form.genre" placeholder="如：科幻 / 动作" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="对白语言">
                                    <el-input v-model="form.language" placeholder="主要使用语言" />
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-form-item label="导演团队">
                    <el-select v-model="form.directorIds" multiple filterable class="dark-select" style="width: 100%;">
                        <el-option v-for="item in allDirectors" :key="item.directorId" :label="item.name" :value="item.directorId" />
                    </el-select>
                </el-form-item>

                <el-form-item label="主创演职员">
                    <div class="actor-card-list">
                        <el-button type="primary" link :icon="Plus" @click="addActorRow" style="margin-bottom: 10px;">添加演员槽位</el-button>
                        <div class="actor-scroll-box">
                            <div v-for="(row, index) in form.actorList" :key="index" class="actor-entry">
                                <el-select v-model="row.actorId" placeholder="选择演员" filterable>
                                    <el-option v-for="item in allActors" :key="item.actorId" :label="item.name" :value="item.actorId" />
                                </el-select>
                                <el-input v-model="row.roleName" placeholder="饰演角色" />
                                <el-button class="del-row-btn" :icon="Delete" circle @click="removeActorRow(index)" />
                            </div>
                        </div>
                    </div>
                </el-form-item>

                <el-form-item label="剧情梗概">
                    <el-input v-model="form.synopsis" type="textarea" :rows="4" />
                </el-form-item>
            </el-form>

            <template #footer>
                <div class="dialog-footer-glass">
                    <el-button class="cancel-btn" @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" class="glow-btn" @click="save">确认入库</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
/* 基础背景与布局 */
.management-page {
    padding: 10px;
    background: #0f172a; /* 更深邃的背景 */
    min-height: 80vh;
}

/* 顶部操作栏玻璃化 */
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
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

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

/* 表格全局样式覆盖 */
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
:deep(.el-table__row td) {
    border-bottom: 1px solid rgba(255,255,255,0.03);
}

/* 电影列表细节 */
.movie-poster-box {
    width: 60px;
    height: 85px;
    border-radius: 6px;
    overflow: hidden;
    border: 1px solid rgba(255, 255, 255, 0.1);
}
.info-title { color: #f8fafc; font-weight: bold; font-size: 16px; margin-bottom: 8px; }
.info-sub { display: flex; align-items: center; gap: 8px; font-size: 13px; color: #64748b; }
.dot { width: 3px; height: 3px; background: #334155; border-radius: 50%; }
.type-tag { background: rgba(59, 130, 246, 0.15) !important; color: #60a5fa !important; border: none; }

.score-badge {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 4px 12px;
    border: 1px solid;
    border-radius: 20px;
    background: rgba(0,0,0,0.2);
}
.score-num { font-size: 16px; font-weight: 800; font-family: 'Arial'; }

/* 操作按钮 */
.action-btns { display: flex; gap: 10px; justify-content: center; }
.edit-btn { background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); color: #cbd5e1; }
.edit-btn:hover { background: #3b82f6; color: white; }
.del-btn { background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.2); color: #ef4444; }
.del-btn:hover { background: #ef4444; color: white; }

/* 弹窗深度暗化 */
:deep(.dark-dialog) {
    background: #1e293b !important;
    border: 1px solid rgba(255,255,255,0.1);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}
:deep(.el-dialog__header) { margin-right: 0; padding-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.05); }
:deep(.el-dialog__title) { color: #f1f5f9; font-weight: bold; }

/* 表单组件深度定制 */
:deep(.el-form-item__label) { color: #94a3b8 !important; font-weight: 600; padding-bottom: 8px !important; }

:deep(.el-input__wrapper), 
:deep(.el-textarea__inner), 
:deep(.el-input-number__increase), 
:deep(.el-input-number__decrease) {
    background-color: #0f172a !important;
    border: 1px solid #334155 !important;
    box-shadow: none !important;
    color: #f1f5f9 !important;
}

:deep(.el-select .el-input__wrapper) { background-color: #0f172a !important; }

/* 演员表滚动区 */
.actor-scroll-box {
    max-height: 220px;
    overflow-y: auto;
    padding: 15px;
    background: rgba(15, 23, 42, 0.4);
    border-radius: 12px;
}
.actor-entry { display: flex; gap: 10px; margin-bottom: 12px; align-items: center; }
.del-row-btn { color: #64748b; }
.del-row-btn:hover { color: #ef4444; }

.dialog-footer-glass {
    padding-top: 20px;
    border-top: 1px solid rgba(255,255,255,0.05);
}
.cancel-btn { background: transparent; border: 1px solid #334155; color: #94a3b8; }
.cancel-btn:hover { color: white; border-color: #475569; }

/* 全局滚动条美化 */
::-webkit-scrollbar { width: 6px; }
::-webkit-scrollbar-thumb { background: #334155; border-radius: 10px; }
::-webkit-scrollbar-track { background: transparent; }

/* 1. 核心：控制表格行在鼠标悬停时的背景色 */
:deep(.el-table__row.dark-table-row:hover > td) {
    background-color: rgba(255, 255, 255, 0.05) !important; /* 悬停时稍微亮一点 */
    transition: background-color 0.2s ease;
}

/* 2. 可选：如果你想让海报在鼠标滑过整行时也有个放大效果 */
:deep(.dark-table-row:hover .poster-img) {
    transform: scale(1.08);
    transition: transform 0.3s ease;
}

/* 3. 解决 Element Plus 默认的 hover 颜色冲突 */
:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
    background-color: rgba(255, 255, 255, 0.05) !important;
}
</style>