<script setup>
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Plus, Search, Refresh, Document } from '@element-plus/icons-vue'
import { forumApi } from '../api'

const router = useRouter()
const state = reactive({ list: [], keyword: '', loading: false })

async function load() {
  state.loading = true
  try {
    state.list = await forumApi.list({ keyword: state.keyword || undefined })
  } finally {
    state.loading = false
  }
}

function resetQuery() {
  state.keyword = ''
  load()
}

function rowClick(row) {
  router.push('/forum/' + row.id)
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>在线论坛</h2>
      <p class="sub">仅展示审核通过的帖子；发帖后需教师或管理员审核通过方可公开显示。</p>
    </div>

    <el-card shadow="never" class="ruoyi-query-card">
      <template #header>
        <span class="card-header-title">筛选条件</span>
      </template>
      <el-form :inline="true" class="ruoyi-query-form" @submit.prevent="load">
        <el-form-item label="关键词">
          <el-input v-model="state.keyword" placeholder="标题 / 正文" clearable style="width: 240px" @keyup.enter="load" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" native-type="submit">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">帖子列表</span>
        <div class="table-toolbar" style="margin: 0">
          <el-button type="primary" :icon="Plus" @click="router.push('/forum/new')">我要发帖</el-button>
          <el-button type="success" plain :icon="Document" @click="router.push('/forum/mine')">我的帖子</el-button>
        </div>
      </template>
      <el-table v-loading="state.loading" :data="state.list" stripe border style="width: 100%" @row-click="rowClick">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" align="center" />
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column prop="createdAt" label="发布时间" width="170" align="center" />
        <el-table-column label="操作" width="90" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click.stop="router.push('/forum/' + row.id)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!state.loading && !state.list.length" description="暂无帖子，或暂无符合关键词的结果" />
    </el-card>
  </div>
</template>

<style scoped>
:deep(.el-table__row) {
  cursor: pointer;
}
</style>
