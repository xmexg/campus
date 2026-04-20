package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttempt {
    private Long id;
    private Long userId;
    private Integer score;
    private Integer totalQuestions;
    private Integer correctCount;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
}
