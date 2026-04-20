-- 高校普法平台 MySQL 初始化脚本（MySQL 8+）
CREATE DATABASE IF NOT EXISTS legal_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE legal_platform;

DROP TABLE IF EXISTS quiz_attempt;
DROP TABLE IF EXISTS quiz_question;
DROP TABLE IF EXISTS forum_reply;
DROP TABLE IF EXISTS forum_post;
DROP TABLE IF EXISTS content_comment;
DROP TABLE IF EXISTS edu_content;
DROP TABLE IF EXISTS sys_backup_log;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    nickname VARCHAR(64),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL COMMENT 'STUDENT,TEACHER,ADMIN',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1正常 0禁用',
    identity_audit_status VARCHAR(20) DEFAULT 'APPROVED' COMMENT '注册后待审核:PENDING,通过:APPROVED,拒绝:REJECTED',
    identity_audit_remark VARCHAR(500),
    teacher_cert_url VARCHAR(500),
    teacher_audit_status VARCHAR(20) DEFAULT 'NONE' COMMENT 'NONE,PENDING,APPROVED,REJECTED',
    teacher_audit_remark VARCHAR(500),
    teacher_intro TEXT,
    teacher_material_url VARCHAR(500),
    avatar_url VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE edu_content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content_type VARCHAR(20) NOT NULL COMMENT 'ARTICLE,VIDEO,NEWS',
    title VARCHAR(256) NOT NULL,
    summary VARCHAR(1000),
    body MEDIUMTEXT,
    video_url VARCHAR(500),
    cover_url VARCHAR(500),
    category VARCHAR(64),
    view_count INT NOT NULL DEFAULT 0,
    published TINYINT NOT NULL DEFAULT 1,
    submit_status VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT '教师投稿:PENDING,已发布:PUBLISHED,退回:REJECTED',
    submit_audit_remark VARCHAR(500),
    creator_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type_pub (content_type, published),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE content_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    body VARCHAR(2000) NOT NULL,
    hidden TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_content (content_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE forum_post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(256) NOT NULL,
    body MEDIUMTEXT NOT NULL,
    audit_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING,APPROVED,REJECTED',
    audit_remark VARCHAR(500),
    view_count INT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_audit (audit_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE forum_reply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT,
    body VARCHAR(2000) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_post (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE quiz_question (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_text VARCHAR(1000) NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option CHAR(1) NOT NULL COMMENT 'A,B,C,D',
    teacher_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE quiz_attempt (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    correct_count INT NOT NULL,
    duration_seconds INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_time (user_id, created_at),
    INDEX idx_score (score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_backup_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    operator_id BIGINT,
    backup_type VARCHAR(32) NOT NULL COMMENT 'FULL,EXPORT',
    file_path VARCHAR(500),
    remark VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 默认管理员 admin / admin123 （BCrypt）
INSERT INTO sys_user (username, password, nickname, role, status, identity_audit_status, teacher_audit_status)
VALUES ('admin', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '系统管理员', 'ADMIN', 1, 'APPROVED', 'NONE');

-- 示例教师、学生（密码均为 admin123）
INSERT INTO sys_user (username, password, nickname, phone, role, status, identity_audit_status, teacher_audit_status)
VALUES 
('teacher1', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '张老师', '13800000001', 'TEACHER', 1, 'APPROVED', 'APPROVED'),
('student1', '$2a$10$enxeGC/KYXOsLl0dO7SXU.zYpWj5gJY7YcAYGMYLs.RugUhcXEHPS', '李同学', '13800000002', 'STUDENT', 1, 'APPROVED', 'NONE');

INSERT INTO edu_content (content_type, title, summary, body, category, published, creator_id) VALUES
('NEWS', '宪法宣传周活动启动', '我校开展宪法学习系列活动', '为增强师生法治观念，学校于本周开展宪法宣传周……', '法治动态', 1, 1),
('ARTICLE', '大学生常见法律风险', '兼职、借贷与个人信息保护', '一、兼职合同……二、校园贷风险……', '普法文章', 1, 1),
('VIDEO', '民法典与生活', '视频普法：合同与侵权责任', NULL, '视频课程', 1, 1);

UPDATE edu_content SET video_url = 'https://www.w3schools.com/html/mov_bbb.mp4' WHERE title = '民法典与生活';

INSERT INTO quiz_question (question_text, option_a, option_b, option_c, option_d, correct_option, teacher_id) VALUES
('我国现行宪法是（）年通过的？', '1949', '1954', '1982', '2018', 'C', 2),
('下列哪项属于民事行为能力人？', '8周岁以下的未成年人', '不能辨认自己行为的成年人', '16周岁以自己劳动收入为主要生活来源', '被宣告失踪的人', 'C', 2);
