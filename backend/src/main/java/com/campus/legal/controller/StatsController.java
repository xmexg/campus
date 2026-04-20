package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/overview")
    public ApiResult<StatsService.StatsBundle> overview() {
        return ApiResult.ok(statsService.overview());
    }
}
