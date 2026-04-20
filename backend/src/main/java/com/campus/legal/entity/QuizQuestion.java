package com.campus.legal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizQuestion {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private Long teacherId;
    private LocalDateTime createdAt;
}
