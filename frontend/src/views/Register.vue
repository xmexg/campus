<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Iphone, Postcard, School } from '@element-plus/icons-vue'
import { auth } from '../api'
import { useSession } from '../store/session'

const router = useRouter()
const { setAuth } = useSession()
const form = reactive({ username: '', password: '', nickname: '', phone: '' })
const loading = reactive({ v: false })

async function submit() {
  loading.v = true
  try {
    const res = await auth.register(form)
    setAuth(res.token, res.user)
    router.push('/')
  } finally {
    loading.v = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-bg" />
    <div class="auth-blob auth-blob-1" />
    <div class="auth-blob auth-blob-2" />

    <div class="auth-card-wrap">
      <div class="auth-brand">
        <div class="brand-icon">
          <el-icon :size="32"><School /></el-icon>
        </div>
        <h1 class="brand-title">加入普法平台</h1>
        <p class="brand-sub">注册后由管理员审核身份，即可参与论坛与竞赛</p>
      </div>

      <div class="auth-card">
        <h2 class="card-title">创建账号</h2>
        <p class="card-hint">请填写真实信息，便于后续教学管理</p>

        <el-form class="auth-form" size="large" label-position="top" @submit.prevent="submit">
          <el-form-item label="登录账号">
            <el-input v-model="form.username" placeholder="3–32 位字符" :prefix-icon="User" clearable />
          </el-form-item>
          <el-form-item label="登录密码">
            <el-input v-model="form.password" type="password" placeholder="至少 6 位" show-password :prefix-icon="Lock" />
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" placeholder="展示名称" :prefix-icon="Postcard" clearable />
          </el-form-item>
          <el-form-item label="手机号（选填）">
            <el-input v-model="form.phone" placeholder="11 位手机号" :prefix-icon="Iphone" clearable />
          </el-form-item>
          <el-button type="primary" class="submit-btn" :loading="loading.v" native-type="submit">注册并登录</el-button>
        </el-form>

        <div class="auth-footer">
          <router-link class="link-login" to="/login">已有账号？返回登录</router-link>
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
  background: #0a1628;
}

.auth-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #132f4c 0%, #1e4d6b 40%, #153a52 100%);
}

.auth-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.45;
  pointer-events: none;
}

.auth-blob-1 {
  width: 320px;
  height: 320px;
  background: #409eff;
  top: -80px;
  right: -60px;
}

.auth-blob-2 {
  width: 280px;
  height: 280px;
  background: #67c23a;
  bottom: -100px;
  left: -80px;
  opacity: 0.25;
}

.auth-card-wrap {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 460px;
  padding: 24px;
}

.auth-brand {
  text-align: center;
  margin-bottom: 22px;
  color: #e8f4ff;
}

.brand-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #a0cfff;
  margin-bottom: 14px;
}

.brand-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}

.brand-sub {
  margin: 10px 0 0;
  font-size: 13px;
  color: rgba(232, 244, 255, 0.6);
  line-height: 1.5;
  max-width: 320px;
  margin-left: auto;
  margin-right: auto;
}

.auth-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px 28px 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.28);
}

.card-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #303133;
}

.card-hint {
  margin: 6px 0 8px;
  font-size: 13px;
  color: #909399;
}

.auth-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #606266;
}

.auth-form :deep(.el-input__wrapper) {
  border-radius: 10px;
}

.submit-btn {
  width: 100%;
  height: 44px;
  margin-top: 12px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
}

.auth-footer {
  margin-top: 20px;
  text-align: center;
}

.link-login {
  font-size: 14px;
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.link-login:hover {
  color: #66b1ff;
}
</style>
