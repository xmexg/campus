<script setup>
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import * as echarts from 'echarts'
import { Refresh } from '@element-plus/icons-vue'
import { statsApi } from '../api'

const st = reactive({
  loading: false,
  updatedAt: '',
  user: null,
  content: null,
  forum: null,
  quiz: null,
})

const userRoleRef = ref(null)
const userTrendRef = ref(null)
const contentTypeRef = ref(null)
const contentCategoryRef = ref(null)
const forumTrendRef = ref(null)
const quizScoreRef = ref(null)

const charts = {
  userRole: null,
  userTrend: null,
  contentType: null,
  contentCategory: null,
  forumTrend: null,
  quizScore: null,
}

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

function ensureChart(name, dom) {
  if (!dom) return null
  if (!charts[name]) {
    charts[name] = echarts.init(dom)
  }
  return charts[name]
}

function renderUserRole() {
  const chart = ensureChart('userRole', userRoleRef.value)
  if (!chart || !st.user) return
  const data = (st.user.roleDistribution || []).map((i) => ({
    name: i.name,
    value: Number(i.value || 0),
  }))

  chart.setOption({
    color: ['#5B8FF9', '#61DDAA', '#F6BD16'],
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '45%'],
        label: { formatter: '{b}: {d}%' },
        data,
      },
    ],
  })
}

function renderUserTrend() {
  const chart = ensureChart('userTrend', userTrendRef.value)
  if (!chart || !st.user) return
  const rows = st.user.recent7DayNewUsers || []

  chart.setOption({
    color: ['#36CFC9'],
    tooltip: { trigger: 'axis' },
    grid: { left: 30, right: 20, top: 24, bottom: 30, containLabel: true },
    xAxis: {
      type: 'category',
      data: rows.map((i) => i.dateLabel),
      boundaryGap: false,
    },
    yAxis: { type: 'value' },
    series: [
      {
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.2 },
        data: rows.map((i) => Number(i.totalCount || 0)),
      },
    ],
  })
}

function renderContentType() {
  const chart = ensureChart('contentType', contentTypeRef.value)
  if (!chart || !st.content) return
  const rows = st.content.typeStats || []

  chart.setOption({
    color: ['#FF9D4D'],
    tooltip: {
      trigger: 'axis',
      formatter(params) {
        const p = params && params[0]
        if (!p) return ''
        const row = rows[p.dataIndex] || {}
        return `${row.contentType || '-'}<br/>阅读量: ${row.totalViews || 0}<br/>内容数: ${row.contentCount || 0}`
      },
    },
    grid: { left: 30, right: 20, top: 24, bottom: 30, containLabel: true },
    xAxis: { type: 'category', data: rows.map((i) => i.contentType || '未知') },
    yAxis: { type: 'value' },
    series: [
      {
        type: 'bar',
        barWidth: 36,
        itemStyle: { borderRadius: [6, 6, 0, 0] },
        data: rows.map((i) => Number(i.totalViews || 0)),
      },
    ],
  })
}

function renderContentCategory() {
  const chart = ensureChart('contentCategory', contentCategoryRef.value)
  if (!chart || !st.content) return
  const data = (st.content.categoryViewStats || []).map((i) => ({
    name: i.category || '未分类',
    value: Number(i.totalViews || 0),
  }))

  chart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: '64%',
        center: ['50%', '45%'],
        label: { formatter: '{b}: {d}%' },
        data,
      },
    ],
  })
}

function renderForumTrend() {
  const chart = ensureChart('forumTrend', forumTrendRef.value)
  if (!chart || !st.forum) return
  const rows = st.forum.interactionTrend || []

  chart.setOption({
    color: ['#F16C7A'],
    tooltip: { trigger: 'axis' },
    grid: { left: 30, right: 20, top: 24, bottom: 30, containLabel: true },
    xAxis: {
      type: 'category',
      data: rows.map((i) => i.dateLabel),
      boundaryGap: false,
    },
    yAxis: { type: 'value' },
    series: [
      {
        type: 'line',
        smooth: true,
        symbolSize: 8,
        areaStyle: { opacity: 0.18 },
        data: rows.map((i) => Number(i.totalCount || 0)),
      },
    ],
  })
}

function renderQuizScore() {
  const chart = ensureChart('quizScore', quizScoreRef.value)
  if (!chart || !st.quiz) return
  const rows = st.quiz.scoreRangeStats || []

  chart.setOption({
    color: ['#9270CA'],
    tooltip: { trigger: 'axis' },
    grid: { left: 30, right: 20, top: 24, bottom: 30, containLabel: true },
    xAxis: { type: 'category', data: rows.map((i) => i.scoreRange) },
    yAxis: { type: 'value' },
    series: [
      {
        type: 'bar',
        barWidth: 34,
        itemStyle: { borderRadius: [6, 6, 0, 0] },
        data: rows.map((i) => Number(i.totalCount || 0)),
        label: { show: true, position: 'top' },
      },
    ],
  })
}

function renderAllCharts() {
  renderUserRole()
  renderUserTrend()
  renderContentType()
  renderContentCategory()
  renderForumTrend()
  renderQuizScore()
}

function resizeAllCharts() {
  Object.keys(charts).forEach((key) => {
    if (charts[key]) charts[key].resize()
  })
}

async function refreshData() {
  st.loading = true
  try {
    const [user, content, forum, quiz] = await Promise.all([
      statsApi.userStat(),
      statsApi.contentStat(),
      statsApi.forumStat(),
      statsApi.quizStat(),
    ])
    st.user = user
    st.content = content
    st.forum = forum
    st.quiz = quiz
    st.updatedAt = formatNow()
    await nextTick()
    renderAllCharts()
  } finally {
    st.loading = false
  }
}

onMounted(async () => {
  await refreshData()
  window.addEventListener('resize', resizeAllCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeAllCharts)
  Object.keys(charts).forEach((key) => {
    if (charts[key]) {
      charts[key].dispose()
      charts[key] = null
    }
  })
})
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar page-title-flex">
      <div>
        <h2>数据统计</h2>
        <p class="sub">用户、内容、论坛、竞赛四大模块实时统计分析。</p>
      </div>
      <div class="page-actions">
        <span class="updated-at">更新时间：{{ st.updatedAt || '-' }}</span>
        <el-button type="primary" :icon="Refresh" :loading="st.loading" @click="refreshData">刷新数据</el-button>
      </div>
    </div>

    <el-row :gutter="16" class="mb16">
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="kpi-card kpi-blue">
          <div class="kpi-title">总用户数</div>
          <div class="kpi-value">{{ st.user?.totalUsers ?? 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="kpi-card kpi-green">
          <div class="kpi-title">内容总数</div>
          <div class="kpi-value">{{ st.content?.totalContents ?? 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="kpi-card kpi-red">
          <div class="kpi-title">论坛总帖子</div>
          <div class="kpi-value">{{ st.forum?.totalPosts ?? 0 }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="kpi-card kpi-purple">
          <div class="kpi-title">竞赛答题次数</div>
          <div class="kpi-value">{{ st.quiz?.totalAttempts ?? 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>用户身份分布（饼图）</span>
          </template>
          <div ref="userRoleRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>近 7 日新增用户（折线图）</span>
          </template>
          <div ref="userTrendRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>各类内容阅读量（柱状图）</span>
          </template>
          <div ref="contentTypeRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>分类阅读量占比（饼图）</span>
          </template>
          <div ref="contentCategoryRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>论坛互动趋势（折线图）</span>
          </template>
          <div ref="forumTrendRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card chart-card">
          <template #header>
            <span>分数段答题人数（柱状图）</span>
          </template>
          <div ref="quizScoreRef" class="chart-box"></div>
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

.kpi-card {
  border: none;
}

.kpi-card :deep(.el-card__body) {
  padding: 14px 16px;
}

.kpi-title {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
}

.kpi-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  line-height: 1;
}

.kpi-blue {
  background: linear-gradient(135deg, #4d83ff, #3a5ccc);
}

.kpi-green {
  background: linear-gradient(135deg, #33c39f, #169a7b);
}

.kpi-red {
  background: linear-gradient(135deg, #ff7a8a, #e24e61);
}

.kpi-purple {
  background: linear-gradient(135deg, #9b7cf7, #6a56d1);
}

.chart-card :deep(.el-card__body) {
  padding: 10px 12px 16px;
}

.chart-box {
  width: 100%;
  height: 320px;
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
    height: 280px;
  }
}
</style>
