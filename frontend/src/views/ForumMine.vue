<script setup>
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { forumApi } from '../api'

const router = useRouter()
const state = reactive({ list: [], loading: false })

function statusText(s) {
  if (s === 'APPROVED') return '已通过'
  if (s === 'PENDING') return '待审核'
  if (s === 'REJECTED') return '未通过'
  return s
}

async function load() {
  state.list = await forumApi.myPosts()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>我的帖子</h2>
      <p class="sub">查看审核状态；未通过的可修改后重新提交审核。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">帖子记录</span>
        <el-button type="primary" :icon="Plus" size="small" @click="router.push('/forum/new')">新发帖</el-button>
      </template>
      <el-table v-loading="state.loading" :data="state.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.auditStatus === 'APPROVED' ? 'success' : row.auditStatus === 'REJECTED' ? 'danger' : 'warning'" size="small">
              {{ statusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditRemark" label="审核说明" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.auditStatus === 'APPROVED'" link type="primary" @click="router.push('/forum/' + row.id)">查看</el-button>
            <el-button v-if="row.auditStatus !== 'APPROVED'" link type="warning" @click="router.push('/forum/edit/' + row.id)">修改</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!state.loading && !state.list.length" description="您还没有发帖，点击右上角「新发帖」开始" />
    </el-card>
  </div>
</template>
