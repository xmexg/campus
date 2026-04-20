<script setup>
import { reactive, onMounted } from 'vue'
import { RefreshRight } from '@element-plus/icons-vue'
import { quizApi } from '../api'

const st = reactive({
  questions: [],
  answers: {},
  ranking: [],
  submitted: null,
  loading: false,
  rankLoading: false,
})

async function loadPaper() {
  st.loading = true
  try {
    st.questions = await quizApi.questions(10)
    st.answers = {}
    st.submitted = null
  } finally {
    st.loading = false
  }
}

async function loadRank() {
  st.rankLoading = true
  try {
    st.ranking = await quizApi.ranking(20)
  } finally {
    st.rankLoading = false
  }
}

async function submit() {
  st.loading = true
  try {
    const payload = { answers: st.answers, durationSeconds: null }
    st.submitted = await quizApi.submit(payload)
    await loadRank()
  } finally {
    st.loading = false
  }
}

onMounted(() => {
  loadRank()
  loadPaper()
})
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>知识竞赛</h2>
      <p class="sub">随机抽取选择题作答，提交后自动计分；右侧实时展示成绩排名（需身份审核通过）。</p>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16">
        <el-card v-loading="st.loading" shadow="never" class="ruoyi-panel-card">
          <template #header>
            <div style="display: flex; align-items: center; justify-content: space-between; width: 100%">
              <span>在线答题</span>
              <el-button :icon="RefreshRight" size="small" @click="loadPaper">换一批题目</el-button>
            </div>
          </template>

          <el-alert v-if="st.submitted" type="success" :closable="false" class="mb16" show-icon>
            本次得分：<strong>{{ st.submitted.score }}</strong> 分（答对 {{ st.submitted.correctCount }} / {{ st.submitted.totalQuestions }} 题）
          </el-alert>

          <div v-for="(q, idx) in st.questions" :key="q.id" class="quiz-item">
            <div class="quiz-q">
              <span class="quiz-no">{{ idx + 1 }}.</span>
              {{ q.questionText }}
            </div>
            <el-radio-group v-model="st.answers[q.id]" class="quiz-options">
              <el-radio label="A">{{ q.optionA }}</el-radio>
              <el-radio label="B">{{ q.optionB }}</el-radio>
              <el-radio label="C">{{ q.optionC }}</el-radio>
              <el-radio label="D">{{ q.optionD }}</el-radio>
            </el-radio-group>
          </div>

          <el-empty v-if="!st.questions.length && !st.loading" description="暂无题目，请联系教师维护题库" />

          <div class="table-toolbar" style="margin-top: 8px; margin-bottom: 0">
            <el-button type="primary" :loading="st.loading" :disabled="!st.questions.length" @click="submit">提交答卷</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="ruoyi-table-card">
          <template #header>
            <span class="card-header-title">成绩排名</span>
          </template>
          <el-table
            v-loading="st.rankLoading"
            :data="st.ranking"
            stripe
            border
            size="small"
            max-height="520"
            style="width: 100%"
          >
            <el-table-column type="index" label="排名" width="60" align="center" />
            <el-table-column prop="nickname" label="用户" min-width="90" show-overflow-tooltip />
            <el-table-column prop="bestScore" label="最高分" width="80" align="center" />
          </el-table>
          <el-empty v-if="!st.rankLoading && !st.ranking.length" description="暂无排行数据" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.quiz-item {
  padding: 16px 0;
  border-bottom: 1px solid #ebeef5;
}

.quiz-item:last-of-type {
  border-bottom: none;
}

.quiz-q {
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  line-height: 1.5;
}

.quiz-no {
  color: #409eff;
  margin-right: 6px;
}

.quiz-options {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}
</style>
