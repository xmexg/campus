<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { School, House, Reading, ChatDotRound, Document, Trophy, User, UserFilled, Setting } from '@element-plus/icons-vue'
import { useSession } from '../store/session'
import { adminBase } from '../config'
import { resolveApiStaticUrl } from '../utils/mediaUrl'

const router = useRouter()
const route = useRoute()
const { state, logout, isLogin, role } = useSession()

const roleLabel = computed(() => {
  const m = { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' }
  return m[role.value] || role.value || ''
})

const showConsoleEntry = computed(() => isLogin.value && ['TEACHER', 'ADMIN'].includes(role.value))

const activePath = computed(() => route.path)
const currentYear = new Date().getFullYear()

function go(path) {
  router.push(path)
}

function openAdminConsole() {
  window.location.href = `${adminBase}/admin`
}

function handleLogout() {
  logout()
  router.push('/login')
}

function onUserCmd(c) {
  if (c === 'profile') go('/profile')
  else if (c === 'console') openAdminConsole()
  else if (c === 'logout') handleLogout()
}

const identityBanner = computed(
  () =>
    isLogin.value &&
    state.user &&
    state.user.identityAuditStatus !== 'APPROVED' &&
    !route.path.startsWith('/profile')
)

const headerAvatarSrc = computed(() => resolveApiStaticUrl(state.user?.avatarUrl))
</script>

<template>
  <div class="portal-root">
    <header class="portal-header">
      <div class="portal-inner header-flex">
        <div class="brand" @click="go('/')">
          <el-icon class="brand-icon" :size="26"><School /></el-icon>
          <span class="brand-text">高校普法平台</span>
          <span class="brand-badge">门户前台</span>
        </div>

        <nav class="nav-menu">
          <router-link to="/" class="nav-link" :class="{ active: activePath === '/' }">
            <el-icon><House /></el-icon> 首页
          </router-link>
          <router-link to="/learn" class="nav-link" :class="{ active: activePath.startsWith('/learn') }">
            <el-icon><Reading /></el-icon> 内容学习
          </router-link>
          <router-link to="/forum" class="nav-link" :class="{ active: activePath.startsWith('/forum') }">
            <el-icon><ChatDotRound /></el-icon> 论坛
          </router-link>
          <router-link v-if="isLogin" to="/forum/mine" class="nav-link" :class="{ active: activePath === '/forum/mine' }">
            <el-icon><Document /></el-icon> 我的帖子
          </router-link>
          <router-link v-if="isLogin" to="/quiz" class="nav-link" :class="{ active: activePath === '/quiz' }">
            <el-icon><Trophy /></el-icon> 知识竞赛
          </router-link>
          <router-link v-if="isLogin" to="/profile" class="nav-link" :class="{ active: activePath === '/profile' }">
            <el-icon><User /></el-icon> 个人中心
          </router-link>
        </nav>

        <div class="header-actions">
          <el-button v-if="showConsoleEntry" type="warning" plain size="small" @click="openAdminConsole">
            <el-icon><Setting /></el-icon>
            管理后台
          </el-button>
          <template v-if="isLogin">
            <el-dropdown trigger="click" @command="onUserCmd">
              <span class="user-pill">
                <el-avatar :size="28" :src="headerAvatarSrc" class="header-user-avatar">
                  {{ (state.user?.nickname || state.user?.username || '?').slice(0, 1) }}
                </el-avatar>
                {{ state.user?.nickname || '用户' }}
                <span class="role-mini">{{ roleLabel }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item v-if="showConsoleEntry" command="console">进入管理后台</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" link @click="go('/login')">登录</el-button>
            <el-button type="primary" link @click="go('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </header>

    <el-alert v-if="identityBanner" type="warning" :closable="false" show-icon class="portal-alert">
      <template #title>身份审核提醒</template>
      <div class="alert-body">
        <p>您的账号尚待管理员身份审核，通过后可发帖、参与知识竞赛与发表评论。</p>
        <el-button type="warning" link @click="go('/profile')">前往个人中心查看审核状态</el-button>
      </div>
    </el-alert>

    <main class="portal-main">
      <div class="portal-inner main-pad">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>

    <footer class="portal-footer">
      <div class="portal-inner footer-main">
        <div class="footer-brand-block">
          <div class="footer-logo-line">
            <el-icon class="footer-logo-icon" :size="22"><School /></el-icon>
            <span class="footer-logo-text">高校普法平台</span>
          </div>
          <p class="footer-tagline">普法学习 · 论坛互动 · 在线竞赛，一站式校园法治教育门户。</p>
        </div>
        <div class="footer-links">
          <span class="footer-links-title">快速入口</span>
          <router-link to="/" class="footer-link">工作台</router-link>
          <router-link to="/learn" class="footer-link">内容学习</router-link>
          <router-link to="/forum" class="footer-link">在线论坛</router-link>
          <router-link to="/quiz" class="footer-link">知识竞赛</router-link>
          <router-link to="/profile" class="footer-link">个人中心</router-link>
        </div>
      </div>
      <div class="portal-inner footer-copy">© {{ currentYear }} 高校普法平台</div>
    </footer>
  </div>
</template>

<style scoped>
.portal-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f2f5;
}

.portal-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.portal-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.header-flex {
  display: flex;
  align-items: center;
  gap: 16px;
  height: 56px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-shrink: 0;
}

.brand-icon {
  color: #409eff;
}

.brand-text {
  font-size: 17px;
  font-weight: 700;
  color: #303133;
}

.brand-badge {
  font-size: 11px;
  color: #67c23a;
  background: #f0f9eb;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: 600;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  flex-wrap: wrap;
}

.nav-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  font-size: 14px;
  color: #606266;
  text-decoration: none;
  border-radius: 6px;
  transition: background 0.15s, color 0.15s;
}

.nav-link:hover {
  color: #409eff;
  background: #ecf5ff;
}

.nav-link.active {
  color: #409eff;
  font-weight: 600;
  background: #ecf5ff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.user-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
}

.header-user-avatar {
  flex-shrink: 0;
}

.role-mini {
  font-size: 11px;
  color: #909399;
  background: #f4f4f5;
  padding: 1px 6px;
  border-radius: 4px;
}

.portal-alert {
  border-radius: 0;
}

.portal-alert .alert-body {
  margin-top: 4px;
  font-size: 13px;
  line-height: 1.6;
  color: #606266;
}

.portal-alert .alert-body p {
  margin: 0 0 4px;
}

.portal-main {
  flex: 1;
}

.main-pad {
  padding-top: 20px;
  padding-bottom: 32px;
}

.portal-footer {
  padding: 24px 16px 16px;
  background: #fff;
  border-top: 1px solid #ebeef5;
}

.footer-main {
  display: flex;
  flex-wrap: wrap;
  gap: 28px 40px;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.footer-brand-block {
  max-width: 420px;
}

.footer-logo-line {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.footer-logo-icon {
  color: #409eff;
}

.footer-logo-text {
  font-size: 16px;
  font-weight: 700;
  color: #303133;
}

.footer-tagline {
  margin: 0;
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
}

.footer-links {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px 16px;
}

.footer-links-title {
  width: 100%;
  font-size: 13px;
  font-weight: 600;
  color: #606266;
  margin-bottom: 4px;
}

.footer-link {
  font-size: 13px;
  color: #606266;
  text-decoration: none;
}

.footer-link:hover {
  color: #409eff;
}

.footer-copy {
  text-align: center;
  padding-top: 14px;
  font-size: 12px;
  color: #c0c4cc;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.15s ease, transform 0.15s ease;
}
.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(6px);
}
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>
