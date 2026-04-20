<script setup>
import { computed, onMounted, reactive } from 'vue'
import { Upload } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { userApi, contentApi, fileApi } from '../api'
import { useSession } from '../store/session'
import { resolveApiStaticUrl } from '../utils/mediaUrl'

const { state, refreshUser } = useSession()
const pageLoading = reactive({ v: false })
const profile = reactive({ nickname: '', phone: '', avatarUrl: '' })
const pwd = reactive({ oldPassword: '', newPassword: '' })
const teacher = reactive({ intro: '', materialUrl: '', certUrl: '' })
const submitContent = reactive({
  contentType: 'ARTICLE',
  title: '',
  summary: '',
  body: '',
  videoUrl: '',
  category: '',
})

const roleMap = { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' }

const auditMap = { APPROVED: '已通过', PENDING: '待审核', REJECTED: '未通过' }

const isStudent = computed(() => state.user?.role === 'STUDENT')
const canApplyTeacher = computed(() => isStudent.value && state.user?.identityAuditStatus === 'APPROVED')
const teacherApplyPending = computed(() => state.user?.teacherAuditStatus === 'PENDING')

function auditLabel(s) {
  return auditMap[s] || s || '—'
}

function auditTagType(s) {
  if (s === 'APPROVED') return 'success'
  if (s === 'REJECTED') return 'danger'
  if (s === 'PENDING') return 'warning'
  return 'info'
}

async function load() {
  pageLoading.v = true
  try {
    await refreshUser()
  } finally {
    pageLoading.v = false
  }
  const u = state.user
  if (u) {
    profile.nickname = u.nickname || ''
    profile.phone = u.phone || ''
    profile.avatarUrl = u.avatarUrl || ''
    teacher.intro = u.teacherIntro || ''
    teacher.materialUrl = u.teacherMaterialUrl || ''
    teacher.certUrl = u.teacherCertUrl || ''
  }
}

async function saveProfile() {
  await userApi.profile(profile)
  await load()
}

async function savePwd() {
  await userApi.password(pwd)
  pwd.oldPassword = ''
  pwd.newPassword = ''
}

async function applyTeacher() {
  if (!canApplyTeacher.value) {
    ElMessage.warning('身份审核通过后才可申请教师认证')
    return
  }
  if (teacherApplyPending.value) {
    ElMessage.info('教师认证申请已提交，请等待管理员审核')
    return
  }
  await userApi.applyTeacher({ certUrl: teacher.certUrl, intro: teacher.intro })
  await load()
  ElMessage.success('教师认证申请提交成功，请等待管理员审核')
}

async function saveTeacher() {
  await userApi.teacherInfo({ intro: teacher.intro, materialUrl: teacher.materialUrl })
  await load()
}

async function submitTeacherContent() {
  await contentApi.teacherSubmit({ ...submitContent })
  Object.assign(submitContent, { title: '', summary: '', body: '', videoUrl: '', category: '' })
}

async function customUpload(options, field) {
  const { file, onError, onSuccess } = options
  try {
    const url = await fileApi.upload(file)
    if (field === 'cert') teacher.certUrl = url
    if (field === 'mat') teacher.materialUrl = url
    if (field === 'avatar') profile.avatarUrl = url
    onSuccess(url)
    ElMessage.success('上传成功')
  } catch (e) {
    onError(e)
  }
}

async function uploadVideoContent(options) {
  const { file, onError, onSuccess } = options
  try {
    const url = await fileApi.upload(file)
    submitContent.videoUrl = url
    onSuccess(url)
    ElMessage.success('视频上传成功')
  } catch (e) {
    onError(e)
  }
}

const avatarDisplaySrc = computed(() => resolveApiStaticUrl(profile.avatarUrl))

onMounted(load)
</script>

<template>
  <div class="app-container" v-loading="pageLoading.v">
    <div class="page-title-bar">
      <h2>个人中心</h2>
      <p class="sub">维护账号资料、密码；学生可申请教师入驻，教师可提交普法投稿。</p>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="ruoyi-panel-card mb16">
          <template #header>
            <span>账户信息</span>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="登录账号">{{ state.user?.username }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ roleMap[state.user?.role] || state.user?.role }}</el-descriptions-item>
            <el-descriptions-item label="身份审核">
              <el-tag :type="auditTagType(state.user?.identityAuditStatus)" size="small">
                {{ auditLabel(state.user?.identityAuditStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="教师认证">
              <el-tag :type="auditTagType(state.user?.teacherAuditStatus)" size="small">
                {{ auditLabel(state.user?.teacherAuditStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card shadow="never" class="ruoyi-panel-card mb16">
          <template #header>
            <span>基本资料</span>
          </template>
          <el-form label-width="100px">
            <el-form-item label="昵称">
              <el-input v-model="profile.nickname" style="max-width: 360px" />
            </el-form-item>
            <el-form-item label="手机">
              <el-input v-model="profile.phone" style="max-width: 360px" />
            </el-form-item>
            <el-form-item label="头像">
              <div class="avatar-field">
                <el-avatar class="avatar-preview" :size="88" shape="square" :src="avatarDisplaySrc">
                  {{ (profile.nickname || state.user?.username || '用').slice(0, 1) }}
                </el-avatar>
                <div class="avatar-field-main">
                  <div class="upload-line">
                    <el-input v-model="profile.avatarUrl" placeholder="上传后自动填入，也可粘贴完整图片 URL" class="upload-line-input" />
                    <el-upload
                      action="#"
                      :show-file-list="false"
                      :http-request="(opt) => customUpload(opt, 'avatar')"
                      accept="image/jpeg,image/png,image/gif,image/webp"
                    >
                      <el-button type="primary" :icon="Upload">上传图片</el-button>
                    </el-upload>
                  </div>
                  <p class="avatar-hint">左侧为实时预览（含 /api 前缀路径）；点击「保存资料」写入数据库后，顶栏头像会随会话刷新显示。</p>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile">保存资料</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card shadow="never" class="ruoyi-panel-card mb16">
          <template #header>
            <span>修改密码</span>
          </template>
          <el-form label-width="100px">
            <el-form-item label="原密码">
              <el-input v-model="pwd.oldPassword" type="password" show-password style="max-width: 360px" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="pwd.newPassword" type="password" show-password style="max-width: 360px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="savePwd">更新密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <template v-if="isStudent">
          <el-card v-if="!canApplyTeacher" shadow="never" class="ruoyi-panel-card mb16">
            <template #header>
              <span>申请教师入驻</span>
            </template>
            <el-alert
              type="warning"
              :closable="false"
              title="请先完成并通过身份认证，审核通过后可申请教师入驻。"
            />
          </el-card>

          <el-card v-else shadow="never" class="ruoyi-panel-card mb16">
            <template #header>
              <span>申请教师入驻</span>
            </template>
            <el-alert type="info" :closable="false" class="mb16" title="上传执业资格证相关材料，管理员审核通过后将升级为教师。" />
            <el-alert
              v-if="teacherApplyPending"
              type="success"
              :closable="false"
              class="mb16"
              title="申请已提交，正在等待管理员审核。"
            />
            <el-form label-width="100px">
              <el-form-item label="简介">
                <el-input v-model="teacher.intro" type="textarea" :rows="3" style="max-width: 480px" />
              </el-form-item>
              <el-form-item label="资格证">
                <div class="upload-line">
                  <el-input v-model="teacher.certUrl" placeholder="上传后自动填入路径" class="upload-line-input upload-line-input--wide" />
                  <el-upload
                    action="#"
                    :show-file-list="false"
                    :http-request="(opt) => customUpload(opt, 'cert')"
                    accept="image/jpeg,image/png,image/gif,image/webp,.pdf,application/pdf"
                  >
                    <el-button type="primary" :icon="Upload">上传文件</el-button>
                  </el-upload>
                </div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="teacherApplyPending" @click="applyTeacher">
                  {{ teacherApplyPending ? '已提交审核' : '提交申请' }}
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </template>

        <template v-if="state.user?.role === 'TEACHER'">
          <el-card shadow="never" class="ruoyi-panel-card mb16">
            <template #header>
              <span>教师资料</span>
            </template>
            <el-form label-width="100px">
              <el-form-item label="简介">
                <el-input v-model="teacher.intro" type="textarea" :rows="4" style="max-width: 480px" />
              </el-form-item>
              <el-form-item label="教研资料">
                <div class="upload-line">
                  <el-input v-model="teacher.materialUrl" class="upload-line-input upload-line-input--wide" />
                  <el-upload action="#" :show-file-list="false" :http-request="(opt) => customUpload(opt, 'mat')">
                    <el-button type="primary" :icon="Upload">上传文件</el-button>
                  </el-upload>
                </div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveTeacher">保存</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card shadow="never" class="ruoyi-panel-card">
            <template #header>
              <span>投稿普法内容</span>
            </template>
            <el-form label-width="100px">
              <el-form-item label="类型">
                <el-select v-model="submitContent.contentType" style="width: 200px">
                  <el-option label="文章" value="ARTICLE" />
                  <el-option label="视频" value="VIDEO" />
                  <el-option label="资讯" value="NEWS" />
                </el-select>
              </el-form-item>
              <el-form-item label="标题">
                <el-input v-model="submitContent.title" style="max-width: 480px" />
              </el-form-item>
              <el-form-item label="摘要">
                <el-input v-model="submitContent.summary" style="max-width: 480px" />
              </el-form-item>
              <el-form-item label="正文">
                <el-input v-model="submitContent.body" type="textarea" :rows="5" style="max-width: 480px" />
              </el-form-item>
              <el-form-item v-if="submitContent.contentType === 'VIDEO'" label="视频">
                <div class="upload-line">
                  <el-upload
                    :show-file-list="false"
                    :http-request="uploadVideoContent"
                    accept="video/*,.mp4,.webm,.ogg,.mov,.m4v"
                  >
                    <el-button type="primary" :icon="Upload">上传视频</el-button>
                  </el-upload>
                  <el-input
                    v-model="submitContent.videoUrl"
                    class="upload-line-input upload-line-input--wide"
                    clearable
                    placeholder="上传后自动填入，或直接粘贴外链"
                  />
                </div>
              </el-form-item>
              <el-form-item label="分类">
                <el-input v-model="submitContent.category" style="max-width: 240px" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitTeacherContent">提交审核</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </template>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.mb16 {
  margin-bottom: 16px;
}

.upload-line {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.upload-line-input {
  max-width: 360px;
  flex: 1;
  min-width: 200px;
}

.upload-line-input--wide {
  max-width: 480px;
}

.upload-line :deep(.el-upload) {
  display: inline-flex;
}

.avatar-field {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 16px;
}

.avatar-preview {
  flex-shrink: 0;
  border: 1px solid #ebeef5;
}

.avatar-field-main {
  flex: 1;
  min-width: 220px;
}

.avatar-hint {
  margin: 8px 0 0;
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
}
</style>
