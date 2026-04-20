-- =============================================================================
-- 高校普法平台 · 演示/测试数据（在 schema.sql 执行成功后运行）
-- 密码说明：下列新增账号与 schema 中一致，均为明文 admin123 的 BCrypt 哈希
--   $2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS
-- 执行：mysql -uroot -p legal_platform < seed_demo_data.sql
-- =============================================================================

USE legal_platform;

SET NAMES utf8mb4;

-- 与 schema.sql 中 admin123 相同的 BCrypt（便于统一演示密码）
SET @pwd_admin123 = '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS';

-- ---------------------------------------------------------------------------
-- 补充用户（若用户名已存在会报错，可先删除对应行或跳过）
-- ---------------------------------------------------------------------------
INSERT INTO sys_user (username, password, nickname, phone, role, status, identity_audit_status, teacher_audit_status, teacher_intro)
VALUES
  ('xiaozhang', @pwd_admin123, '小张', '13900001001', 'STUDENT', 1, 'PENDING', 'NONE', NULL),
  ('student2', @pwd_admin123, '王同学', '13900001002', 'STUDENT', 1, 'APPROVED', 'NONE', NULL),
  ('student3', @pwd_admin123, '赵同学', '13900001003', 'STUDENT', 1, 'APPROVED', 'NONE', NULL),
  ('teacher2', @pwd_admin123, '李老师', '13900002001', 'TEACHER', 1, 'APPROVED', 'APPROVED', '法学院讲师，主讲民法与劳动法'),
  ('demo_admin2', @pwd_admin123, '周教务', '13900003001', 'ADMIN', 1, 'APPROVED', 'NONE', NULL);

-- ---------------------------------------------------------------------------
-- 普法内容（creator_id 指向管理员 1，可按需改）
-- ---------------------------------------------------------------------------
INSERT INTO edu_content (content_type, title, summary, body, video_url, category, view_count, published, submit_status, creator_id) VALUES
('NEWS', '「法治进校园」主题讲座本周三举行', '欢迎师生报名参加，现场可领取学习资料。',
 '学生处与法学院联合举办「法治进校园」专题讲座，邀请执业律师讲解校园贷、网络诈骗防范与维权途径。地点：学术报告厅；时间：周三 14:00-16:00。',
 NULL, '校园动态', 156, 1, 'PUBLISHED', 1),
('ARTICLE', '劳动合同试用期，这些坑不要踩', '试用期内工资、解除与社保，一文说清。',
 '试用期最长不得超过法定期限；同一用人单位与同一劳动者只能约定一次试用期；试用期工资不得低于合同约定工资的 80% 且不得低于当地最低工资标准……（以下为示例正文，可替换为真实普法稿。）',
 NULL, '劳动权益', 89, 1, 'PUBLISHED', 1),
('ARTICLE', '个人信息保护法与大学生日常', '快递、App 与社交账号中的个人信息。',
 '处理个人信息应当遵循合法、正当、必要原则。大学生在填写各类表格、注册 App 时，应注意隐私政策，谨慎授权位置、通讯录等敏感权限。',
 NULL, '数字法治', 72, 1, 'PUBLISHED', 1),
('NEWS', '国家宪法日｜线上知识竞答即将开放', '参与即有机会获得第二课堂学分。',
 '12 月 4 日前后，平台将开放宪法知识线上竞答，题目涵盖宪法条文、公民权利与义务等内容，敬请期待。',
 NULL, '法治动态', 201, 1, 'PUBLISHED', 1),
('VIDEO', '防电信诈骗：常见话术识别', '短视频示例，配合案例讲解。',
 NULL, 'https://www.w3schools.com/html/mov_bbb.mp4', '安全防范', 45, 1, 'PUBLISHED', 1),
('ARTICLE', '宿舍矛盾与校规：如何理性维权', '协商、调解与申诉渠道。',
 '同学之间因作息、卫生等产生矛盾时，建议先通过辅导员、宿管协调；涉及处分不服的，可按学校规定申请复核或申诉。',
 NULL, '校园管理', 63, 1, 'PUBLISHED', 1);

-- ---------------------------------------------------------------------------
-- 内容评论（挂在已有内容 id=1 上；若你库中 id 不同请手工改 content_id）
-- ---------------------------------------------------------------------------
INSERT INTO content_comment (content_id, user_id, body, hidden)
SELECT c.id, u.id, '讲得清晰，希望多推送这类文章！', 0
FROM edu_content c, sys_user u
WHERE c.title = '宪法宣传周活动启动' AND u.username = 'student1' LIMIT 1;

INSERT INTO content_comment (content_id, user_id, body, hidden)
SELECT c.id, u.id, '已转发给室友一起学习。', 0
FROM edu_content c, sys_user u
WHERE c.title = '大学生常见法律风险' AND u.username = 'student2' LIMIT 1;

-- ---------------------------------------------------------------------------
-- 论坛帖子：已通过 / 待审核 / 未通过 各若干，便于测审核与列表
-- ---------------------------------------------------------------------------
INSERT INTO forum_post (user_id, title, body, audit_status, audit_remark, view_count)
SELECT u.id, '求助：实习协议里竞业条款有效吗？', '下周要签三方和实习协议，里面有一段竞业限制，想请教老师和同学这种对在校生是否常见？', 'APPROVED', NULL, 88
FROM sys_user u WHERE u.username = 'student1' LIMIT 1;

INSERT INTO forum_post (user_id, title, body, audit_status, audit_remark, view_count)
SELECT u.id, '分享：我整理的「消费者权益」笔记', '包含七天无理由、举证责任等要点，欢迎补充。', 'APPROVED', NULL, 134
FROM sys_user u WHERE u.username = 'teacher1' LIMIT 1;

INSERT INTO forum_post (user_id, title, body, audit_status, audit_remark, view_count)
SELECT u.id, '辩论：AI 生成内容著作权归谁？', '课堂讨论延伸，想听听大家观点。', 'APPROVED', NULL, 56
FROM sys_user u WHERE u.username = 'teacher2' LIMIT 1;

INSERT INTO forum_post (user_id, title, body, audit_status, audit_remark, view_count)
SELECT u.id, '【待审核测试】失物招领：图书馆捡到 U 盘', '黑色金士顿，已交一楼服务台。', 'PENDING', NULL, 0
FROM sys_user u WHERE u.username = 'student3' LIMIT 1;

INSERT INTO forum_post (user_id, title, body, audit_status, audit_remark, view_count)
SELECT u.id, '【未通过示例】测试广告帖', '请加微信 xxx 代购…', 'REJECTED', '内容与普法主题无关', 0
FROM sys_user u WHERE u.username = 'student2' LIMIT 1;

-- ---------------------------------------------------------------------------
-- 论坛回复（对「最新一条已通过」的帖子批量回复不方便，按标题取 post id）
-- ---------------------------------------------------------------------------
INSERT INTO forum_reply (post_id, user_id, parent_id, body)
SELECT p.id, u.id, NULL, '一般竞业限制针对负有保密义务的员工，实习岗位要区分是否接触核心商业秘密，建议找学院就业指导老师看一眼。'
FROM forum_post p, sys_user u
WHERE p.title = '求助：实习协议里竞业条款有效吗？' AND u.username = 'teacher1' LIMIT 1;

INSERT INTO forum_reply (post_id, user_id, parent_id, body)
SELECT p.id, u.id, NULL, '同问，我们专业也有很多同学遇到类似条款。'
FROM forum_post p, sys_user u
WHERE p.title = '求助：实习协议里竞业条款有效吗？' AND u.username = 'student3' LIMIT 1;

INSERT INTO forum_reply (post_id, user_id, parent_id, body)
SELECT p.id, u.id, NULL, '笔记很系统，建议再补一节「网购纠纷管辖」。'
FROM forum_post p, sys_user u
WHERE p.title = '分享：我整理的「消费者权益」笔记' AND u.username = 'teacher2' LIMIT 1;

-- ---------------------------------------------------------------------------
-- 题库（随机抽题需要足够数量；teacher_id 指向 teacher1）
-- ---------------------------------------------------------------------------
INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '下列哪项属于《民法典》规定的民事法律行为无效情形？', '基于重大误解实施的', '违反法律、行政法规的强制性规定的', '显失公平的', '一方利用对方处于危困状态的', 'B', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '我国最高国家权力机关是？', '国务院', '全国人民代表大会', '最高人民法院', '国家监察委员会', 'B', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '劳动者提前解除劳动合同，正式员工一般应提前多少日以书面形式通知用人单位？', '3 日', '7 日', '30 日', '60 日', 'C', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '下列属于可撤销婚姻的情形是？', '重婚', '有禁止结婚的亲属关系', '未到法定婚龄', '因胁迫结婚', 'D', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '校园内打架斗殴致人轻伤，可能涉及？', '仅校规处分', '治安管理处罚或刑事责任', '民事赔偿与校规处分', 'B 和 C 均可能', 'D', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '诉讼时效期间一般为？', '1 年', '2 年', '3 年', '5 年', 'C', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '下列哪项不属于人格权？', '姓名权', '肖像权', '债权', '隐私权', 'C', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '行政复议机关作出复议决定的期限，一般为受理之日起？', '15 日', '30 日', '60 日', '90 日', 'C', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '已满多少周岁的人犯罪，应当负刑事责任？（刑法一般规定）', '14', '16', '18', '12', 'B', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id)
SELECT '合同成立的一般要件不包括？', '要约与承诺', '当事人具有相应民事行为能力', '必须书面形式', '意思表示真实', 'C', id FROM sys_user WHERE username = 'teacher1' LIMIT 1;

-- ---------------------------------------------------------------------------
-- 竞赛成绩记录（排行榜展示用）
-- ---------------------------------------------------------------------------
INSERT INTO quiz_attempt (user_id, score, total_questions, correct_count, duration_seconds)
SELECT id, 80, 10, 8, 420 FROM sys_user WHERE username = 'student1' LIMIT 1;

INSERT INTO quiz_attempt (user_id, score, total_questions, correct_count, duration_seconds)
SELECT id, 60, 10, 6, 600 FROM sys_user WHERE username = 'student2' LIMIT 1;

INSERT INTO quiz_attempt (user_id, score, total_questions, correct_count, duration_seconds)
SELECT id, 100, 10, 10, 300 FROM sys_user WHERE username = 'teacher1' LIMIT 1;

-- 备份日志示例（管理端「备份记录」）
INSERT INTO sys_backup_log (operator_id, backup_type, file_path, remark)
SELECT id, 'EXPORT', '/data/backup/demo_export_20250320.zip', '演示导出' FROM sys_user WHERE username = 'admin' LIMIT 1;
