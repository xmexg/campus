import { createRouter, createWebHistory } from 'vue-router'
import { useSession } from '../store/session'

const routes = [
  { path: '/', redirect: '/admin' },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', public: true },
  },
  {
    path: '/admin',
    component: () => import('../layout/AdminLayout.vue'),
    meta: { requiresAuth: true, consoleAccess: true },
    children: [
      { path: '', redirect: { name: 'consoleHome' } },
      {
        path: 'home',
        name: 'consoleHome',
        component: () => import('../views/Stats.vue'),
        meta: { title: '首页', roles: ['TEACHER', 'ADMIN'], activeMenu: '/admin/home' },
      },
      {
        path: 'content',
        name: 'consoleContent',
        component: () => import('../views/AdminContent.vue'),
        meta: { title: '内容学习', roles: ['ADMIN'], activeMenu: '/admin/content' },
      },
      {
        path: 'forum',
        name: 'consoleForum',
        component: () => import('../views/ForumManage.vue'),
        meta: { title: '在线论坛', roles: ['TEACHER', 'ADMIN'], activeMenu: '/admin/forum' },
      },
      {
        path: 'quiz',
        name: 'consoleQuiz',
        component: () => import('../views/QuestionBank.vue'),
        meta: { title: '知识竞赛', roles: ['TEACHER', 'ADMIN'], activeMenu: '/admin/quiz' },
      },
      {
        path: 'users',
        name: 'consoleUsers',
        component: () => import('../views/UserManage.vue'),
        meta: { title: '个人中心', roles: ['ADMIN'], activeMenu: '/admin/users' },
      },
      {
        path: 'teaching',
        name: 'consoleTeaching',
        component: () => import('../views/TeachingManage.vue'),
        meta: { title: '教学管理', roles: ['ADMIN'], activeMenu: '/admin/teaching' },
      },
      {
        path: 'settings',
        name: 'consoleSettings',
        component: () => import('../views/SystemSettings.vue'),
        meta: { title: '系统设置', roles: ['ADMIN'], activeMenu: '/admin/settings' },
      },
      {
        path: 'analytics',
        name: 'consoleAnalytics',
        component: () => import('../views/DataStats.vue'),
        meta: { title: '数据统计', roles: ['ADMIN'], activeMenu: '/admin/analytics' },
      },
      { path: 'stats', redirect: '/admin/home' },
      { path: 'questions', redirect: '/admin/quiz' },
      { path: 'forum-audit', redirect: '/admin/forum' },
      { path: 'system', redirect: '/admin/settings' },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 高校普法平台` : '高校普法平台 - 管理后台'
  const { state, refreshUser, logout } = useSession()
  if (state.token && !state.user) {
    await refreshUser()
  }

  if (to.matched.some((r) => r.meta.consoleAccess)) {
    if (!state.token) {
      next({ path: '/login', query: { redirect: to.fullPath } })
      return
    }
    if (!state.user || !['TEACHER', 'ADMIN'].includes(state.user.role)) {
      logout()
      next({ path: '/login', query: { needConsole: '1', redirect: to.fullPath } })
      return
    }
  }

  if (to.matched.some((r) => r.meta.requiresAuth) && !state.token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  if (to.meta.roles?.length && state.user && !to.meta.roles.includes(state.user.role)) {
    next('/admin')
    return
  }
  next()
})

export default router
