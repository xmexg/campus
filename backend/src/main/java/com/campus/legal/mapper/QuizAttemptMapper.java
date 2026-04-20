package com.campus.legal.mapper;

import com.campus.legal.dto.QuizAttemptAdminVO;
import com.campus.legal.dto.QuizRankRow;
import com.campus.legal.dto.ScoreRangeStatRow;
import com.campus.legal.dto.QuizStatSummary;
import com.campus.legal.entity.QuizAttempt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuizAttemptMapper {
    int insert(QuizAttempt a);

    List<QuizRankRow> ranking(@Param("limit") int limit);

    QuizStatSummary statsSummary();

    List<QuizAttemptAdminVO> listRecentAttempts(@Param("limit") int limit);

    List<ScoreRangeStatRow> statsScoreRange();
}
