package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumPost {
    private Long id;
    private Long userId;
    private String title;
    private String body;
    private String auditStatus;
    private String auditRemark;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
