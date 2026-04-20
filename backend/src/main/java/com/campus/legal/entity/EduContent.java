package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EduContent {
    private Long id;
    /** ARTICLE, VIDEO, NEWS */
    private String contentType;
    private String title;
    private String summary;
    private String body;
    private String videoUrl;
    private String coverUrl;
    private String category;
    private Integer viewCount;
    private Integer published;
    private String submitStatus;
    private String submitAuditRemark;
    private Long creatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
