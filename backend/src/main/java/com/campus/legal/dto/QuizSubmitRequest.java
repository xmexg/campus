package com.campus.legal.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class QuizSubmitRequest {
    /** questionId -> 选项 A/B/C/D */
    @NotEmpty
    private Map<Long, String> answers;
    private Integer durationSeconds;
}
