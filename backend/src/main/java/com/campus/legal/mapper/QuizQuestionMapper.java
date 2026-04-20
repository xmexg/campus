package com.campus.legal.mapper;

import com.campus.legal.entity.QuizQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuizQuestionMapper {
    QuizQuestion findById(@Param("id") Long id);

    int insert(QuizQuestion q);

    int update(QuizQuestion q);

    int delete(@Param("id") Long id);

    List<QuizQuestion> listAll();

    List<QuizQuestion> randomPick(@Param("limit") int limit);
}
