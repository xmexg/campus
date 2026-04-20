package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.ForumPostVO;
import com.campus.legal.service.ForumService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum/mod")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class ForumModController {

    private final ForumService forumService;

    @GetMapping("/posts/all")
    public ApiResult<List<ForumPostVO>> listAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String auditStatus) {
        return ApiResult.ok(forumService.listAllForMod(keyword, auditStatus));
    }

    @PutMapping("/posts/{id}")
    public ApiResult<Void> updatePost(@PathVariable Long id, @RequestBody ModPostBody body) {
        forumService.adminUpdatePost(id, body.getTitle(), body.getBody());
        return ApiResult.ok();
    }

    @GetMapping("/posts/pending")
    public ApiResult<List<ForumPostVO>> pending() {
        return ApiResult.ok(forumService.listPendingAudit());
    }

    @PostMapping("/posts/{id}/audit")
    public ApiResult<Void> audit(@PathVariable Long id, @RequestBody AuditBody body) {
        forumService.auditPost(id, body.isApprove(), body.getRemark());
        return ApiResult.ok();
    }

    @DeleteMapping("/posts/{id}")
    public ApiResult<Void> deletePost(@PathVariable Long id) {
        forumService.modDeletePost(id);
        return ApiResult.ok();
    }

    @DeleteMapping("/replies/{id}")
    public ApiResult<Void> deleteReply(@PathVariable Long id) {
        forumService.modDeleteReply(id);
        return ApiResult.ok();
    }

    @Data
    public static class AuditBody {
        private boolean approve;
        private String remark;
    }

    @Data
    public static class ModPostBody {
        private String title;
        private String body;
    }
}
