package com.campus.legal.entity;

import com.campus.legal.dto.UserProfileVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentComment {
    private Long id;
    private Long contentId;
    private Long userId;
    private String body;
    private Integer hidden;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
}
