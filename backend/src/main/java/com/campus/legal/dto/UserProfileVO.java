package com.campus.legal.dto;

import com.campus.legal.entity.SysUser;
import lombok.Data;

@Data
public class UserProfileVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String role;
    private String identityAuditStatus;
    private String teacherAuditStatus;
    private String teacherIntro;
    private String teacherMaterialUrl;
    private String teacherCertUrl;
    private String avatarUrl;

    public static UserProfileVO from(SysUser u) {
        if (u == null) {
            return null;
        }
        UserProfileVO v = new UserProfileVO();
        v.setId(u.getId());
        v.setUsername(u.getUsername());
        v.setNickname(u.getNickname());
        v.setPhone(u.getPhone());
        v.setRole(u.getRole());
        v.setIdentityAuditStatus(u.getIdentityAuditStatus());
        v.setTeacherAuditStatus(u.getTeacherAuditStatus());
        v.setTeacherIntro(u.getTeacherIntro());
        v.setTeacherMaterialUrl(u.getTeacherMaterialUrl());
        v.setTeacherCertUrl(u.getTeacherCertUrl());
        v.setAvatarUrl(u.getAvatarUrl());
        return v;
    }
}
