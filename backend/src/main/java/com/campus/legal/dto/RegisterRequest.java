package com.campus.legal.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 32, message = "账号长度3-32")
    private String username;

    @NotBlank
    @Size(min = 6, max = 64, message = "密码至少6位")
    private String password;

    private String nickname;
    @Pattern(regexp = "^$|^1\\d{10}$", message = "手机号格式不正确")
    private String phone;
}
