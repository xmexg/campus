<script setup>
import { onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { forumApi } from '../api'
import { useSession } from '../store/session'

const route = useRoute()
const router = useRouter()
const { isLogin, identityOk } = useSession()
const st = reactive({ post: null, replies: [], text: '', loading: true, error: false })

async function load() {
  const id = route.params.id
  st.loading = true
  st.error = false
  try {
    st.post = await forumApi.detail(id)
    st.replies = await forumApi.replies(id)
  } catch {
    st.error = true
    st.post = null
    st.replies = []
  } finally {
    st.loading = false
  }
}

async function sendReply() {
  if (!st.text.trim()) return
  await forumApi.reply(route.params.id, { body: st.text.trim(), parentId: null })
  st.text = ''
  st.replies = await forumApi.replies(route.params.id)
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar detail-toolbar">
      <el-button :icon="ArrowLeft" @click="router.push('/forum')">返回论坛</el-button>
    </div>

    <el-skeleton v-if="st.loading" :rows="6" animated />

    <el-result v-else-if="st.error" icon="warning" title="加载失败" sub-title="帖子不存在或已被删除">
      <template #extra>
        <el-button type="primary" @click="router.push('/forum')">返回论坛列表</el-button>
      </template>
    </el-result>

    <template v-else-if="st.post">
      <el-card shadow="never" class="ruoyi-panel-card mb16">
        <template #header>
          <div class="post-head">
            <span class="post-title">{{ st.post.title }}</span>
            <div class="post-meta">
              <span>{{ st.post.authorName }}</span>
              <el-divider direction="vertical" />
              <span>浏览 {{ st.post.viewCount }}</span>
            </div>
          </div>
        </template>
        <div class="post-body">{{ st.post.body }}</div>
      </el-card>

      <el-card shadow="never" class="ruoyi-panel-card">
        <template #header>
          <span>回复列表</span>
        </template>
        <template v-if="isLogin && identityOk">
          <el-input v-model="st.text" type="textarea" :rows="3" placeholder="参与讨论" />
          <div class="table-toolbar">
            <el-button type="primary" @click="sendReply">发表回复</el-button>
          </div>
        </template>
        <el-alert v-else-if="isLogin" type="warning" :closable="false" title="身份审核通过后可回复" class="mb16" />
        <el-alert v-else type="info" :closable="false" title="请登录后回复" class="mb16" />
        <el-empty v-if="!st.replies.length" description="暂无回复" />
        <div v-for="r in st.replies" :key="r.id" class="reply-row">
          <div class="reply-head">
            <strong>{{ r.authorName }}</strong>
            <span class="reply-time">{{ r.createdAt }}</span>
          </div>
          <div class="reply-body">{{ r.body }}</div>
        </div>
      </el-card>
    </template>
  </div>
</template>

<style scoped>
.post-head {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.post-meta {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
}

.post-body {
  white-space: pre-wrap;
  line-height: 1.85;
  color: #303133;
  font-size: 14px;
}

.reply-row {
  padding: 14px;
  background: #fafafa;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 12px;
}

.reply-row:last-child {
  margin-bottom: 0;
}

.reply-head {
  margin-bottom: 8px;
}

.reply-time {
  color: #909399;
  font-size: 12px;
  margin-left: 10px;
  font-weight: normal;
}

.reply-body {
  color: #606266;
  line-height: 1.6;
}

.detail-toolbar {
  border-bottom: none;
  padding-bottom: 0;
  margin-bottom: 12px;
}
</style>
