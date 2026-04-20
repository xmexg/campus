package com.campus.legal.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttemptAdminVO {
    private Long id;
    private Long userId;
    private String nickname;
    private Integer score;
    private Integer totalQuestions;
    private Integer correctCount;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
}
