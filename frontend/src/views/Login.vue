<script setup>
import { reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, Lock, School } from '@element-plus/icons-vue'
import { auth } from '../api'
import { useSession } from '../store/session'

const router = useRouter()
const route = useRoute()
const { setAuth } = useSession()
const form = reactive({ username: '', password: '' })
const loading = reactive({ v: false })

async function submit() {
  loading.v = true
  try {
    const res = await auth.login(form)
    setAuth(res.token, res.user)
    router.push(route.query.redirect || '/')
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
        <h2 class="card-title">账号登录</h2>
        <p class="card-hint">欢迎回来，请登录后使用完整功能</p>

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
          <router-link class="link-register" to="/register">没有账号？立即注册</router-link>
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
  margin: 8px 0 24px;
  font-size: 14px;
  color: #909399;
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
