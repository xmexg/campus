<script setup>
import { onMounted, reactive } from 'vue'
import { adminApi } from '../api'

const st = reactive({
  idPending: [],
  tPending: [],
})

async function load() {
  st.idPending = await adminApi.pendingIdentity()
  st.tPending = await adminApi.pendingTeacher()
}

async function auditId(row, approve) {
  await adminApi.auditIdentity(row.id, { approve, remark: approve ? '' : '不符合要求' })
  await load()
}

async function auditT(row, approve) {
  await adminApi.auditTeacher(row.id, { approve, remark: approve ? '' : '材料不全' })
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>教学管理</h2>
      <p class="sub">学生实名身份审核与教师资质入驻审核。</p>
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
      </el-tabs>
    </el-card>
  </div>
</template>
