<script setup>
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Refresh } from '@element-plus/icons-vue'
import { contentApi } from '../api'

const router = useRouter()
const state = reactive({ list: [], keyword: '', type: '', loading: false })

async function load() {
  state.loading = true
  try {
    state.list = await contentApi.search({ keyword: state.keyword || undefined, contentType: state.type || undefined })
  } finally {
    state.loading = false
  }
}

function resetQuery() {
  state.keyword = ''
  state.type = ''
  load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>内容学习</h2>
      <p class="sub">支持按关键词检索普法资讯、文章与视频，点击「查看」阅读详情并记录浏览量。</p>
    </div>

    <el-card shadow="never" class="ruoyi-query-card">
      <template #header>
        <span class="card-header-title">筛选条件</span>
      </template>
      <el-form :inline="true" class="ruoyi-query-form" @submit.prevent="load">
        <el-form-item label="关键词">
          <el-input v-model="state.keyword" placeholder="标题 / 摘要 / 正文" clearable style="width: 220px" @keyup.enter="load" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="state.type" clearable placeholder="全部" style="width: 140px">
            <el-option label="资讯" value="NEWS" />
            <el-option label="文章" value="ARTICLE" />
            <el-option label="视频" value="VIDEO" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" native-type="submit">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">内容列表</span>
        <span style="font-size: 12px; color: #909399">共 {{ state.list.length }} 条</span>
      </template>
      <el-table v-loading="state.loading" :data="state.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contentType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.contentType === 'NEWS'" type="success" size="small">资讯</el-tag>
            <el-tag v-else-if="row.contentType === 'ARTICLE'" type="primary" size="small">文章</el-tag>
            <el-tag v-else-if="row.contentType === 'VIDEO'" type="warning" size="small">视频</el-tag>
            <el-tag v-else size="small">{{ row.contentType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" align="center" show-overflow-tooltip />
        <el-table-column prop="viewCount" label="浏览量" width="90" align="center" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="router.push('/learn/' + row.id)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!state.loading && !state.list.length" description="暂无符合条件的内容" />
    </el-card>
  </div>
</template>
