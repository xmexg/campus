package com.campus.legal.controller;

import com.campus.legal.common.ApiResult;
import com.campus.legal.dto.UserProfileVO;
import com.campus.legal.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 获取任意id的nickname
    @GetMapping("/getnicknameById")
    public ApiResult<Void> getnicknameById(@RequestParam("username") String username) {
        userService.getUserInfo(username);
        return ApiResult.ok();
    }

    @GetMapping("/me")
    public ApiResult<UserProfileVO> me() {
        return ApiResult.ok(userService.me());
    }

    @PutMapping("/profile")
    public ApiResult<Void> profile(@RequestBody ProfileBody body) {
        userService.updateProfile(body.getNickname(), body.getPhone(), body.getAvatarUrl());
        return ApiResult.ok();
    }

    @PutMapping("/password")
    public ApiResult<Void> password(@Validated @RequestBody PwdBody body) {
        userService.changePassword(body.getOldPassword(), body.getNewPassword());
        return ApiResult.ok();
    }

    @PostMapping("/teacher/apply")
    public ApiResult<Void> applyTeacher(@RequestBody TeacherApplyBody body) {
        userService.applyTeacher(body.getCertUrl(), body.getIntro());
        return ApiResult.ok();
    }

    @PutMapping("/teacher/info")
    public ApiResult<Void> teacherInfo(@RequestBody TeacherInfoBody body) {
        userService.updateTeacherInfo(body.getIntro(), body.getMaterialUrl());
        return ApiResult.ok();
    }

    @Data
    public static class ProfileBody {
        private String nickname;
        private String phone;
        private String avatarUrl;
    }

    @Data
    public static class PwdBody {
        @NotBlank
        private String oldPassword;
        @NotBlank
        private String newPassword;
    }

    @Data
    public static class TeacherApplyBody {
        private String certUrl;
        private String intro;
    }

    @Data
    public static class TeacherInfoBody {
        private String intro;
        private String materialUrl;
    }
}
