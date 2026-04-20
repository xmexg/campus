package com.campus.legal.dto;

import lombok.Data;

@Data
public class QuizRankRow {
    private Long userId;
    private String nickname;
    private Integer bestScore;
    private Integer attemptCount;
}
