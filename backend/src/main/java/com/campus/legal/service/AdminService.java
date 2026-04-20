package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.dto.QuizAttemptAdminVO;
import com.campus.legal.entity.SysBackupLog;
import com.campus.legal.entity.SysUser;
import com.campus.legal.mapper.QuizAttemptMapper;
import com.campus.legal.mapper.SysBackupLogMapper;
import com.campus.legal.mapper.SysUserMapper;
import com.campus.legal.security.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final SysUserMapper sysUserMapper;
    private final SysBackupLogMapper sysBackupLogMapper;
    private final QuizAttemptMapper quizAttemptMapper;
    private final ObjectMapper objectMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<SysUser> pendingIdentity() {
        return sysUserMapper.listPendingIdentity();
    }

    @Transactional
    public void auditIdentity(Long userId, boolean approve, String remark) {
        SysUser u = sysUserMapper.findById(userId);
        if (u == null) {
            throw new BusinessException("用户不存在");
        }
        sysUserMapper.updateIdentityAudit(userId, approve ? "APPROVED" : "REJECTED", remark);
    }

    public List<SysUser> pendingTeacher() {
        return sysUserMapper.listPendingTeacher();
    }

    @Transactional
    public void auditTeacher(Long userId, boolean approve, String remark) {
        SysUser u = sysUserMapper.findById(userId);
        if (u == null) {
            throw new BusinessException("用户不存在");
        }
        if (approve) {
            sysUserMapper.updateTeacherAudit(userId, "APPROVED", remark, "TEACHER");
        } else {
            sysUserMapper.updateTeacherAudit(userId, "REJECTED", remark, "STUDENT");
        }
    }

    public List<SysUser> users(String role, String keyword) {
        return sysUserMapper.listAll(role, keyword);
    }

    @Transactional
    public void updateUserByAdmin(Long id, SysUser patch) {
        SysUser u = sysUserMapper.findById(id);
        if (u == null) {
            throw new BusinessException("用户不存在");
        }
        if ("ADMIN".equals(u.getRole()) && patch.getRole() != null && !"ADMIN".equals(patch.getRole())) {
            long adminCount = sysUserMapper.listAll("ADMIN", null).size();
            if (adminCount <= 1) {
                throw new BusinessException("至少保留一名管理员");
            }
        }
        u.setNickname(patch.getNickname() != null ? patch.getNickname() : u.getNickname());
        u.setPhone(patch.getPhone());
        u.setRole(patch.getRole() != null ? patch.getRole() : u.getRole());
        u.setStatus(patch.getStatus() != null ? patch.getStatus() : u.getStatus());
        u.setAvatarUrl(patch.getAvatarUrl());
        sysUserMapper.updateByAdmin(u);
    }

    /** 导出核心表数据为 JSON 文件（演示用“全量备份”） */
    @Transactional(readOnly = true)
    public String exportDataJson() throws Exception {
        Long op = SecurityUtils.requireUserId();
        Path dir = Paths.get(uploadDir, "backup");
        Files.createDirectories(dir);
        String name = "export-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".json";
        Path file = dir.resolve(name);

        Map<String, Object> dump = new HashMap<>();
        dump.put("exportedAt", LocalDateTime.now().toString());
        List<SysUser> users = sysUserMapper.listAll(null, null);
        users.forEach(u -> u.setPassword(null));
        dump.put("users", users);

        byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dump).getBytes(StandardCharsets.UTF_8);
        Files.write(file, bytes);

        SysBackupLog log = new SysBackupLog();
        log.setOperatorId(op);
        log.setBackupType("EXPORT");
        log.setFilePath(file.toAbsolutePath().toString());
        log.setRemark("JSON导出");
        sysBackupLogMapper.insert(log);

        return "/uploads/backup/" + name;
    }

    public List<SysBackupLog> backupLogs() {
        return sysBackupLogMapper.listRecent(50);
    }

    public List<QuizAttemptAdminVO> quizAttempts(int limit) {
        int n = Math.min(Math.max(limit, 1), 500);
        return quizAttemptMapper.listRecentAttempts(n);
    }
}
