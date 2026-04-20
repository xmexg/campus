<script setup>
import { onMounted, reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { quizApi } from '../api'

const st = reactive({ list: [], dlg: false })
const form = reactive({
  id: null,
  questionText: '',
  optionA: '',
  optionB: '',
  optionC: '',
  optionD: '',
  correctOption: 'A',
})

async function load() {
  st.list = await quizApi.teacherQuestions()
}

function open(row) {
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null,
      questionText: '',
      optionA: '',
      optionB: '',
      optionC: '',
      optionD: '',
      correctOption: 'A',
    })
  }
  st.dlg = true
}

async function save() {
  await quizApi.saveQuestion({ ...form })
  st.dlg = false
  await load()
}

async function del(id) {
  await quizApi.delQuestion(id)
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>题库管理</h2>
      <p class="sub">维护竞赛选择题，支持增删改；学生端将随机抽题作答。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">题目列表</span>
        <el-button type="primary" :icon="Plus" size="small" @click="open(null)">新增题目</el-button>
      </template>
      <el-table :data="st.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="questionText" label="题干" min-width="220" show-overflow-tooltip />
        <el-table-column prop="correctOption" label="答案" width="80" align="center" />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="open(row)">编辑</el-button>
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="st.dlg" title="题目维护" width="560px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="题干">
          <el-input v-model="form.questionText" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="A"><el-input v-model="form.optionA" /></el-form-item>
        <el-form-item label="B"><el-input v-model="form.optionB" /></el-form-item>
        <el-form-item label="C"><el-input v-model="form.optionC" /></el-form-item>
        <el-form-item label="D"><el-input v-model="form.optionD" /></el-form-item>
        <el-form-item label="正确">
          <el-select v-model="form.correctOption" style="width: 120px">
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
            <el-option label="D" value="D" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="st.dlg = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
