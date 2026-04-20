<script setup>
import { computed, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, Lock, School } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { auth } from '../api'
import { useSession } from '../store/session'
import { portalBase } from '../config'

const router = useRouter()
const route = useRoute()
const { setAuth } = useSession()
const form = reactive({ username: '', password: '' })
const loading = reactive({ v: false })

const showNeedConsoleHint = computed(() => route.query.needConsole === '1')

async function submit() {
  loading.v = true
  try {
    const res = await auth.login(form)
    if (!res.user || !['TEACHER', 'ADMIN'].includes(res.user.role)) {
      ElMessage.warning('管理后台仅限教师或管理员账号，学生请从门户（师生端）登录')
      return
    }
    setAuth(res.token, res.user)
    const redir = route.query.redirect
    if (typeof redir === 'string' && redir.startsWith('/') && !redir.startsWith('//')) {
      router.push(redir)
    } else {
      router.push('/admin/stats')
    }
  } finally {
    loading.v = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-bg" />
    <div class="auth-grid" />
    <div class="auth-card-wrap">
      <div class="auth-brand">
        <div class="brand-icon">
          <el-icon :size="36"><School /></el-icon>
        </div>
        <h1 class="brand-title">高校普法平台</h1>
        <p class="brand-sub">法治教育 · 在线学习 · 竞赛互动</p>
      </div>

      <div class="auth-card">
        <h2 class="card-title">管理后台登录</h2>
        <p class="card-hint">教师 / 管理员请在此登录；学生请使用门户站点</p>
        <el-alert
          v-if="showNeedConsoleHint"
          type="warning"
          :closable="false"
          show-icon
          class="login-alert"
          title="当前账号无权进入管理后台，请使用教师或管理员账号登录。"
        />

        <el-form class="auth-form" size="large" @submit.prevent="submit">
          <el-form-item>
            <el-input v-model="form.username" placeholder="用户名" autocomplete="username" :prefix-icon="User" clearable />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="密码"
              autocomplete="current-password"
              show-password
              :prefix-icon="Lock"
              @keyup.enter="submit"
            />
          </el-form-item>
          <el-button type="primary" class="submit-btn" :loading="loading.v" native-type="submit">登 录</el-button>
        </el-form>

        <div class="auth-footer">
          <span class="demo-tip">演示账号：<code>admin</code> / <code>admin123</code></span>
          <a class="link-register" :href="`${portalBase}/register`">没有账号？前往门户注册</a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: #0c1426;
}

.auth-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #0f2744 0%, #1a3a52 35%, #0d2137 70%, #0a1628 100%);
}

.auth-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(64, 158, 255, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64, 158, 255, 0.06) 1px, transparent 1px);
  background-size: 48px 48px;
  mask-image: radial-gradient(ellipse 80% 70% at 50% 40%, black 20%, transparent 100%);
}

.auth-card-wrap {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  padding: 24px;
}

.auth-brand {
  text-align: center;
  margin-bottom: 28px;
  color: #e8f4ff;
}

.brand-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72px;
  height: 72px;
  border-radius: 20px;
  background: linear-gradient(145deg, rgba(64, 158, 255, 0.35), rgba(64, 158, 255, 0.08));
  border: 1px solid rgba(64, 158, 255, 0.35);
  color: #79bbff;
  margin-bottom: 16px;
}

.brand-title {
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  letter-spacing: 0.06em;
}

.brand-sub {
  margin: 10px 0 0;
  font-size: 14px;
  color: rgba(232, 244, 255, 0.55);
}

.auth-card {
  background: rgba(255, 255, 255, 0.97);
  border-radius: 16px;
  padding: 36px 32px 28px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.35), 0 0 0 1px rgba(255, 255, 255, 0.08) inset;
  backdrop-filter: blur(12px);
}

.card-title {
  margin: 0;
  font-size: 22px;
  font-weight: 700;
  color: #303133;
}

.card-hint {
  margin: 8px 0 12px;
  font-size: 14px;
  color: #909399;
}

.login-alert {
  margin-bottom: 16px;
}

.auth-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.submit-btn {
  width: 100%;
  height: 44px;
  margin-top: 8px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.2em;
}

.auth-footer {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  align-items: center;
}

.demo-tip {
  font-size: 12px;
  color: #909399;
}

.demo-tip code {
  background: #f4f4f5;
  padding: 2px 8px;
  border-radius: 4px;
  color: #606266;
}

.link-register {
  font-size: 14px;
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.link-register:hover {
  color: #66b1ff;
}
</style>
