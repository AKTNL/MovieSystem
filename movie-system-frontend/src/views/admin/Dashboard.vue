<script setup>
import { ref, onMounted } from 'vue'
import { getStatistics } from '../../api/statistics'
import * as echarts from 'echarts'

const stats = ref({
    movieCount: 0,
    userCount: 0,
    reviewCount: 0,
})

onMounted(() => {
    loadStats()
})

const loadStats = () => {
    getStatistics().then(res => {
        if (res.code === 200) {
            stats.value = res.data
            //数据加载完后初始化图表
            initGenreChart(res.data.genreData)
        }
    })
}

const initGenreChart = (data) => { 
    //获取DOM元素
    const chartDom = document.getElementById('genreChart')
    const myChart = echarts.init(chartDom)

    const option = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
        },
        series: [
            {
                name: '电影类型',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '20',
                        fontWeight: 'bold'
                    }  
                },
                labelLine: {
                    show: false
                },
                data: data
            }
        ]
    }
    myChart.setOption(option)
}
</script>

<template>
    <div>
        <!--数据卡片区域-->
        <el-row :gutter="20" style="margin-bottom: 20px;">
            <el-col :span="8">
                <el-card shadow="hover" style="background: linear-gradient(to right, #4facfe 0%, #00f2fe 100%); color: white;">
                    <div style="font-size: 16px;">🎬 电影总数</div>
                    <div style="font-size: 30px; font-weight: bold; margin-top: 10px;">{{ stats.movieCount }}</div>
                </el-card> 
            </el-col>
            <el-col :span="8"> 
                <el-card shadow="hover" style="background: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); color: white;">
                    <div style="font-size: 16px;">👥 用户总数</div>
                    <div style="font-size: 30px; font-weight: bold; margin-top: 10px;">{{ stats.userCount }}</div>
                </el-card>
            </el-col> 
            <el-col :span="8"> 
                <el-card shadow="hover" style="background: linear-gradient(to right, #fa709a 0%, #fee140 100%); color: white;">
                    <div style="font-size: 16px;">📝 评论总数</div>
                    <div style="font-size: 30px; font-weight: bold; margin-top: 10px;">{{ stats.reviewCount }}</div>
                </el-card>
            </el-col>
        </el-row>

        <!--图表区域-->
        <el-row :gutter="20"> 
            <el-col :span="12">
                <el-card header="电影类型分布">
                    <!-- ECharts 容器，必须给高度 -->
                    <div id="genreChart" style="width: 100%; height: 400px;"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card header="系统说明">
                    <div style="line-height: 2; color: #666;">
                        <p>欢迎进入电影评分系统后台管理。</p>
                        <p>这里展示了系统的核心数据概览。</p>
                        <p>左侧饼图实时反映了当前库中不同类型电影的比例。</p>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>