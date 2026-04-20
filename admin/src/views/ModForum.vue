<script setup>
import { onMounted, reactive } from 'vue'
import { forumApi } from '../api'

const st = reactive({ list: [] })
const audit = reactive({ id: null, approve: true, remark: '', show: false })

async function load() {
  st.list = await forumApi.modPending()
}

function open(row, approve) {
  audit.id = row.id
  audit.approve = approve
  audit.remark = ''
  audit.show = true
}

async function doAudit() {
  await forumApi.modAudit(audit.id, { approve: audit.approve, remark: audit.remark })
  audit.show = false
  await load()
}

async function del(id) {
  await forumApi.modDelPost(id)
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>发帖审核</h2>
      <p class="sub">处理待审核帖子：通过后公开显示，驳回后作者可修改再次提交。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">待审核列表</span>
        <span style="font-size: 12px; color: #909399">共 {{ st.list.length }} 条</span>
      </template>
      <el-table :data="st.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" align="center" />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="success" @click="open(row, true)">通过</el-button>
            <el-button link type="warning" @click="open(row, false)">驳回</el-button>
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="audit.show" :title="audit.approve ? '通过审核' : '驳回'" width="420px" destroy-on-close>
      <el-input v-if="!audit.approve" v-model="audit.remark" type="textarea" placeholder="驳回原因（建议填写）" :rows="3" />
      <template #footer>
        <el-button @click="audit.show = false">取消</el-button>
        <el-button type="primary" @click="doAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
