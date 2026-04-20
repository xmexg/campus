package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.QuizAttemptAdminVO;
import com.campus.legal.entity.SysBackupLog;
import com.campus.legal.entity.SysUser;
import com.campus.legal.service.AdminService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping("/identity/pending")
    public ApiResult<List<SysUser>> pendingIdentity() {
        return ApiResult.ok(adminService.pendingIdentity());
    }

    @PostMapping("/identity/{userId}/audit")
    public ApiResult<Void> auditIdentity(@PathVariable Long userId, @RequestBody AuditBody body) {
        adminService.auditIdentity(userId, body.isApprove(), body.getRemark());
        return ApiResult.ok();
    }

    @GetMapping("/teacher/pending")
    public ApiResult<List<SysUser>> pendingTeacher() {
        return ApiResult.ok(adminService.pendingTeacher());
    }

    @PostMapping("/teacher/{userId}/audit")
    public ApiResult<Void> auditTeacher(@PathVariable Long userId, @RequestBody AuditBody body) {
        adminService.auditTeacher(userId, body.isApprove(), body.getRemark());
        return ApiResult.ok();
    }

    @GetMapping("/users")
    public ApiResult<List<SysUser>> users(@RequestParam(required = false) String role,
                                          @RequestParam(required = false) String keyword) {
        return ApiResult.ok(adminService.users(role, keyword));
    }

    @PutMapping("/users/{id}")
    public ApiResult<Void> updateUser(@PathVariable Long id, @RequestBody SysUser body) {
        adminService.updateUserByAdmin(id, body);
        return ApiResult.ok();
    }

    @GetMapping("/quiz/attempts")
    public ApiResult<java.util.List<QuizAttemptAdminVO>> quizAttempts(@RequestParam(defaultValue = "100") int limit) {
        return ApiResult.ok(adminService.quizAttempts(limit));
    }

    @PostMapping("/backup/export")
    public ApiResult<String> exportBackup() throws Exception {
        return ApiResult.ok(adminService.exportDataJson());
    }

    @GetMapping("/backup/logs")
    public ApiResult<List<SysBackupLog>> logs() {
        return ApiResult.ok(adminService.backupLogs());
    }

    @Data
    public static class AuditBody {
        private boolean approve;
        private String remark;
    }
}
