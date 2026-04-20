package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.entity.EduContent;
import com.campus.legal.service.ContentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/content")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminContentController {

    private final ContentService contentService;

    @GetMapping("/list")
    public ApiResult<List<EduContent>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String contentType) {
        return ApiResult.ok(contentService.adminList(keyword, contentType));
    }

    @GetMapping("/{id}")
    public ApiResult<EduContent> get(@PathVariable Long id) {
        return ApiResult.ok(contentService.adminGet(id));
    }

    @PostMapping("/save")
    public ApiResult<Void> save(@RequestBody EduContent c) {
        contentService.adminSave(c);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        contentService.adminDelete(id);
        return ApiResult.ok();
    }

    @GetMapping("/submit/pending")
    public ApiResult<List<EduContent>> pending() {
        return ApiResult.ok(contentService.listPendingSubmits());
    }

    @PostMapping("/submit/{id}/audit")
    public ApiResult<Void> audit(@PathVariable Long id, @RequestBody AuditBody body) {
        contentService.auditSubmit(id, body.isApprove(), body.getRemark());
        return ApiResult.ok();
    }

    @PostMapping("/comment/{id}/hidden")
    public ApiResult<Void> hide(@PathVariable Long id, @RequestParam int hidden) {
        contentService.setCommentHidden(id, hidden);
        return ApiResult.ok();
    }

    @DeleteMapping("/comment/{id}")
    public ApiResult<Void> delComment(@PathVariable Long id) {
        contentService.deleteComment(id);
        return ApiResult.ok();
    }

    @Data
    public static class AuditBody {
        private boolean approve;
        private String remark;
    }
}
