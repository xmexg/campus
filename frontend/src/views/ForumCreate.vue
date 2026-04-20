<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Back } from '@element-plus/icons-vue'
import { forumApi } from '../api'

const router = useRouter()
const form = reactive({ title: '', body: '' })
const loading = reactive({ v: false })

async function submit() {
  loading.v = true
  try {
    const id = await forumApi.create(form)
    router.replace('/forum/' + id)
  } finally {
    loading.v = false
  }
}
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>发布帖子</h2>
      <p class="sub">提交后进入待审核，通过后在论坛列表展示；未通过请根据说明修改后再次提交。</p>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16" :xl="14">
        <el-card shadow="never" class="ruoyi-panel-card">
          <template #header>
            <span>帖子内容</span>
          </template>
          <el-form label-width="80px" label-position="right" @submit.prevent="submit">
            <el-form-item label="标题" required>
              <el-input v-model="form.title" placeholder="请输入标题" maxlength="200" show-word-limit />
            </el-form-item>
            <el-form-item label="正文" required>
              <el-input v-model="form.body" type="textarea" :rows="12" placeholder="请输入正文" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" native-type="submit" :loading="loading.v">提交审核</el-button>
              <el-button :icon="Back" @click="router.back()">返回</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
