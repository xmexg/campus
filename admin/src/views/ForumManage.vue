<script setup>
import { onMounted, reactive } from 'vue'
import { ElMessageBox } from 'element-plus'
import { forumApi } from '../api'

const st = reactive({ list: [], loading: false })
const query = reactive({ keyword: '', auditStatus: '' })
const edit = reactive({ show: false, id: null, title: '', body: '' })
const audit = reactive({ id: null, approve: true, remark: '', show: false })

async function load() {
  st.loading = true
  try {
    st.list = await forumApi.modListAll({
      keyword: query.keyword || undefined,
      auditStatus: query.auditStatus || undefined,
    })
  } finally {
    st.loading = false
  }
}

function openEdit(row) {
  edit.id = row.id
  edit.title = row.title
  edit.body = row.body || ''
  edit.show = true
}

async function saveEdit() {
  await forumApi.modUpdatePost(edit.id, { title: edit.title, body: edit.body })
  edit.show = false
  await load()
}

function openAudit(row, approve) {
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
  await ElMessageBox.confirm('确定删除该帖子？', '提示', { type: 'warning' })
  await forumApi.modDelPost(id)
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>在线论坛</h2>
      <p class="sub">全量帖子查询、编辑与审核；待审核帖子可在此通过或驳回。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <div class="toolbar">
          <el-input
            v-model="query.keyword"
            clearable
            placeholder="标题关键词"
            style="width: 200px"
            @clear="load"
          />
          <el-select v-model="query.auditStatus" clearable placeholder="审核状态" style="width: 140px" @change="load" @clear="load">
            <el-option label="待审核" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
          <el-button type="primary" @click="load">查询</el-button>
        </div>
      </template>

      <el-table v-loading="st.loading" :data="st.list" stripe border style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="authorName" label="作者" width="120" align="center" />
        <el-table-column prop="auditStatus" label="状态" width="100" align="center" />
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column prop="createdAt" label="发布时间" width="170" align="center" />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <template v-if="row.auditStatus === 'PENDING'">
              <el-button link type="success" @click="openAudit(row, true)">通过</el-button>
              <el-button link type="warning" @click="openAudit(row, false)">驳回</el-button>
            </template>
            <el-button link type="danger" @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="edit.show" title="编辑帖子" width="640px" destroy-on-close>
      <el-form label-width="72px">
        <el-form-item label="标题"><el-input v-model="edit.title" /></el-form-item>
        <el-form-item label="正文"><el-input v-model="edit.body" type="textarea" :rows="8" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="edit.show = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="audit.show" :title="audit.approve ? '通过审核' : '驳回'" width="420px" destroy-on-close>
      <el-input v-if="!audit.approve" v-model="audit.remark" type="textarea" placeholder="驳回原因（建议填写）" :rows="3" />
      <template #footer>
        <el-button @click="audit.show = false">取消</el-button>
        <el-button type="primary" @click="doAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  width: 100%;
}
</style>
