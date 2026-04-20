<script setup>
import { onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Back } from '@element-plus/icons-vue'
import { forumApi } from '../api'

const route = useRoute()
const router = useRouter()
const form = reactive({ title: '', body: '' })
const loading = reactive({ v: false })

onMounted(async () => {
  try {
    const id = route.params.id
    const p = await forumApi.myPostDetail(id)
    form.title = p.title
    form.body = p.body
  } catch {
    router.replace('/forum/mine')
  }
})

async function submit() {
  loading.v = true
  try {
    await forumApi.update(route.params.id, form)
    router.push('/forum/' + route.params.id)
  } finally {
    loading.v = false
  }
}
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>修改帖子</h2>
      <p class="sub">保存后重新进入待审核状态，通过前不会在论坛公开展示。</p>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16" :xl="14">
        <el-card shadow="never" class="ruoyi-panel-card">
          <template #header>
            <span>编辑内容</span>
          </template>
          <el-form label-width="80px" label-position="right" @submit.prevent="submit">
            <el-form-item label="标题" required>
              <el-input v-model="form.title" maxlength="200" show-word-limit />
            </el-form-item>
            <el-form-item label="正文" required>
              <el-input v-model="form.body" type="textarea" :rows="12" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" native-type="submit" :loading="loading.v">保存并提交审核</el-button>
              <el-button :icon="Back" @click="router.back()">返回</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
