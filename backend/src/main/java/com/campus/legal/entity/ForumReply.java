package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumReply {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String body;
    private LocalDateTime createdAt;
}
