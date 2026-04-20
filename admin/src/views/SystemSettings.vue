<script setup>
import { onMounted, reactive } from 'vue'
import { Download } from '@element-plus/icons-vue'
import { adminApi } from '../api'

const st = reactive({ logs: [] })

async function load() {
  st.logs = await adminApi.backupLogs()
}

async function backup() {
  const path = await adminApi.exportBackup()
  alert('已导出：' + path)
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>系统设置</h2>
      <p class="sub">数据备份导出与备份记录查询。</p>
    </div>

    <el-card shadow="never" class="ruoyi-panel-card">
      <el-alert type="info" :closable="false" show-icon class="mb16" title="导出用户等核心数据为 JSON 文件（演示备份能力）" />
      <el-button type="primary" :icon="Download" @click="backup">执行导出</el-button>
      <el-table :data="st.logs" stripe border class="mt16" style="width: 100%">
        <el-table-column prop="backupType" label="类型" width="100" align="center" />
        <el-table-column prop="filePath" label="路径" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="时间" width="180" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.mb16 {
  margin-bottom: 16px;
}
.mt16 {
  margin-top: 16px;
}
</style>
