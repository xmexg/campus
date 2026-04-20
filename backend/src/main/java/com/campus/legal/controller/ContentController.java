package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.entity.ContentComment;
import com.campus.legal.entity.EduContent;
import com.campus.legal.service.ContentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/search")
    public ApiResult<List<EduContent>> search(@RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) String contentType) {
        return ApiResult.ok(contentService.searchPublic(keyword, contentType));
    }

    @GetMapping("/{id}")
    public ApiResult<EduContent> detail(@PathVariable Long id) {
        return ApiResult.ok(contentService.getPublic(id));
    }

    @PostMapping("/{id}/view")
    public ApiResult<Void> view(@PathVariable Long id) {
        contentService.addView(id);
        return ApiResult.ok();
    }

    @GetMapping("/{id}/comments")
    public ApiResult<List<ContentComment>> comments(@PathVariable Long id) {
        List<ContentComment> comments_origin = contentService.listComments(id);
        return ApiResult.ok(comments_origin);
    }

    @PostMapping("/{id}/comments")
    public ApiResult<Void> addComment(@PathVariable Long id, @RequestBody CommentBody body) {
        contentService.addComment(id, body.getBody());
        return ApiResult.ok();
    }

    @PostMapping("/teacher/submit")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResult<Long> teacherSubmit(@RequestBody EduContent c) {
        return ApiResult.ok(contentService.teacherSubmit(c));
    }

    @Data
    public static class CommentBody {
        private String body;
    }
}
