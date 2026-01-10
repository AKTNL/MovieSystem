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
    // 【优化1】自定义配色：清新风格
    color: [
      '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', 
      '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'
    ],
    title: {
      text: '类型分布',
      left: 'center',
      top: 'center',
      textStyle: { color: '#999', fontSize: 14 }
    },
    tooltip: {
      trigger: 'item',
      // 【优化2】显示百分比：剧情: 10 (25%)
      formatter: '{b}: {c} ({d}%)' 
    },
    legend: {
      type: 'scroll', // 【优化3】如果类型还是很多，允许图例滚动
      bottom: '0%',
      left: 'center'
    },
    series: [
      {
        name: '电影类型',
        type: 'pie',
        radius: ['40%', '70%'], // 甜甜圈
        avoidLabelOverlap: true, // 防止标签重叠
        itemStyle: {
          borderRadius: 10, // 圆角扇形
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}' // 默认只显示名字，鼠标放上去显示详情
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        data: data
      }
    ]
  }
    myChart.setOption(option)
    // 跟随窗口大小自动缩放
    window.addEventListener('resize', () => {
    myChart.resize()
  })
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