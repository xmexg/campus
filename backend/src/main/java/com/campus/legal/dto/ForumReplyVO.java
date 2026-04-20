package com.campus.legal.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumReplyVO {
    private Long id;
    private Long postId;
    private Long userId;
    private String authorName;
    private Long parentId;
    private String body;
    private LocalDateTime createdAt;
}
