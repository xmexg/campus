package com.campus.legal.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuizStatSummary {
    private Long totalAttempts;
    private BigDecimal avgScore;
    private BigDecimal avgCorrectRate;
    private Long distinctUsers;
}
