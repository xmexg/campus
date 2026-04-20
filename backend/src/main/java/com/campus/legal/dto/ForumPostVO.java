package com.campus.legal.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumPostVO {
    private Long id;
    private Long userId;
    private String authorName;
    private String title;
    private String body;
    private String auditStatus;
    private String auditRemark;
    private Integer viewCount;
    private LocalDateTime createdAt;
}
