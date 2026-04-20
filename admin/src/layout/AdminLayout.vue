<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ChatLineRound,
  Document,
  Expand,
  Fold,
  House,
  School,
  Setting,
  PieChart,
  Trophy,
  User,
  UserFilled,
} from '@element-plus/icons-vue'
import { useSession } from '../store/session'
import { portalBase } from '../config'

const router = useRouter()
const route = useRoute()
const { state, logout, isLogin, role } = useSession()

const collapsed = ref(false)
const asideWidth = computed(() => (collapsed.value ? 64 : 210))
const activeMenu = computed(() => route.meta.activeMenu || route.path)
const pageTitle = computed(() => route.meta.title || '首页')

function go(path) {
  router.push(path)
}

function handleLogout() {
  logout()
  router.push('/login')
}

const showAdminOnlyMenu = computed(() => isLogin.value && role.value === 'ADMIN')

const roleLabel = computed(() => {
  const m = { STUDENT: '学生', TEACHER: '教师', ADMIN: '管理员' }
  return m[role.value] || role.value || ''
})
</script>

<template>
  <el-container class="ruoyi-layout">
    <el-aside :width="asideWidth + 'px'" class="ruoyi-aside">
      <div class="logo" @click="go('/admin/home')">
        <img class="logo-mark" src="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23409eff'%3E%3Cpath d='M12 2L2 7v10c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V7l-10-5z'/%3E%3C/svg%3E" alt="" />
        <span v-show="!collapsed" class="logo-text">管理后台</span>
      </div>
      <el-scrollbar class="menu-scroll">
        <el-menu
          :default-active="activeMenu"
          :collapse="collapsed"
          :collapse-transition="true"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
          router
          class="ruoyi-menu"
        >
          <el-menu-item index="/admin/home">
            <el-icon><House /></el-icon>
            <template #title>首页</template>
          </el-menu-item>

          <template v-if="showAdminOnlyMenu">
            <el-menu-item index="/admin/content">
              <el-icon><Document /></el-icon>
              <template #title>内容学习</template>
            </el-menu-item>
          </template>

          <el-menu-item index="/admin/forum">
            <el-icon><ChatLineRound /></el-icon>
            <template #title>在线论坛</template>
          </el-menu-item>
          <el-menu-item index="/admin/quiz">
            <el-icon><Trophy /></el-icon>
            <template #title>知识竞赛</template>
          </el-menu-item>

          <template v-if="showAdminOnlyMenu">
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <template #title>个人中心</template>
            </el-menu-item>
            <el-menu-item index="/admin/teaching">
              <el-icon><School /></el-icon>
              <template #title>教学管理</template>
            </el-menu-item>
            <el-menu-item index="/admin/settings">
              <el-icon><Setting /></el-icon>
              <template #title>系统设置</template>
            </el-menu-item>
            <el-menu-item index="/admin/analytics">
              <el-icon><PieChart /></el-icon>
              <template #title>数据统计</template>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>
      <div class="aside-footer">
        <el-button class="collapse-btn" text @click="collapsed = !collapsed">
          <el-icon :size="18" color="#bfcbd9">
            <Fold v-if="!collapsed" />
            <Expand v-else />
          </el-icon>
        </el-button>
      </div>
    </el-aside>

    <el-container class="main-container">
      <el-header class="ruoyi-header" height="50px">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>
              <a :href="portalBase" class="portal-crumb">前台首页</a>
            </el-breadcrumb-item>
            <el-breadcrumb-item>{{ pageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
     
          <template v-if="isLogin">
            <el-dropdown trigger="click" @command="(c) => (c === 'logout' ? handleLogout() : null)">
              <span class="user-trigger">
                <el-icon><UserFilled /></el-icon>
                <span>{{ state.user?.nickname || '用户' }}</span>
                <span class="role-tag">{{ roleLabel }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </div>
      </el-header>

      <el-main class="ruoyi-main">
        <div class="main-inner">
          <router-view v-slot="{ Component }">
            <transition name="fade-slide" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.ruoyi-layout {
  height: 100vh;
  background: #f0f2f5;
}

.ruoyi-aside {
  height: 100vh;
  background: #304156;
  display: flex;
  flex-direction: column;
  transition: width 0.28s;
  overflow: hidden;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.08);
}

.logo {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
  background: #2b2f3a;
  color: #fff;
  flex-shrink: 0;
}

.logo-mark {
  width: 28px;
  height: 28px;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  white-space: nowrap;
  letter-spacing: 0.02em;
}

.menu-scroll {
  flex: 1;
  min-height: 0;
}

.ruoyi-menu {
  border-right: none !important;
}

.ruoyi-menu:not(.el-menu--collapse) {
  width: 210px;
}

:deep(.ruoyi-menu .el-menu-item),
:deep(.ruoyi-menu .el-sub-menu__title) {
  height: 48px;
  line-height: 48px;
}

:deep(.ruoyi-menu .el-menu-item:hover),
:deep(.ruoyi-menu .el-sub-menu__title:hover) {
  background-color: #263445 !important;
}

:deep(.ruoyi-menu .el-menu-item.is-active) {
  background-color: #1f2d3d !important;
  color: #409eff !important;
}

.aside-footer {
  flex-shrink: 0;
  border-top: 1px solid #1f2d3d;
  padding: 8px 0;
  display: flex;
  justify-content: center;
}

.collapse-btn {
  width: 100%;
  color: #bfcbd9;
}

.main-container {
  flex-direction: column;
  min-width: 0;
}

.ruoyi-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 16px;
  z-index: 9;
}

.header-left :deep(.el-breadcrumb__inner) {
  font-weight: 500;
}

.portal-crumb {
  color: inherit;
  text-decoration: none;
}
.portal-crumb:hover {
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #606266;
  font-size: 14px;
}

.role-tag {
  font-size: 12px;
  color: #909399;
  padding: 0 6px;
  background: #f4f4f5;
  border-radius: 4px;
}

.ruoyi-main {
  padding: 0;
  overflow: auto;
}

.main-inner {
  padding: 20px;
  min-height: calc(100vh - 50px);
  background: #f0f2f5;
  box-sizing: border-box;
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
