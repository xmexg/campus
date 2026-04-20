<script setup>
import { onMounted, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { contentApi } from '../api'
import { useSession } from '../store/session'
import { resolveApiStaticUrl } from '../utils/mediaUrl'

const route = useRoute()
const router = useRouter()
const { isLogin, identityOk } = useSession()
const state = reactive({ item: null, comments: [], text: '', loading: true, error: false })

const videoSrc = computed(() => resolveApiStaticUrl(state.item?.videoUrl))

async function load() {
  const id = route.params.id
  state.loading = true
  state.error = false
  try {
    state.item = await contentApi.detail(id)
    await contentApi.view(id)
    state.comments = await contentApi.comments(id)
  } catch {
    state.error = true
    state.item = null
    state.comments = []
  } finally {
    state.loading = false
  }
}

async function sendComment() {
  if (!state.text.trim()) return
  await contentApi.addComment(route.params.id, state.text.trim())
  state.text = ''
  state.comments = await contentApi.comments(route.params.id)
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar detail-toolbar">
      <el-button :icon="ArrowLeft" @click="router.push('/learn')">返回列表</el-button>
    </div>

    <el-skeleton v-if="state.loading" :rows="8" animated />

    <el-result v-else-if="state.error" icon="warning" title="加载失败" sub-title="内容不存在或网络异常，请返回列表重试">
      <template #extra>
        <el-button type="primary" @click="router.push('/learn')">返回内容学习</el-button>
      </template>
    </el-result>

    <template v-else-if="state.item">
      <el-card shadow="never" class="ruoyi-panel-card mb16">
        <template #header>
          <div class="detail-head">
            <span class="detail-title">{{ state.item.title }}</span>
            <div class="detail-tags">
              <el-tag size="small">{{ { NEWS: '资讯', ARTICLE: '文章', VIDEO: '视频' }[state.item.contentType] || state.item.contentType }}</el-tag>
              <el-tag size="small" type="info">{{ state.item.category }}</el-tag>
              <el-tag size="small" type="success">浏览 {{ state.item.viewCount }}</el-tag>
            </div>
          </div>
        </template>
        <p v-if="state.item.summary" class="detail-summary">{{ state.item.summary }}</p>
        <div v-if="state.item.contentType === 'VIDEO' && state.item.videoUrl" class="video-wrap">
          <video :src="videoSrc" controls class="video-el" />
        </div>
        <div v-else class="article-body">{{ state.item.body }}</div>
      </el-card>

      <el-card shadow="never" class="ruoyi-panel-card">
        <template #header>
          <span>评论</span>
        </template>
        <template v-if="isLogin && identityOk">
          <el-input v-model="state.text" type="textarea" :rows="3" placeholder="发表看法" />
          <div class="table-toolbar">
            <el-button type="primary" @click="sendComment">发送评论</el-button>
          </div>
        </template>
        <el-alert v-else-if="isLogin" type="warning" :closable="false" title="身份审核通过后可评论" class="mb16" />
        <el-alert v-else type="info" :closable="false" title="请登录后评论" class="mb16" />
        <el-empty v-if="!state.comments.length" description="暂无评论" />
        <div v-for="c in state.comments" :key="c.id" class="comment-row">
          <div class="comment-meta">{{ c.nickname }}</div>
          <div class="comment-body">{{ c.body }}</div>
        </div>
      </el-card>
    </template>
  </div>
</template>

<style scoped>
.detail-head {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}

.detail-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.detail-summary {
  margin: 0 0 16px;
  color: #606266;
  font-size: 14px;
  line-height: 1.7;
}

.video-wrap {
  margin-top: 8px;
}

.video-el {
  max-width: 100%;
  border-radius: 4px;
  background: #000;
}

.article-body {
  white-space: pre-wrap;
  line-height: 1.85;
  color: #303133;
  font-size: 14px;
}

.comment-row {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.comment-row:last-child {
  border-bottom: none;
}

.comment-meta {
  font-size: 12px;
  color: #909399;
  margin-bottom: 6px;
}

.comment-body {
  color: #606266;
  line-height: 1.6;
}

.detail-toolbar {
  border-bottom: none;
  padding-bottom: 0;
  margin-bottom: 12px;
}
</style>
