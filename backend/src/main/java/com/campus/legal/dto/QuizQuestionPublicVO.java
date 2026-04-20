package com.campus.legal.dto;

import com.campus.legal.entity.QuizQuestion;
import lombok.Data;

@Data
public class QuizQuestionPublicVO {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    public static QuizQuestionPublicVO from(QuizQuestion q) {
        QuizQuestionPublicVO v = new QuizQuestionPublicVO();
        v.setId(q.getId());
        v.setQuestionText(q.getQuestionText());
        v.setOptionA(q.getOptionA());
        v.setOptionB(q.getOptionB());
        v.setOptionC(q.getOptionC());
        v.setOptionD(q.getOptionD());
        return v;
    }
}
