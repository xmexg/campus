package com.campus.legal.dto;

import lombok.Data;

@Data
public class ContentTypeStatRow {
    private String contentType;
    private Long contentCount;
    private Long totalViews;
}