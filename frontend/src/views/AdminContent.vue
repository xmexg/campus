<script setup>
import { onMounted, reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { adminApi } from '../api'

const st = reactive({ list: [], pending: [], dlg: false, auditDlg: false })
const form = reactive({
  id: null,
  contentType: 'ARTICLE',
  title: '',
  summary: '',
  body: '',
  videoUrl: '',
  coverUrl: '',
  category: '',
  published: 1,
  submitStatus: 'PUBLISHED',
})
const audit = reactive({ id: null, approve: true, remark: '' })

async function load() {
  st.list = await adminApi.contentList({})
  st.pending = await adminApi.contentPending()
}

function open(row) {
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null,
      contentType: 'ARTICLE',
      title: '',
      summary: '',
      body: '',
      videoUrl: '',
      coverUrl: '',
      category: '',
      published: 1,
      submitStatus: 'PUBLISHED',
    })
  }
  st.dlg = true
}

async function save() {
  await adminApi.contentSave({ ...form })
  st.dlg = false
  await load()
}

async function del(id) {
  await adminApi.contentDel(id)
  await load()
}

function openAudit(row, approve) {
  audit.id = row.id
  audit.approve = approve
  audit.remark = ''
  st.auditDlg = true
}

async function doAudit() {
  await adminApi.contentAudit(audit.id, { approve: audit.approve, remark: audit.remark })
  st.auditDlg = false
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>内容管理</h2>
      <p class="sub">维护普法素材，审核教师投稿，支持上架与退回。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card mb16">
      <template #header>
        <div style="display: flex; align-items: center; justify-content: space-between; width: 100%">
          <span class="card-header-title">待审核投稿</span>
          <el-button type="primary" :icon="Plus" size="small" @click="open(null)">新建内容</el-button>
        </div>
      </template>
      <el-table :data="st.pending" stripe border size="small" style="width: 100%">
        <el-table-column type="index" label="#" width="50" align="center" />
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="contentType" label="类型" width="90" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="success" @click="openAudit(row, true)">发布</el-button>
            <el-button link type="warning" @click="openAudit(row, false)">退回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <span class="card-header-title">全部内容</span>
        <span style="font-size: 12px; color: #909399">共 {{ st.list.length }} 条</span>
      </template>
      <el-table :data="st.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="contentType" label="类型" width="90" align="center" />
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column prop="submitStatus" label="投稿状态" width="110" align="center" />
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="open(row)">编辑</el-button>
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="st.dlg" title="内容维护" width="640px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="类型">
          <el-select v-model="form.contentType" style="width: 100%">
            <el-option label="文章" value="ARTICLE" />
            <el-option label="视频" value="VIDEO" />
            <el-option label="资讯" value="NEWS" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" /></el-form-item>
        <el-form-item label="正文"><el-input v-model="form.body" type="textarea" :rows="6" /></el-form-item>
        <el-form-item label="视频URL"><el-input v-model="form.videoUrl" /></el-form-item>
        <el-form-item label="封面"><el-input v-model="form.coverUrl" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="form.category" /></el-form-item>
        <el-form-item label="上架">
          <el-switch v-model="form.published" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="st.dlg = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="st.auditDlg" :title="audit.approve ? '通过并发布' : '退回投稿'" width="420px" destroy-on-close>
      <el-input v-if="!audit.approve" v-model="audit.remark" type="textarea" :rows="3" placeholder="退回说明" />
      <template #footer>
        <el-button @click="st.auditDlg = false">取消</el-button>
        <el-button type="primary" @click="doAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.mb16 {
  margin-bottom: 16px;
}
</style>
