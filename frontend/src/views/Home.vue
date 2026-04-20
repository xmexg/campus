<script setup>
import { onMounted, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  ArrowRight,
  ChatDotRound,
  Reading,
  Trophy,
  Clock,
  TrendCharts,
  Guide,
  Right,
} from '@element-plus/icons-vue'
import { contentApi, forumApi } from '../api'
import { useSession } from '../store/session'

const router = useRouter()
const { state: sessionState, isLogin, identityOk } = useSession()

const tiles = [
  { title: '内容学习', desc: '法律资讯、案例文章与普法视频', icon: Reading, path: '/learn', type: 'primary' },
  { title: '在线论坛', desc: '发帖交流、师生互动与审核展示', icon: ChatDotRound, path: '/forum', type: 'success' },
  { title: '知识竞赛', desc: '在线答题、题库与成绩排名', icon: Trophy, path: '/quiz', type: 'warning' },
]

const st = reactive({
  loading: true,
  loadError: false,
  recentLearn: [],
  recentForum: [],
  learnTotal: 0,
  forumTotal: 0,
})

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return '上午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const displayName = computed(() => sessionState.user?.nickname || sessionState.user?.username || '同学')

async function loadHighlights() {
  st.loading = true
  st.loadError = false
  try {
    const [contents, posts] = await Promise.all([contentApi.search({}), forumApi.list({})])
    const c = Array.isArray(contents) ? contents : []
    const p = Array.isArray(posts) ? posts : []
    st.learnTotal = c.length
    st.forumTotal = p.length
    st.recentLearn = c.slice(0, 6)
    st.recentForum = p.slice(0, 5)
  } catch {
    st.loadError = true
    st.recentLearn = []
    st.recentForum = []
    st.learnTotal = 0
    st.forumTotal = 0
  } finally {
    st.loading = false
  }
}

function typeLabel(t) {
  const m = { NEWS: '资讯', ARTICLE: '文章', VIDEO: '视频' }
  return m[t] || t || '—'
}

onMounted(loadHighlights)
</script>

<template>
  <div class="home-page">
    <section class="hero">
      <div class="hero-inner">
        <div class="hero-text">
          <p class="hero-kicker">高校普法平台 · 门户工作台</p>
          <h1 class="hero-title">{{ greeting }}，{{ displayName }}</h1>
          <!-- <p class="hero-desc">
            在这里浏览普法素材、参与论坛讨论、完成在线竞赛。教师与管理员请通过顶部「管理后台」进入独立控制台（默认
            <code class="hero-code">5174</code> 端口）。
          </p> -->
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="router.push('/learn')">
              开始学习
              <el-icon class="el-icon--right"><Right /></el-icon>
            </el-button>
            <el-button size="large" @click="router.push('/forum')">进入论坛</el-button>
          </div>
          <el-alert
            v-if="isLogin && !identityOk"
            type="warning"
            :closable="false"
            show-icon
            class="hero-alert"
            title="您的身份尚在审核中，通过后即可发帖、答题与评论。"
          />
        </div>
        <div class="hero-side">
          <div class="hero-stat-card">
            <div class="stat-row">
              <el-icon class="stat-icon" :size="28"><Reading /></el-icon>
              <div>
                <div class="stat-label">可学习内容（条）</div>
                <div class="stat-value">
                  <template v-if="st.loading">—</template>
                  <template v-else>{{ st.learnTotal }}</template>
                </div>
              </div>
            </div>
            <el-divider class="stat-divider" />
            <div class="stat-row">
              <el-icon class="stat-icon stat-icon--green" :size="28"><ChatDotRound /></el-icon>
              <div>
                <div class="stat-label">论坛公开帖（条）</div>
                <div class="stat-value">
                  <template v-if="st.loading">—</template>
                  <template v-else>{{ st.forumTotal }}</template>
                </div>
              </div>
            </div>
            <p v-if="st.loadError" class="stat-hint error">数据加载失败，请确认后端已在 8080 启动。</p>
            <p v-else class="stat-hint">数字来自当前列表接口；下方卡片可快速打开详情。</p>
          </div>
        </div>
      </div>
    </section>

    <div class="app-container home-section">
      <div class="page-title-bar">
        <h2>常用入口</h2>
        <p class="sub">与顶部导航一致，支持一键进入各业务模块。</p>
      </div>

      <el-row :gutter="16" class="tiles-row">
        <el-col v-for="item in tiles" :key="item.path" :xs="24" :sm="12" :md="8">
          <el-card shadow="hover" class="dash-tile" @click="router.push(item.path)">
            <div class="dash-tile-inner">
              <el-icon class="dash-icon" :class="'dash-icon--' + item.type" :size="40">
                <component :is="item.icon" />
              </el-icon>
              <div class="dash-text">
                <div class="dash-title">{{ item.title }}</div>
                <div class="dash-desc">{{ item.desc }}</div>
              </div>
              <el-icon class="dash-arrow"><ArrowRight /></el-icon>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="16" class="preview-row">
        <el-col :xs="24" :lg="14">
          <el-card v-loading="st.loading" shadow="never" class="ruoyi-panel-card preview-card">
            <template #header>
              <div class="card-head-flex">
                <span><el-icon><Clock /></el-icon> 最新普法内容</span>
                <el-button type="primary" link @click="router.push('/learn')">查看全部</el-button>
              </div>
            </template>
            <el-empty v-if="!st.loading && !st.recentLearn.length" description="暂无内容，请等待管理员发布" />
            <ul v-else class="link-list">
              <li v-for="row in st.recentLearn" :key="row.id" class="link-item" @click="router.push('/learn/' + row.id)">
                <span class="link-title">{{ row.title }}</span>
                <el-tag size="small" type="info">{{ typeLabel(row.contentType) }}</el-tag>
                <span class="link-meta">{{ row.viewCount ?? 0 }} 浏览</span>
              </li>
            </ul>
          </el-card>
        </el-col>
        <el-col :xs="24" :lg="10">
          <el-card v-loading="st.loading" shadow="never" class="ruoyi-panel-card preview-card">
            <template #header>
              <div class="card-head-flex">
                <span><el-icon><TrendCharts /></el-icon> 论坛热帖预览</span>
                <el-button type="primary" link @click="router.push('/forum')">进入论坛</el-button>
              </div>
            </template>
            <el-empty v-if="!st.loading && !st.recentForum.length" description="暂无公开帖子" />
            <ul v-else class="link-list">
              <li v-for="row in st.recentForum" :key="row.id" class="link-item" @click="router.push('/forum/' + row.id)">
                <span class="link-title">{{ row.title }}</span>
                <span class="link-meta">{{ row.authorName }}</span>
              </li>
            </ul>
          </el-card>
        </el-col>
      </el-row>

      <el-card shadow="never" class="ruoyi-panel-card mb16 guide-card">
        <template #header>
          <span><el-icon><Guide /></el-icon> 使用指引</span>
        </template>
        <div class="guide-grid">
          <div class="guide-item">
            <span class="guide-no">1</span>
            <strong>登录账号</strong>
            <p>使用注册账号或演示账号登录门户（默认端口 5173）。</p>
          </div>
          <div class="guide-item">
            <span class="guide-no">2</span>
            <strong>身份审核</strong>
            <p>新用户需管理员在后台通过身份审核后，才可发帖、答题与评论。</p>
          </div>
          <div class="guide-item">
            <span class="guide-no">3</span>
            <strong>学习互动</strong>
            <p>浏览普法内容、参与论坛讨论、完成在线竞赛巩固知识。</p>
          </div>
          <div class="guide-item">
            <span class="guide-no">4</span>
            <strong>个人中心</strong>
            <p>维护资料与密码；学生可申请教师入驻，教师可提交普法投稿。</p>
          </div>
        </div>
      </el-card>

      <el-card shadow="never" class="ruoyi-panel-card">
        <template #header>
          <span>平台简介</span>
        </template>
        <p class="intro-text">
          本系统基于 <strong>Spring Boot</strong> 与 <strong>Vue 3</strong> 构建，聚合普法素材学习、论坛互动与知识竞赛；教师与管理员在
          <strong>独立管理端</strong> 完成内容审核、题库维护与数据统计，界面风格与若依后台一脉相承，便于日常运营与答辩演示。
        </p>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  margin: -20px -20px 0;
}

@media (max-width: 768px) {
  .home-page {
    margin: -16px -12px 0;
  }
}

.hero {
  background: linear-gradient(135deg, #1a3a5c 0%, #0d2847 45%, #0a1f38 100%);
  color: #fff;
  padding: 32px 20px 40px;
  margin-bottom: 8px;
}

.hero-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
  align-items: flex-start;
  justify-content: space-between;
}

.hero-text {
  flex: 1;
  min-width: 280px;
  max-width: 640px;
}

.hero-kicker {
  margin: 0 0 8px;
  font-size: 13px;
  opacity: 0.85;
  letter-spacing: 0.04em;
}

.hero-title {
  margin: 0 0 12px;
  font-size: 26px;
  font-weight: 700;
  line-height: 1.3;
}

.hero-desc {
  margin: 0 0 20px;
  font-size: 14px;
  line-height: 1.75;
  opacity: 0.9;
}

.hero-code {
  padding: 1px 6px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.12);
  font-size: 12px;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.hero-alert {
  max-width: 560px;
  background: rgba(255, 255, 255, 0.95) !important;
}

.hero-side {
  flex-shrink: 0;
  width: 100%;
  max-width: 320px;
}

.hero-stat-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(8px);
}

.stat-row {
  display: flex;
  align-items: center;
  gap: 14px;
}

.stat-icon {
  color: #79bbff;
}

.stat-icon--green {
  color: #95d475;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

.stat-value {
  font-size: 22px;
  font-weight: 700;
  margin-top: 2px;
}

.stat-divider {
  margin: 16px 0;
  border-color: rgba(255, 255, 255, 0.15);
}

.stat-hint {
  margin: 12px 0 0;
  font-size: 12px;
  opacity: 0.75;
  line-height: 1.5;
}

.stat-hint.error {
  color: #f89898;
  opacity: 1;
}

.home-section {
  padding-top: 8px;
}

.tiles-row {
  margin-bottom: 8px;
}

.preview-row {
  margin-bottom: 16px;
}

.preview-card :deep(.el-card__header) {
  padding: 14px 18px;
}

.card-head-flex {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  gap: 12px;
  font-weight: 600;
  font-size: 15px;
  color: #303133;
}

.card-head-flex .el-icon {
  vertical-align: middle;
  margin-right: 6px;
}

.link-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  padding: 12px 4px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background 0.12s ease;
}

.link-item:last-child {
  border-bottom: none;
}

.link-item:hover {
  background: #f5f7fa;
}

.link-title {
  flex: 1;
  min-width: 160px;
  font-size: 14px;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.link-meta {
  font-size: 12px;
  color: #909399;
}

.guide-card :deep(.el-card__header) {
  font-weight: 600;
}

.guide-card .el-icon {
  vertical-align: middle;
  margin-right: 6px;
}

.guide-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.guide-item {
  padding: 14px 16px;
  background: #f8fafc;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  font-size: 13px;
  color: #606266;
  line-height: 1.55;
}

.guide-item strong {
  display: block;
  color: #303133;
  margin-bottom: 6px;
  font-size: 14px;
}

.guide-item p {
  margin: 0;
}

.guide-no {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: #409eff;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  margin-bottom: 8px;
}

.intro-text {
  margin: 0;
  color: #606266;
  line-height: 1.85;
  font-size: 14px;
}

.dash-tile {
  margin-bottom: 16px;
  cursor: pointer;
  border-radius: 8px;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.dash-tile:hover {
  transform: translateY(-2px);
}

.dash-tile :deep(.el-card__body) {
  padding: 20px;
}

.dash-tile-inner {
  display: flex;
  align-items: center;
  gap: 16px;
}

.dash-icon {
  flex-shrink: 0;
  padding: 12px;
  border-radius: 10px;
  background: #ecf5ff;
  color: #409eff;
}

.dash-icon--success {
  background: #f0f9eb;
  color: #67c23a;
}

.dash-icon--warning {
  background: #fdf6ec;
  color: #e6a23c;
}

.dash-text {
  flex: 1;
  min-width: 0;
}

.dash-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 6px;
}

.dash-desc {
  font-size: 13px;
  color: #909399;
  line-height: 1.5;
}

.dash-arrow {
  color: #c0c4cc;
  font-size: 18px;
}
</style>
