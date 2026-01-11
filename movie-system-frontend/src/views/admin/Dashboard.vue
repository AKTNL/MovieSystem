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
            // 确保 DOM 加载后初始化
            setTimeout(() => {
                initGenreChart(res.data.genreData)
            }, 0)
        }
    })
}

const initGenreChart = (data) => { 
    const chartDom = document.getElementById('genreChart')
    if (!chartDom) return
    const myChart = echarts.init(chartDom)

    const option = {
        // 【高级感配色】使用高饱和度的荧光色系
        color: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#06b6d4'],
        tooltip: {
            trigger: 'item',
            backgroundColor: '#1e293b',
            borderColor: '#334155',
            textStyle: { color: '#f1f5f9' },
            formatter: '{b}: <b style="color:#3b82f6">{c}</b> ({d}%)' 
        },
        legend: {
            type: 'scroll',
            bottom: '5%',
            left: 'center',
            textStyle: { color: '#94a3b8' },
            pageTextStyle: { color: '#fff' }
        },
        series: [
            {
                name: '电影类型',
                type: 'pie',
                radius: ['50%', '75%'], // 稍微加粗甜甜圈
                center: ['50%', '45%'],
                avoidLabelOverlap: true,
                itemStyle: {
                    borderRadius: 8,
                    borderColor: '#1e293b', // 边框颜色与卡片背景一致，制造镂空感
                    borderWidth: 3
                },
                label: {
                    show: false, // 默认隐藏，emphasis 时显示
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 20,
                        fontWeight: 'bold',
                        color: '#f1f5f9',
                        formatter: '{b}\n{d}%'
                    }
                },
                data: data
            }
        ]
    }
    myChart.setOption(option)
    window.addEventListener('resize', () => myChart.resize())
}
</script>

<template>
    <div class="dashboard-container">
        <el-row :gutter="25" class="stat-row">
            <el-col :span="8">
                <div class="stat-glass-card blue">
                    <div class="icon-box"><el-icon><Film /></el-icon></div>
                    <div class="stat-info">
                        <div class="label">电影库总数</div>
                        <div class="value">{{ stats.movieCount.toLocaleString() }}</div>
                    </div>
                    <div class="decoration"></div>
                </div> 
            </el-col>
            <el-col :span="8"> 
                <div class="stat-glass-card green">
                    <div class="icon-box"><el-icon><User /></el-icon></div>
                    <div class="stat-info">
                        <div class="label">活跃用户</div>
                        <div class="value">{{ stats.userCount.toLocaleString() }}</div>
                    </div>
                    <div class="decoration"></div>
                </div>
            </el-col> 
            <el-col :span="8"> 
                <div class="stat-glass-card orange">
                    <div class="icon-box"><el-icon><ChatDotSquare /></el-icon></div>
                    <div class="stat-info">
                        <div class="label">影评累计</div>
                        <div class="value">{{ stats.reviewCount.toLocaleString() }}</div>
                    </div>
                    <div class="decoration"></div>
                </div>
            </el-col>
        </el-row>

        <el-row :gutter="25"> 
            <el-col :span="14">
                <div class="chart-card">
                    <div class="card-header">
                        <span class="title">电影类型分布</span>
                        <span class="subtitle">实时数据分析</span>
                    </div>
                    <div id="genreChart" class="echarts-box"></div>
                </div>
            </el-col>
            <el-col :span="10">
                <div class="info-card">
                    <div class="card-header">
                        <span class="title">管理终端说明</span>
                    </div>
                    <div class="system-tips">
                        <div class="tip-item">
                            <div class="dot blue"></div>
                            <p><strong>数据同步：</strong> 所有统计每 5 分钟自动更新一次。</p>
                        </div>
                        <div class="tip-item">
                            <div class="dot green"></div>
                            <p><strong>安全审计：</strong> 所有操作将被记录在管理员日志中。</p>
                        </div>
                        <div class="tip-item">
                            <div class="dot orange"></div>
                            <p><strong>内容风控：</strong> 评论系统已接入非法词库自动拦截。</p>
                        </div>
                        <div class="welcome-text">
                            欢迎回来，管理员。系统运行状态：<span class="status">良好</span>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
.dashboard-container {
    padding: 10px 0;
}

/* --- 顶部玻璃态卡片 --- */
.stat-glass-card {
    position: relative;
    height: 120px;
    background: #1e293b;
    border-radius: 20px;
    display: flex;
    align-items: center;
    padding: 0 30px;
    overflow: hidden;
    border: 1px solid rgba(255, 255, 255, 0.05);
    transition: all 0.3s;
}

.stat-glass-card:hover {
    transform: translateY(-5px);
    border-color: rgba(255, 255, 255, 0.1);
}

.icon-box {
    width: 56px;
    height: 56px;
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    margin-right: 20px;
    z-index: 2;
}

.stat-info { z-index: 2; }
.stat-info .label { color: #64748b; font-size: 14px; margin-bottom: 5px; }
.stat-info .value { color: #f1f5f9; font-size: 28px; font-weight: 800; font-family: 'Arial'; }

/* 三种颜色风格 */
.blue .icon-box { background: rgba(59, 130, 246, 0.1); color: #3b82f6; }
.green .icon-box { background: rgba(16, 185, 129, 0.1); color: #10b981; }
.orange .icon-box { background: rgba(245, 158, 11, 0.1); color: #f59e0b; }

.decoration {
    position: absolute;
    right: -20px;
    bottom: -20px;
    width: 100px;
    height: 100px;
    background: currentColor;
    opacity: 0.03;
    border-radius: 50%;
}

/* --- 图表卡片美化 --- */
.chart-card, .info-card {
    background: #1e293b;
    border-radius: 20px;
    padding: 30px;
    height: 500px;
    border: 1px solid rgba(255, 255, 255, 0.05);
}

.card-header {
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
}

.card-header .title { color: #f1f5f9; font-size: 18px; font-weight: 600; }
.card-header .subtitle { color: #64748b; font-size: 12px; margin-top: 4px; }

.echarts-box {
    width: 100%;
    height: 380px;
}

/* --- 系统说明区美化 --- */
.system-tips {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.tip-item {
    display: flex;
    align-items: flex-start;
    gap: 15px;
    background: rgba(15, 23, 42, 0.4);
    padding: 15px;
    border-radius: 12px;
}

.tip-item p { margin: 0; font-size: 13px; color: #94a3b8; line-height: 1.6; }
.tip-item strong { color: #f1f5f9; display: block; margin-bottom: 2px; }

.dot { width: 8px; height: 8px; border-radius: 50%; margin-top: 6px; flex-shrink: 0; }
.dot.blue { background: #3b82f6; box-shadow: 0 0 10px #3b82f6; }
.dot.green { background: #10b981; box-shadow: 0 0 10px #10b981; }
.dot.orange { background: #f59e0b; box-shadow: 0 0 10px #f59e0b; }

.welcome-text {
    margin-top: 20px;
    text-align: center;
    color: #64748b;
    font-size: 14px;
    padding-top: 20px;
    border-top: 1px solid rgba(255,255,255,0.05);
}

.welcome-text .status {
    color: #10b981;
    font-weight: bold;
}
</style>