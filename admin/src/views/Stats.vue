<script setup>
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { Refresh } from '@element-plus/icons-vue'
import { statsApi } from '../api'

const st = reactive({
  overview: null,
  loading: false,
  updatedAt: '',
})

const contentChartRef = ref(null)
let contentChart = null

function formatNow() {
  const d = new Date()
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const h = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  const s = String(d.getSeconds()).padStart(2, '0')
  return `${y}-${m}-${day} ${h}:${min}:${s}`
}

function renderContentChart() {
  if (!contentChartRef.value || !st.overview) return
  if (!contentChart) {
    contentChart = echarts.init(contentChartRef.value)
  }
  const rows = st.overview.contentViews || []
  const xData = rows.map((i) => i.contentType || '未知')
  const yData = rows.map((i) => Number(i.totalViews || 0))

  contentChart.setOption({
    color: ['#5B8FF9'],
    tooltip: { trigger: 'axis' },
    grid: { left: 36, right: 20, top: 30, bottom: 36, containLabel: true },
    xAxis: {
      type: 'category',
      data: xData,
      axisTick: { show: false },
      axisLine: { lineStyle: { color: '#dcdfe6' } },
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: '#eef1f6' } },
    },
    series: [
      {
        type: 'bar',
        barWidth: 36,
        data: yData,
        label: { show: true, position: 'top' },
        itemStyle: {
          borderRadius: [6, 6, 0, 0],
        },
      },
    ],
  })
}

function resizeChart() {
  if (contentChart) contentChart.resize()
}

function formatCount(v) {
  return Number(v || 0).toLocaleString('zh-CN')
}

function formatScore(v) {
  const n = Number(v)
  return Number.isFinite(n) ? n.toFixed(1) : '-'
}

function ratePercent(v) {
  const n = Number(v)
  if (!Number.isFinite(n)) return 0
  return Math.min(100, Math.max(0, Number((n * 100).toFixed(1))))
}

function formatRate(v) {
  return `${ratePercent(v).toFixed(1)}%`
}

function scoreLevel(v) {
  const n = Number(v)
  if (!Number.isFinite(n)) return '暂无数据'
  if (n >= 90) return '优秀'
  if (n >= 80) return '良好'
  if (n >= 60) return '及格'
  return '待提升'
}

function scoreLevelClass(v) {
  const n = Number(v)
  if (!Number.isFinite(n)) return 'level-empty'
  if (n >= 90) return 'level-excellent'
  if (n >= 80) return 'level-good'
  if (n >= 60) return 'level-pass'
  return 'level-low'
}

async function refreshData() {
  st.loading = true
  try {
    st.overview = await statsApi.overview()
    st.updatedAt = formatNow()
    await nextTick()
    renderContentChart()
  } finally {
    st.loading = false
  }
}

onMounted(async () => {
  await refreshData()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  if (contentChart) {
    contentChart.dispose()
    contentChart = null
  }
})
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar page-title-flex">
      <div>
        <h2>首页</h2>
        <p class="sub">按内容类型汇总浏览量，并展示知识竞赛参与与成绩指标。</p>
      </div>
      <div class="page-actions">
        <span class="updated-at">更新时间：{{ st.updatedAt || '-' }}</span>
        <el-button type="primary" :icon="Refresh" :loading="st.loading" @click="refreshData">刷新数据</el-button>
      </div>
    </div>

    <el-row v-if="st.overview" :gutter="16" class="dashboard-row">
      <el-col :xs="24" :lg="14" class="dashboard-col">
        <el-card shadow="never" class="ruoyi-table-card mb16 dashboard-card">
          <template #header>
            <span class="card-header-title">内容浏览量（按类型）</span>
          </template>
          <div ref="contentChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10" class="dashboard-col">
        <el-card shadow="never" class="ruoyi-panel-card mb16 dashboard-card home-quiz-card">
          <template #header>
            <span>竞赛数据概览</span>
          </template>
          <div v-if="st.overview.quiz" class="quiz-overview">
            <div class="quiz-hero">
              <div class="hero-label">平均分</div>
              <div class="hero-score">{{ formatScore(st.overview.quiz.avgScore) }}</div>
              <span class="hero-level" :class="scoreLevelClass(st.overview.quiz.avgScore)">
                {{ scoreLevel(st.overview.quiz.avgScore) }}
              </span>
            </div>

            <div class="quiz-metrics">
              <div class="metric-item">
                <div class="metric-label">总答卷次数</div>
                <div class="metric-value">{{ formatCount(st.overview.quiz.totalAttempts) }}</div>
              </div>
              <div class="metric-item">
                <div class="metric-label">参与人数</div>
                <div class="metric-value">{{ formatCount(st.overview.quiz.distinctUsers) }}</div>
              </div>
            </div>

            <div class="quiz-progress">
              <div class="progress-head">
                <span>平均正确率</span>
                <strong>{{ formatRate(st.overview.quiz.avgCorrectRate) }}</strong>
              </div>
              <el-progress
                :show-text="false"
                :stroke-width="12"
                :percentage="ratePercent(st.overview.quiz.avgCorrectRate)"
                color="linear-gradient(90deg, #36CFC9, #5B8FF9)"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.page-title-flex {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.page-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.updated-at {
  font-size: 12px;
  color: #909399;
}

.dashboard-row {
  align-items: stretch;
}

.dashboard-col {
  display: flex;
}

.dashboard-card {
  width: 100%;
}

.dashboard-card :deep(.el-card__body) {
  height: 360px;
  box-sizing: border-box;
}

.chart-box {
  width: 100%;
  height: 100%;
}

.home-quiz-card :deep(.el-card__body) {
  padding: 16px;
}

.quiz-overview {
  height: 100%;
  display: grid;
  grid-template-rows: auto 1fr auto;
  gap: 14px;
}

.quiz-hero {
  border-radius: 12px;
  background: linear-gradient(135deg, #4f7dff, #2f5bd1);
  color: #fff;
  padding: 14px 16px;
}

.hero-label {
  font-size: 12px;
  opacity: 0.88;
}

.hero-score {
  font-size: 38px;
  line-height: 1;
  font-weight: 700;
  margin: 8px 0 10px;
}

.hero-level {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 2px 10px;
  font-size: 12px;
  background: rgba(255, 255, 255, 0.2);
}

.level-excellent {
  background: rgba(20, 196, 133, 0.22);
}

.level-good {
  background: rgba(255, 192, 82, 0.22);
}

.level-pass {
  background: rgba(39, 155, 255, 0.22);
}

.level-low {
  background: rgba(255, 120, 120, 0.28);
}

.level-empty {
  background: rgba(255, 255, 255, 0.2);
}

.quiz-metrics {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.metric-item {
  background: #f7faff;
  border: 1px solid #e8eefc;
  border-radius: 10px;
  padding: 12px;
}

.metric-label {
  color: #7b8599;
  font-size: 12px;
}

.metric-value {
  margin-top: 6px;
  color: #1f2f56;
  font-size: 22px;
  line-height: 1.1;
  font-weight: 700;
}

.quiz-progress {
  border-radius: 10px;
  border: 1px solid #edf1f7;
  padding: 12px;
  background: #fff;
}

.progress-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 13px;
  color: #6b7280;
}

.progress-head strong {
  color: #0f172a;
}

.quiz-progress :deep(.el-progress-bar__outer) {
  background-color: #edf2ff;
}

@media (max-width: 768px) {
  .page-title-flex {
    flex-direction: column;
  }

  .page-actions {
    width: 100%;
    justify-content: space-between;
  }

  .chart-box {
    min-height: 280px;
  }

  .dashboard-card :deep(.el-card__body) {
    height: auto;
    min-height: 320px;
  }

  .quiz-metrics {
    grid-template-columns: 1fr;
  }
}
</style>
