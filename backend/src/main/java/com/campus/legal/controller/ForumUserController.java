package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.ForumPostVO;
import com.campus.legal.service.ForumService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumUserController {

    private final ForumService forumService;

    @GetMapping("/my/posts")
    public ApiResult<List<ForumPostVO>> myPosts() {
        return ApiResult.ok(forumService.myPosts());
    }

    /** 本人查看任意状态帖子（用于修改被拒帖子） */
    @GetMapping("/posts/{id}/mine")
    public ApiResult<ForumPostVO> myPostDetail(@PathVariable Long id) {
        return ApiResult.ok(forumService.myPostDetail(id));
    }

    @PostMapping("/posts")
    public ApiResult<Long> create(@RequestBody PostBody body) {
        return ApiResult.ok(forumService.createPost(body.getTitle(), body.getBody()));
    }

    @PutMapping("/posts/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody PostBody body) {
        forumService.updatePost(id, body.getTitle(), body.getBody());
        return ApiResult.ok();
    }

    @PostMapping("/posts/{postId}/replies")
    public ApiResult<Void> reply(@PathVariable Long postId, @RequestBody ReplyBody body) {
        forumService.reply(postId, body.getBody(), body.getParentId());
        return ApiResult.ok();
    }

    @Data
    public static class PostBody {
        private String title;
        private String body;
    }

    @Data
    public static class ReplyBody {
        private String body;
        private Long parentId;
    }
}
