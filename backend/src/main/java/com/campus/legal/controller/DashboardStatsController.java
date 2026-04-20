package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardStatsController {

    private final StatsService statsService;

    @GetMapping({"/apistatuser", "/statuser"})
    public ApiResult<StatsService.UserStatResult> userStat() {
        return ApiResult.ok(statsService.userStat());
    }

    @GetMapping({"/apistatcontent", "/statcontent"})
    public ApiResult<StatsService.ContentStatResult> contentStat() {
        return ApiResult.ok(statsService.contentStat());
    }

    @GetMapping({"/apistat/fomum", "/apistat/forum", "/stat/forum"})
    public ApiResult<StatsService.ForumStatResult> forumStat() {
        return ApiResult.ok(statsService.forumStat());
    }

    @GetMapping({"/statquiz", "/apistatquiz", "/api/statquiz"})
    public ApiResult<StatsService.QuizStatResult> quizStat() {
        return ApiResult.ok(statsService.quizStat());
    }
}