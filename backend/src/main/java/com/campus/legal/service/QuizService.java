package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.dto.QuizQuestionPublicVO;
import com.campus.legal.dto.QuizRankRow;
import com.campus.legal.dto.QuizStatSummary;
import com.campus.legal.dto.QuizSubmitRequest;
import com.campus.legal.entity.QuizAttempt;
import com.campus.legal.entity.QuizQuestion;
import com.campus.legal.mapper.QuizAttemptMapper;
import com.campus.legal.mapper.QuizQuestionMapper;
import com.campus.legal.security.LoginUser;
import com.campus.legal.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizQuestionMapper quizQuestionMapper;
    private final QuizAttemptMapper quizAttemptMapper;
    private final UserService userService;

    public List<QuizQuestionPublicVO> pickQuestions(int limit) {
        List<QuizQuestion> list = quizQuestionMapper.randomPick(Math.max(1, Math.min(limit, 50)));
        return list.stream().map(QuizQuestionPublicVO::from).collect(Collectors.toList());
    }

    @Transactional
    public QuizAttempt submit(QuizSubmitRequest req) {
        LoginUser lu = SecurityUtils.currentUser();
        if (lu == null) {
            throw new BusinessException(401, "请先登录");
        }
        userService.requireIdentityApproved(lu);
        Map<Long, String> answers = req.getAnswers();
        if (answers == null || answers.isEmpty()) {
            throw new BusinessException("答案不能为空");
        }
        int correct = 0;
        int total = answers.size();
        for (Map.Entry<Long, String> e : answers.entrySet()) {
            QuizQuestion q = quizQuestionMapper.findById(e.getKey());
            if (q == null) {
                continue;
            }
            String ans = e.getValue() == null ? "" : e.getValue().trim().toUpperCase();
            if (ans.equals(q.getCorrectOption().trim().toUpperCase())) {
                correct++;
            }
        }
        int score = total == 0 ? 0 : (int) Math.round(correct * 100.0 / total);
        QuizAttempt a = new QuizAttempt();
        a.setUserId(lu.getUserId());
        a.setScore(score);
        a.setTotalQuestions(total);
        a.setCorrectCount(correct);
        a.setDurationSeconds(req.getDurationSeconds());
        quizAttemptMapper.insert(a);
        return a;
    }

    public List<QuizRankRow> ranking(int limit) {
        return quizAttemptMapper.ranking(Math.min(Math.max(limit, 1), 200));
    }

    public QuizStatSummary stats() {
        return quizAttemptMapper.statsSummary();
    }

    public List<QuizQuestion> listAllQuestions() {
        return quizQuestionMapper.listAll();
    }

    @Transactional
    public void saveQuestion(QuizQuestion q) {
        Long uid = SecurityUtils.requireUserId();
        if (q.getId() == null) {
            q.setTeacherId(uid);
            quizQuestionMapper.insert(q);
        } else {
            quizQuestionMapper.update(q);
        }
    }

    @Transactional
    public void deleteQuestion(Long id) {
        quizQuestionMapper.delete(id);
    }
}
