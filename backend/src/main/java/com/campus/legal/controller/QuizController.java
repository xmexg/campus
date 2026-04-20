package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.QuizQuestionPublicVO;
import com.campus.legal.dto.QuizRankRow;
import com.campus.legal.dto.QuizStatSummary;
import com.campus.legal.dto.QuizSubmitRequest;
import com.campus.legal.entity.QuizAttempt;
import com.campus.legal.entity.QuizQuestion;
import com.campus.legal.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/questions")
    public ApiResult<List<QuizQuestionPublicVO>> questions(@RequestParam(defaultValue = "10") int limit) {
        return ApiResult.ok(quizService.pickQuestions(limit));
    }

    @PostMapping("/submit")
    public ApiResult<QuizAttempt> submit(@Validated @RequestBody QuizSubmitRequest req) {
        return ApiResult.ok(quizService.submit(req));
    }

    @GetMapping("/ranking")
    public ApiResult<List<QuizRankRow>> ranking(@RequestParam(defaultValue = "50") int limit) {
        return ApiResult.ok(quizService.ranking(limit));
    }

    @GetMapping("/teacher/questions")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResult<List<QuizQuestion>> teacherList() {
        return ApiResult.ok(quizService.listAllQuestions());
    }

    @PostMapping("/teacher/questions")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResult<Void> saveQ(@RequestBody QuizQuestion q) {
        quizService.saveQuestion(q);
        return ApiResult.ok();
    }

    @DeleteMapping("/teacher/questions/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResult<Void> delQ(@PathVariable Long id) {
        quizService.deleteQuestion(id);
        return ApiResult.ok();
    }

    @GetMapping("/stats/summary")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResult<QuizStatSummary> stats() {
        return ApiResult.ok(quizService.stats());
    }
}
