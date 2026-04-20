/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : legal_platform

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 21/04/2026 00:11:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for content_comment
-- ----------------------------
DROP TABLE IF EXISTS `content_comment`;
CREATE TABLE `content_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `body` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `hidden` tinyint(4) NOT NULL DEFAULT 0,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_content`(`content_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of content_comment
-- ----------------------------
INSERT INTO `content_comment` VALUES (1, 1, 3, '讲得清晰，希望多推送这类文章！', 0, '2026-03-20 16:57:47');
INSERT INTO `content_comment` VALUES (2, 2, 6, '已转发给室友一起学习。', 0, '2026-03-20 16:57:47');
INSERT INTO `content_comment` VALUES (3, 3, 10, 'SDFGHJKL', 0, '2026-04-20 14:55:10');
INSERT INTO `content_comment` VALUES (4, 12, 10, '支持', 0, '2026-04-20 21:24:51');
INSERT INTO `content_comment` VALUES (5, 11, 10, '支持', 0, '2026-04-20 21:29:03');
INSERT INTO `content_comment` VALUES (6, 11, 4, '11', 0, '2026-04-20 21:30:03');
INSERT INTO `content_comment` VALUES (7, 11, 10, '11', 0, '2026-04-20 21:38:21');
INSERT INTO `content_comment` VALUES (8, 11, 10, '11', 0, '2026-04-20 22:01:12');
INSERT INTO `content_comment` VALUES (9, 11, 10, '12', 0, '2026-04-20 22:01:38');
INSERT INTO `content_comment` VALUES (10, 11, 10, '1234567abc', 0, '2026-04-20 22:02:39');
INSERT INTO `content_comment` VALUES (11, 11, 10, '1111111', 0, '2026-04-20 22:03:57');
INSERT INTO `content_comment` VALUES (12, 11, 4, '1111', 0, '2026-04-20 22:04:16');
INSERT INTO `content_comment` VALUES (13, 11, 12, '555', 0, '2026-04-20 23:00:35');
INSERT INTO `content_comment` VALUES (14, 11, 12, '可以', 0, '2026-04-20 23:43:59');
INSERT INTO `content_comment` VALUES (15, 11, 10, 'qqqq', 0, '2026-04-20 23:44:34');

-- ----------------------------
-- Table structure for edu_content
-- ----------------------------
DROP TABLE IF EXISTS `edu_content`;
CREATE TABLE `edu_content`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ARTICLE,VIDEO,NEWS',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `summary` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `video_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cover_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `view_count` int(11) NOT NULL DEFAULT 0,
  `published` tinyint(4) NOT NULL DEFAULT 1,
  `submit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PUBLISHED' COMMENT '教师投稿:PENDING,已发布:PUBLISHED,退回:REJECTED',
  `submit_audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creator_id` bigint(20) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_type_pub`(`content_type` ASC, `published` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of edu_content
-- ----------------------------
INSERT INTO `edu_content` VALUES (1, 'NEWS', '宪法宣传周活动启动', '我校开展宪法学习系列活动', '为增强师生法治观念，学校于本周开展宪法宣传周……', NULL, NULL, '法治动态', 1, 1, 'PUBLISHED', NULL, 1, '2026-03-20 15:47:13', '2026-03-20 16:57:15');
INSERT INTO `edu_content` VALUES (2, 'ARTICLE', '大学生常见法律风险', '兼职、借贷与个人信息保护', '一、兼职合同……二、校园贷风险……', NULL, NULL, '普法文章', 0, 1, 'PUBLISHED', NULL, 1, '2026-03-20 15:47:13', '2026-03-20 15:47:13');
INSERT INTO `edu_content` VALUES (3, 'VIDEO', '民法典与生活', '视频普法：合同与侵权责任', NULL, 'https://www.w3schools.com/html/mov_bbb.mp4', NULL, '视频课程', 8, 1, 'PUBLISHED', NULL, 1, '2026-03-20 15:47:13', '2026-04-20 17:42:22');
INSERT INTO `edu_content` VALUES (4, 'NEWS', '「法治进校园」主题讲座本周三举行', '欢迎师生报名参加，现场可领取学习资料。', '学生处与法学院联合举办「法治进校园」专题讲座，邀请执业律师讲解校园贷、网络诈骗防范与维权途径。地点：学术报告厅；时间：周三 14:00-16:00。', NULL, NULL, '校园动态', 163, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-04-20 14:53:34');
INSERT INTO `edu_content` VALUES (5, 'ARTICLE', '劳动合同试用期，这些坑不要踩', '试用期内工资、解除与社保，一文说清。', '试用期最长不得超过法定期限；同一用人单位与同一劳动者只能约定一次试用期；试用期工资不得低于合同约定工资的 80% 且不得低于当地最低工资标准……（以下为示例正文，可替换为真实普法稿。）', NULL, NULL, '劳动权益', 91, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-04-20 14:53:13');
INSERT INTO `edu_content` VALUES (6, 'ARTICLE', '个人信息保护法与大学生日常', '快递、App 与社交账号中的个人信息。', '处理个人信息应当遵循合法、正当、必要原则。大学生在填写各类表格、注册 App 时，应注意隐私政策，谨慎授权位置、通讯录等敏感权限。', NULL, NULL, '数字法治', 72, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `edu_content` VALUES (7, 'NEWS', '国家宪法日｜线上知识竞答即将开放', '参与即有机会获得第二课堂学分。', '12 月 4 日前后，平台将开放宪法知识线上竞答，题目涵盖宪法条文、公民权利与义务等内容，敬请期待。', NULL, NULL, '法治动态', 201, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `edu_content` VALUES (8, 'VIDEO', '防电信诈骗：常见话术识别', '短视频示例，配合案例讲解。', NULL, 'https://www.w3schools.com/html/mov_bbb.mp4', NULL, '安全防范', 48, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-04-20 17:03:12');
INSERT INTO `edu_content` VALUES (9, 'ARTICLE', '宿舍矛盾与校规：如何理性维权', '协商、调解与申诉渠道。', '同学之间因作息、卫生等产生矛盾时，建议先通过辅导员、宿管协调；涉及处分不服的，可按学校规定申请复核或申诉。', NULL, NULL, '校园管理', 63, 1, 'PUBLISHED', NULL, 1, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `edu_content` VALUES (10, 'VIDEO', '333333333333', '333333333333', '333333333333333', 'https://msn.yidianzixun.com/zh-cn/video/webcontent/web-content/vi-AA20Wg0Z?vid=vi-AA20Wg0Z&locale=zh-CN&title=%E6%96%B0%E5%BB%BA%E6%A0%87%E7%AD%BE%E9%A1%B5&dsp=1&sp=%E5%BF%85%E5%BA%94&feed_dis=headingsonly&en_widget_reg=false&PC=CNNDDB&prerender=1&providerId=AA1JHler&ocid=msedgntp&pc=CNNDDB&cvid=69e5c4ac9dcf4e84b7a0b602a2f34157&ei=33', '', '', 3, 1, 'PUBLISHED', NULL, NULL, '2026-04-20 14:30:09', '2026-04-20 14:33:21');
INSERT INTO `edu_content` VALUES (11, 'VIDEO', '反对校园暴力', '11', '11', '/api/uploads/files/d825bde302a84fff98a7cc8a9e60349c.mp4', '', '校园', 44, 1, 'PUBLISHED', NULL, NULL, '2026-04-20 17:02:44', '2026-04-20 23:44:26');

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `body` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING,APPROVED,REJECTED',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `view_count` int(11) NOT NULL DEFAULT 0,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_audit`(`audit_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, 3, '求助：实习协议里竞业条款有效吗？', '下周要签三方和实习协议，里面有一段竞业限制，想请教老师和同学这种对在校生是否常见？', 'APPROVED', NULL, 89, '2026-03-20 16:57:47', '2026-04-19 15:49:47');
INSERT INTO `forum_post` VALUES (2, 2, '分享：我整理的「消费者权益」笔记', '包含七天无理由、举证责任等要点，欢迎补充。', 'APPROVED', NULL, 134, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `forum_post` VALUES (3, 8, '辩论：AI 生成内容著作权归谁？', '课堂讨论延伸，想听听大家观点。', 'APPROVED', NULL, 56, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `forum_post` VALUES (4, 7, '【待审核测试】失物招领：图书馆捡到 U 盘', '黑色金士顿，已交一楼服务台。', 'PENDING', NULL, 0, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `forum_post` VALUES (5, 6, '【未通过示例】测试广告帖', '请加微信 xxx 代购…', 'REJECTED', '内容与普法主题无关', 0, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `forum_post` VALUES (6, 10, '呃呃', '呃呃', 'PENDING', NULL, 0, '2026-04-19 15:49:19', '2026-04-19 15:49:19');
INSERT INTO `forum_post` VALUES (7, 10, '11', '123', 'APPROVED', NULL, 2, '2026-04-19 15:49:32', '2026-04-20 21:25:12');
INSERT INTO `forum_post` VALUES (8, 10, '11111', '2222222222222222222222', 'PENDING', NULL, 0, '2026-04-20 14:20:16', '2026-04-20 14:20:16');
INSERT INTO `forum_post` VALUES (9, 10, '222222222222', '222222222222222222222', 'PENDING', NULL, 0, '2026-04-20 14:20:33', '2026-04-20 14:20:33');
INSERT INTO `forum_post` VALUES (10, 4, '111111', '111111', 'APPROVED', NULL, 1, '2026-04-20 14:24:29', '2026-04-20 14:24:58');
INSERT INTO `forum_post` VALUES (11, 4, '2222', '2222', 'REJECTED', '不符合规定', 0, '2026-04-20 14:25:22', '2026-04-20 21:26:54');

-- ----------------------------
-- Table structure for forum_reply
-- ----------------------------
DROP TABLE IF EXISTS `forum_reply`;
CREATE TABLE `forum_reply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `body` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post`(`post_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum_reply
-- ----------------------------
INSERT INTO `forum_reply` VALUES (1, 1, 2, NULL, '一般竞业限制针对负有保密义务的员工，实习岗位要区分是否接触核心商业秘密，建议找学院就业指导老师看一眼。', '2026-03-20 16:57:47');
INSERT INTO `forum_reply` VALUES (2, 1, 7, NULL, '同问，我们专业也有很多同学遇到类似条款。', '2026-03-20 16:57:47');
INSERT INTO `forum_reply` VALUES (3, 2, 8, NULL, '笔记很系统，建议再补一节「网购纠纷管辖」。', '2026-03-20 16:57:47');

-- ----------------------------
-- Table structure for quiz_attempt
-- ----------------------------
DROP TABLE IF EXISTS `quiz_attempt`;
CREATE TABLE `quiz_attempt`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `score` int(11) NOT NULL,
  `total_questions` int(11) NOT NULL,
  `correct_count` int(11) NOT NULL,
  `duration_seconds` int(11) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_time`(`user_id` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_score`(`score` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quiz_attempt
-- ----------------------------
INSERT INTO `quiz_attempt` VALUES (1, 3, 80, 10, 8, 420, '2026-03-20 16:57:47');
INSERT INTO `quiz_attempt` VALUES (2, 6, 60, 10, 6, 600, '2026-03-20 16:57:47');
INSERT INTO `quiz_attempt` VALUES (3, 2, 100, 10, 10, 300, '2026-03-20 16:57:47');
INSERT INTO `quiz_attempt` VALUES (4, 4, 0, 2, 0, NULL, '2026-04-17 00:11:49');
INSERT INTO `quiz_attempt` VALUES (5, 4, 0, 2, 0, NULL, '2026-04-17 00:11:51');
INSERT INTO `quiz_attempt` VALUES (6, 4, 25, 4, 1, NULL, '2026-04-17 00:12:00');
INSERT INTO `quiz_attempt` VALUES (7, 4, 60, 10, 6, NULL, '2026-04-20 14:25:52');

-- ----------------------------
-- Table structure for quiz_question
-- ----------------------------
DROP TABLE IF EXISTS `quiz_question`;
CREATE TABLE `quiz_question`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_text` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `option_a` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `option_b` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `option_c` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `option_d` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `correct_option` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'A,B,C,D',
  `teacher_id` bigint(20) NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of quiz_question
-- ----------------------------
INSERT INTO `quiz_question` VALUES (1, '我国现行宪法是（）年通过的？', '1949', '1954', '1982', '2018', 'C', 2, '2026-03-20 15:47:13');
INSERT INTO `quiz_question` VALUES (2, '下列哪项属于民事行为能力人？', '8周岁以下的未成年人', '不能辨认自己行为的成年人', '16周岁以自己劳动收入为主要生活来源', '被宣告失踪的人', 'C', 2, '2026-03-20 15:47:13');
INSERT INTO `quiz_question` VALUES (4, '下列哪项属于《民法典》规定的民事法律行为无效情形？', '基于重大误解实施的', '违反法律、行政法规的强制性规定的', '显失公平的', '一方利用对方处于危困状态的', 'B', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (5, '我国最高国家权力机关是？', '国务院', '全国人民代表大会', '最高人民法院', '国家监察委员会', 'B', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (6, '劳动者提前解除劳动合同，正式员工一般应提前多少日以书面形式通知用人单位？', '3 日', '7 日', '30 日', '60 日', 'C', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (7, '下列属于可撤销婚姻的情形是？', '重婚', '有禁止结婚的亲属关系', '未到法定婚龄', '因胁迫结婚', 'D', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (8, '校园内打架斗殴致人轻伤，可能涉及？', '仅校规处分', '治安管理处罚或刑事责任', '民事赔偿与校规处分', 'B 和 C 均可能', 'D', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (9, '诉讼时效期间一般为？', '1 年', '2 年', '3 年', '5 年', 'C', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (10, '下列哪项不属于人格权？', '姓名权', '肖像权', '债权', '隐私权', 'C', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (11, '行政复议机关作出复议决定的期限，一般为受理之日起？', '15 日', '30 日', '60 日', '90 日', 'C', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (12, '已满多少周岁的人犯罪，应当负刑事责任？（刑法一般规定）', '14', '16', '18', '12', 'B', 2, '2026-03-20 16:57:47');
INSERT INTO `quiz_question` VALUES (13, '合同成立的一般要件不包括？', '要约与承诺', '当事人具有相应民事行为能力', '必须书面形式', '意思表示真实', 'C', 2, '2026-03-20 16:57:47');

-- ----------------------------
-- Table structure for sys_backup_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_backup_log`;
CREATE TABLE `sys_backup_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator_id` bigint(20) NULL DEFAULT NULL,
  `backup_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'FULL,EXPORT',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_backup_log
-- ----------------------------
INSERT INTO `sys_backup_log` VALUES (1, 1, 'EXPORT', '/data/backup/demo_export_20250320.zip', '演示导出', '2026-03-20 16:57:47');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'STUDENT,TEACHER,ADMIN',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1正常 0禁用',
  `identity_audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'APPROVED' COMMENT '注册后待审核:PENDING,通过:APPROVED,拒绝:REJECTED',
  `identity_audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teacher_cert_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teacher_audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'NONE' COMMENT 'NONE,PENDING,APPROVED,REJECTED',
  `teacher_audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `teacher_intro` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `teacher_material_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '系统管理员', NULL, 'ADMIN', 1, 'APPROVED', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 15:47:13', '2026-03-20 15:47:13');
INSERT INTO `sys_user` VALUES (2, 'teacher1', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '张老师', '13800000001', 'TEACHER', 1, 'APPROVED', NULL, NULL, 'APPROVED', NULL, NULL, NULL, NULL, '2026-03-20 15:47:13', '2026-03-20 15:47:13');
INSERT INTO `sys_user` VALUES (3, 'student1', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '李同学', '13800000002', 'STUDENT', 1, 'APPROVED', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 15:47:13', '2026-03-20 15:47:13');
INSERT INTO `sys_user` VALUES (4, '111111', '$2a$10$5Hgjg6AIiaiRdyIVqywti.6c52YQ2Df6SwbK8Pqjvgzp/tbMMM7XS', '小张', '19153952371', 'STUDENT', 1, 'APPROVED', '', '/api/uploads/files/c4e58079d945441dbb53ec30de83e436.png', 'NONE', NULL, '', NULL, '/api/uploads/files/03724bd1e1474c28aad57082a7729dfc.jpg', '2026-03-20 16:38:30', '2026-04-17 00:11:29');
INSERT INTO `sys_user` VALUES (5, 'xiaozhang', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '小张', '13900001001', 'STUDENT', 1, 'PENDING', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 16:57:47', '2026-04-17 00:11:15');
INSERT INTO `sys_user` VALUES (6, 'student2', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '王同学', '13900001002', 'STUDENT', 1, 'APPROVED', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `sys_user` VALUES (7, 'student3', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '赵同学', '13900001003', 'STUDENT', 1, 'APPROVED', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `sys_user` VALUES (8, 'teacher2', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '李老师', '13900002001', 'TEACHER', 1, 'APPROVED', NULL, NULL, 'APPROVED', NULL, '法学院讲师，主讲民法与劳动法', NULL, NULL, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `sys_user` VALUES (9, 'demo_admin2', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '周教务', '13900003001', 'ADMIN', 1, 'APPROVED', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-03-20 16:57:47', '2026-03-20 16:57:47');
INSERT INTO `sys_user` VALUES (10, '222222', '$2a$10$dPD0lKDdWWCBdu2sAqTpy.qMwi7z52sbpLW0893I0y9Scnt9Tg3uG', 'zaa', '19153952371', 'STUDENT', 1, 'APPROVED', '', NULL, 'NONE', NULL, NULL, NULL, '', '2026-04-17 00:14:41', '2026-04-20 23:44:21');
INSERT INTO `sys_user` VALUES (11, '44444', '$2a$10$Gxrfc/nXrnYJS.Ktd7NOluCrnhvU/u0nbtcUrq5t9lFLFM1CYNEaG', '4444', '', 'STUDENT', 1, 'PENDING', NULL, NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-04-20 14:23:01', '2026-04-20 14:23:01');
INSERT INTO `sys_user` VALUES (12, '123456', '$2a$10$iLp131VDhEaxAqVUaaNHRuMGjdjT.W74fPzgHgMTQG3/Fo1SZQY.O', '张', NULL, 'STUDENT', 1, 'APPROVED', '', NULL, 'NONE', NULL, NULL, NULL, NULL, '2026-04-20 22:59:46', '2026-04-20 23:00:13');

SET FOREIGN_KEY_CHECKS = 1;
