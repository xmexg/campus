package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.AuthResponse;
import com.campus.legal.dto.LoginRequest;
import com.campus.legal.dto.RegisterRequest;
import com.campus.legal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResult<AuthResponse> register(@Validated @RequestBody RegisterRequest req) {
        return ApiResult.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ApiResult<AuthResponse> login(@Validated @RequestBody LoginRequest req) {
        return ApiResult.ok(authService.login(req));
    }
}
