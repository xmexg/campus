package com.campus.legal.service;

import com.campus.legal.dto.ContentViewStatRow;
import com.campus.legal.dto.QuizStatSummary;
import com.campus.legal.mapper.EduContentMapper;
import com.campus.legal.mapper.QuizAttemptMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final EduContentMapper eduContentMapper;
    private final QuizAttemptMapper quizAttemptMapper;

    public StatsBundle overview() {
        StatsBundle b = new StatsBundle();
        b.setContentViews(eduContentMapper.statsViewsByType());
        b.setQuiz(quizAttemptMapper.statsSummary());
        return b;
    }

    @Data
    public static class StatsBundle {
        private List<ContentViewStatRow> contentViews;
        private QuizStatSummary quiz;
    }

    public Map<String, Object> simpleDashboard() {
        Map<String, Object> m = new HashMap<>();
        m.put("contentViews", eduContentMapper.statsViewsByType());
        m.put("quiz", quizAttemptMapper.statsSummary());
        return m;
    }
}
