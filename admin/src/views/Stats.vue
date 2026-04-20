<script setup>
import { onMounted, reactive } from 'vue'
import { statsApi } from '../api'

const st = reactive({ overview: null })

onMounted(async () => {
  st.overview = await statsApi.overview()
})
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>首页</h2>
      <p class="sub">按内容类型汇总浏览量，并展示知识竞赛参与与成绩指标。</p>
    </div>

    <el-row :gutter="16" v-if="st.overview">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-table-card">
          <template #header>
            <span class="card-header-title">内容浏览量（按类型）</span>
          </template>
          <template>
            <div ref="chartRef" style="width: 600px; height: 400px;"></div>
          </template>
<!--          <el-table :data="st.overview.contentViews" stripe border style="width: 100%">-->
<!--            <el-table-column type="index" label="#" width="55" align="center" />-->
<!--            <el-table-column prop="contentType" label="类型" align="center" />-->
<!--            <el-table-column prop="totalViews" label="总浏览量" align="center" />-->
<!--          </el-table>-->

        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="ruoyi-panel-card">
          <template #header>
            <span>竞赛数据概览</span>
          </template>
          <el-descriptions v-if="st.overview.quiz" :column="1" border>
            <el-descriptions-item label="总答卷次数">{{ st.overview.quiz.totalAttempts ?? 0 }}</el-descriptions-item>
            <el-descriptions-item label="参与人数">{{ st.overview.quiz.distinctUsers ?? 0 }}</el-descriptions-item>
            <el-descriptions-item label="平均分">{{ st.overview.quiz.avgScore?.toFixed?.(1) ?? '-' }}</el-descriptions-item>
            <el-descriptions-item label="平均正确率">
              {{ st.overview.quiz.avgCorrectRate != null ? (Number(st.overview.quiz.avgCorrectRate) * 100).toFixed(1) + '%' : '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
