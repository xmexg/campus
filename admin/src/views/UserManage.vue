<script setup>
import { onMounted, reactive } from 'vue'
import { adminApi } from '../api'

const st = reactive({ list: [], loading: false })
const query = reactive({ keyword: '', role: '' })
const dlg = reactive({
  show: false,
  id: null,
  username: '',
  nickname: '',
  phone: '',
  role: 'STUDENT',
  status: 1,
  avatarUrl: '',
})

async function load() {
  st.loading = true
  try {
    st.list = await adminApi.users({
      keyword: query.keyword || undefined,
      role: query.role || undefined,
    })
  } finally {
    st.loading = false
  }
}

function open(row) {
  dlg.id = row.id
  dlg.username = row.username
  dlg.nickname = row.nickname || ''
  dlg.phone = row.phone || ''
  dlg.role = row.role || 'STUDENT'
  dlg.status = row.status != null ? row.status : 1
  dlg.avatarUrl = row.avatarUrl || ''
  dlg.show = true
}

async function save() {
  await adminApi.updateUser(dlg.id, {
    nickname: dlg.nickname,
    phone: dlg.phone || null,
    role: dlg.role,
    status: dlg.status,
    avatarUrl: dlg.avatarUrl || null,
  })
  dlg.show = false
  await load()
}

onMounted(load)
</script>

<template>
  <div class="app-container">
    <div class="page-title-bar">
      <h2>个人中心（用户管理）</h2>
      <p class="sub">维护平台用户昵称、角色、状态与头像等（管理员操作）。</p>
    </div>

    <el-card shadow="never" class="ruoyi-table-card">
      <template #header>
        <div class="toolbar">
          <el-input v-model="query.keyword" clearable placeholder="账号/昵称" style="width: 200px" @clear="load" />
          <el-select v-model="query.role" clearable placeholder="角色" style="width: 120px" @clear="load">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
          <el-button type="primary" @click="load">查询</el-button>
        </div>
      </template>

      <el-table v-loading="st.loading" :data="st.list" stripe border max-height="520" style="width: 100%">
        <el-table-column type="index" label="#" width="55" align="center" />
        <el-table-column prop="username" label="账号" width="140" />
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="phone" label="手机" width="130" align="center" />
        <el-table-column prop="role" label="角色" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="identityAuditStatus" label="身份审核" width="110" align="center" />
        <el-table-column prop="teacherAuditStatus" label="教师审核" width="110" align="center" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="open(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dlg.show" title="编辑用户" width="480px" destroy-on-close>
      <el-form label-width="88px">
        <el-form-item label="账号">
          <el-input v-model="dlg.username" disabled />
        </el-form-item>
        <el-form-item label="昵称"><el-input v-model="dlg.nickname" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="dlg.phone" /></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="dlg.role" style="width: 100%">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dlg.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像URL"><el-input v-model="dlg.avatarUrl" placeholder="可选" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dlg.show = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
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
