package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.ForumPostVO;
import com.campus.legal.dto.ForumReplyVO;
import com.campus.legal.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum/public")
@RequiredArgsConstructor
public class ForumPublicController {

    private final ForumService forumService;

    @GetMapping("/posts")
    public ApiResult<List<ForumPostVO>> list(@RequestParam(required = false) String keyword) {
        return ApiResult.ok(forumService.listPublic(keyword));
    }

    @GetMapping("/posts/{id}")
    public ApiResult<ForumPostVO> detail(@PathVariable Long id) {
        return ApiResult.ok(forumService.getPublicPost(id));
    }

    @GetMapping("/posts/{id}/replies")
    public ApiResult<List<ForumReplyVO>> replies(@PathVariable Long id) {
        return ApiResult.ok(forumService.listReplies(id));
    }
}
