<script setup>
import { onMounted, reactive } from 'vue'
import { Download } from '@element-plus/icons-vue'
import { adminApi } from '../api'

const st = reactive({
  idPending: [],
  tPending: [],
  users: [],
  logs: [],
})

async function loadAll() {
  st.idPending = await adminApi.pendingIdentity()
  st.tPending = await adminApi.pendingTeacher()
  st.users = await adminApi.users({})
  st.logs = await adminApi.backupLogs()
}

async function auditId(row, approve) {
  await adminApi.auditIdentity(row.id, { approve, remark: approve ? '' : '不符合要求' })
  await loadAll()
}

async function auditT(row, approve) {
  await adminApi.auditTeacher(row.id, { approve, remark: approve ? '' : '材料不全' })
  await loadAll()
}

async function backup() {
  const path = await adminApi.exportBackup()
  alert('已导出：' + path)
  await loadAll()
}

onMounted(loadAll)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>系统管理</h2>
      <p class="sub">用户身份审核、教师入驻、用户列表与数据导出备份。</p>
    </div>

    <el-card shadow="never" class="ruoyi-panel-card">
      <el-tabs>
        <el-tab-pane label="身份审核">
          <el-table :data="st.idPending" stripe border style="width: 100%">
            <el-table-column type="index" label="#" width="55" align="center" />
            <el-table-column prop="username" label="账号" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column label="操作" width="160" align="center" fixed="right">
              <template #default="{ row }">
                <el-button link type="success" @click="auditId(row, true)">通过</el-button>
                <el-button link type="danger" @click="auditId(row, false)">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="教师入驻">
          <el-table :data="st.tPending" stripe border style="width: 100%">
            <el-table-column type="index" label="#" width="55" align="center" />
            <el-table-column prop="username" label="账号" />
            <el-table-column prop="teacherCertUrl" label="资格证" show-overflow-tooltip />
            <el-table-column label="操作" width="160" align="center" fixed="right">
              <template #default="{ row }">
                <el-button link type="success" @click="auditT(row, true)">通过</el-button>
                <el-button link type="danger" @click="auditT(row, false)">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="用户列表">
          <el-table :data="st.users" stripe border max-height="440" style="width: 100%">
            <el-table-column type="index" label="#" width="55" align="center" />
            <el-table-column prop="username" label="账号" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="role" label="角色" width="100" align="center" />
            <el-table-column prop="identityAuditStatus" label="身份" width="110" align="center" />
            <el-table-column prop="teacherAuditStatus" label="教师审核" width="110" align="center" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="数据备份">
          <el-alert type="info" :closable="false" show-icon class="mb16" title="导出用户等核心数据为 JSON 文件（演示备份能力）" />
          <el-button type="primary" :icon="Download" @click="backup">执行导出</el-button>
          <el-table :data="st.logs" stripe border class="mt16" style="width: 100%">
            <el-table-column prop="backupType" label="类型" width="100" align="center" />
            <el-table-column prop="filePath" label="路径" show-overflow-tooltip />
            <el-table-column prop="createdAt" label="时间" width="180" align="center" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
.mt16 {
  margin-top: 16px;
}
</style>
