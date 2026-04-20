package com.campus.legal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String phone;
    /** STUDENT, TEACHER, ADMIN */
    private String role;
    private Integer status;
    private String identityAuditStatus;
    private String identityAuditRemark;
    private String teacherCertUrl;
    private String teacherAuditStatus;
    private String teacherAuditRemark;
    private String teacherIntro;
    private String teacherMaterialUrl;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
