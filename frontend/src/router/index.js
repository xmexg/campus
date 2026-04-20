import { createRouter, createWebHistory } from 'vue-router'
import { useSession } from '../store/session'
import { adminBase } from '../config'

function redirectToAdmin(fullPath) {
  return () => {
    window.location.replace(`${adminBase}${fullPath}`)
  }
}

/**
 * 门户（5173）：仅师生前台 + 登录/注册。管理后台为独立工程（5174）。
 */
const routes = [
  { path: '/stats', beforeEnter: redirectToAdmin('/admin/stats') },
  { path: '/teacher/questions', beforeEnter: redirectToAdmin('/admin/questions') },
  { path: '/mod/forum', beforeEnter: redirectToAdmin('/admin/forum-audit') },
  {
    path: '/admin/:pathMatch(.*)*',
    beforeEnter: (to) => {
      window.location.replace(`${adminBase}${to.fullPath}`)
    },
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', public: true },
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册', public: true },
  },
  {
    path: '/',
    component: () => import('../layout/PortalLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', name: 'home', component: () => import('../views/Home.vue'), meta: { title: '首页' } },
      { path: 'learn', name: 'learn', component: () => import('../views/LearnList.vue'), meta: { title: '内容学习' } },
      {
        path: 'learn/:id',
        name: 'learnDetail',
        component: () => import('../views/LearnDetail.vue'),
        meta: { title: '内容详情' },
      },
      { path: 'forum', name: 'forum', component: () => import('../views/ForumList.vue'), meta: { title: '在线论坛' } },
      {
        path: 'forum/mine',
        name: 'forumMine',
        component: () => import('../views/ForumMine.vue'),
        meta: { title: '我的帖子' },
      },
      {
        path: 'forum/new',
        name: 'forumNew',
        component: () => import('../views/ForumCreate.vue'),
        meta: { title: '发帖' },
      },
      {
        path: 'forum/edit/:id',
        name: 'forumEdit',
        component: () => import('../views/ForumEdit.vue'),
        meta: { title: '编辑帖子' },
      },
      {
        path: 'forum/:id',
        name: 'forumDetail',
        component: () => import('../views/ForumDetail.vue'),
        meta: { title: '帖子详情' },
      },
      {
        path: 'quiz',
        name: 'quiz',
        component: () => import('../views/Quiz.vue'),
        meta: { title: '知识竞赛' },
      },
      {
        path: 'profile',
        name: 'profile',
        component: () => import('../views/Profile.vue'),
        meta: { title: '个人中心' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, _from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 高校普法平台` : '高校普法平台'
  const { state, refreshUser } = useSession()
  if (state.token && !state.user) {
    await refreshUser()
  }

  if (to.meta.public && state.token) {
    const q = to.query.redirect
    if (typeof q === 'string' && q.startsWith('/') && !q.startsWith('//')) {
      next(q)
      return
    }
    next('/')
    return
  }

  if (to.matched.some((r) => r.meta.requiresAuth) && !state.token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  next()
})

export default router
