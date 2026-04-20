# 基于 Spring Boot 的高校普法平台

前后端分离：后端 **Java 8 + Spring Boot 2.7 + MyBatis + MySQL + Spring Security(JWT)**，前端 **Vue 3 + Vite 5 + Element Plus**。

## 环境要求

- JDK 1.8、Maven 3.6+
- MySQL 8.x，账号 `root`、**空密码**（与 `backend/src/main/resources/application.yml` 一致，可自行修改）
- Node.js 建议 **≥ 18**（已固定 Vite 5 以降低与 Node 版本冲突）

## 数据库初始化

1. 启动 MySQL，执行脚本：

   `backend/src/main/resources/db/schema.sql`

2. 将创建库 `legal_platform`、表及基础演示数据。

3. **（可选）更多测试数据**：在步骤 1 执行成功后，再执行  
   `backend/src/main/resources/db/seed_demo_data.sql`  
   会追加用户、普法内容、评论、论坛帖与回复、题库、竞赛成绩、备份日志等，便于答辩演示。

**默认账号（密码均为 `admin123`）**

| 账号        | 角色   | 说明 |
|-------------|--------|------|
| admin       | 管理员 | 全站管理 |
| teacher1    | 教师   | 题库、审核 |
| student1    | 学生   | 学习/论坛 |
| xiaozhang   | 学生   | **身份审核：待审核**（测顶栏提醒） |
| student2 / student3 | 学生 | 已通过审核 |
| teacher2    | 教师   | 已通过教师认证 |
| demo_admin2 | 管理员 | 额外管理员账号 |

## 启动后端

```bash
cd backend
mvn spring-boot:run
```

接口前缀：`http://127.0.0.1:8080/api`  
静态上传访问：`http://127.0.0.1:8080/api/uploads/...`

## 启动前端

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 Vite 提示的地址（默认 `http://127.0.0.1:5173`）。已配置代理：`/api` → 后端 `8080`。

生产构建：`npm run build`，将 `dist` 部署到 Nginx 等，并同样代理 `/api`。

## 前端：门户前台 vs 管理后台

同一套 Vue 工程内拆成两套界面，符合常见「前台网站 + 后台控制台」结构：

| 类型 | 路径 | 布局 | 主要用户 | 功能 |
|------|------|------|----------|------|
| **门户前台** | `/`、`/learn`、`/forum`、`/quiz`、`/profile` 等 | 顶栏导航 + 浅色门户 | 学生、访客、教师日常使用 | 浏览学习、论坛、竞赛、个人中心 |
| **管理后台** | `/admin` 下各子页（若依侧栏） | 左侧可折叠菜单 | **教师**、**管理员** | 数据统计、题库、发帖审核；管理员另有用户/备份、内容管理 |

- 教师/管理员登录后，前台顶栏有 **「管理后台」** 按钮，也可从用户下拉进入 **`/admin`**（默认进入数据统计）。
- 后台顶栏有 **「返回门户」** 回到前台首页。
- 旧书签兼容：`/stats`、`/teacher/questions`、`/mod/forum` 会自动重定向到对应 `/admin/*` 路由。

## 功能模块概览

1. **内容学习**：关键词检索、资讯/文章/视频（视频 URL + HTML5 播放）、浏览量统计。
2. **论坛**：发帖、回复、**审核通过后展示**；驳回后在「我的帖子」中修改再提交。
3. **数据统计**：教师/管理员查看内容浏览量汇总、竞赛参与与成绩指标。
4. **知识竞赛**：随机抽题、提交判分、排行榜；教师/管理员维护题库。
5. **个人中心**：注册登录、资料与密码、学生申请教师入驻、教师资料与内容投稿（待管理员审核）。
6. **系统管理（管理员）**：用户身份审核、教师入驻审核、用户列表、JSON 导出备份与日志。

## 目录说明

- `backend/`：Spring Boot 工程  
- `frontend/`：Vue 工程  
- `backend/src/main/resources/db/schema.sql`：数据库脚本  

若本机 Maven/Java 编译内存不足，可在 IDE 中打开工程运行 `LegalPlatformApplication`，或调整 `MAVEN_OPTS` / IDE 编译堆内存。
