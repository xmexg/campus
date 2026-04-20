package com.campus.legal.mapper;

import com.campus.legal.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    SysUser findByUsername(@Param("username") String username);

    SysUser findById(@Param("id") Long id);

    int insert(SysUser user);

    int updateProfile(SysUser user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateIdentityAudit(@Param("id") Long id, @Param("status") String status, @Param("remark") String remark);

    int updateTeacherAudit(@Param("id") Long id, @Param("status") String status, @Param("remark") String remark,
                           @Param("newRole") String newRole);

    int updateTeacherInfo(SysUser user);

    List<SysUser> listPendingIdentity();

    List<SysUser> listPendingTeacher();

    List<SysUser> listAll(@Param("role") String role, @Param("keyword") String keyword);

    int updateByAdmin(SysUser user);

    // 获取nickname专用接口
//    String
}
