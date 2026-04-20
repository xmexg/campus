package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.dto.UserProfileVO;
import com.campus.legal.entity.SysUser;
import com.campus.legal.mapper.SysUserMapper;
import com.campus.legal.security.LoginUser;
import com.campus.legal.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    public UserProfileVO me() {
        Long uid = SecurityUtils.requireUserId();
        return UserProfileVO.from(sysUserMapper.findById(uid));
    }

    @Transactional
    public void updateProfile(String nickname, String phone, String avatarUrl) {
        Long uid = SecurityUtils.requireUserId();
        SysUser u = sysUserMapper.findById(uid);
        if (u == null) {
            throw new BusinessException("用户不存在");
        }
        u.setNickname(StringUtils.hasText(nickname) ? nickname : u.getNickname());
        u.setPhone(phone);
        u.setAvatarUrl(avatarUrl);
        sysUserMapper.updateProfile(u);
    }

    @Transactional
    public com.campus.legal.entity.SysUser getUserInfo(String username){
        com.campus.legal.entity.SysUser u = sysUserMapper.findByUsername(username);
        return u;
    }

    @Transactional
    public void changePassword(String oldPwd, String newPwd) {
        Long uid = SecurityUtils.requireUserId();
        SysUser u = sysUserMapper.findById(uid);
        if (!passwordEncoder.matches(oldPwd, u.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        if (newPwd == null || newPwd.length() < 6) {
            throw new BusinessException("新密码至少6位");
        }
        sysUserMapper.updatePassword(uid, passwordEncoder.encode(newPwd));
    }

    @Transactional
    public void applyTeacher(String certUrl, String intro) {
        Long uid = SecurityUtils.requireUserId();
        SysUser u = sysUserMapper.findById(uid);
        if (!"STUDENT".equals(u.getRole())) {
            throw new BusinessException("当前角色不可申请");
        }
        if (!"APPROVED".equals(u.getIdentityAuditStatus())) {
            throw new BusinessException("身份审核通过后才可申请教师认证");
        }
        if ("PENDING".equals(u.getTeacherAuditStatus())) {
            throw new BusinessException("教师认证申请已提交，请等待审核");
        }
        if (!StringUtils.hasText(certUrl)) {
            throw new BusinessException("请上传资格证材料");
        }
        u.setTeacherCertUrl(certUrl);
        u.setTeacherIntro(intro);
        u.setTeacherAuditStatus("PENDING");
        sysUserMapper.updateTeacherInfo(u);
        sysUserMapper.updateTeacherAudit(uid, "PENDING", null, "STUDENT");
    }

    @Transactional
    public void updateTeacherInfo(String intro, String materialUrl) {
        Long uid = SecurityUtils.requireUserId();
        SysUser u = sysUserMapper.findById(uid);
        if (!"TEACHER".equals(u.getRole())) {
            throw new BusinessException("仅教师可维护");
        }
        u.setTeacherIntro(intro);
        u.setTeacherMaterialUrl(materialUrl);
        sysUserMapper.updateTeacherInfo(u);
    }

    public void requireIdentityApproved(LoginUser lu) {
        SysUser u = lu.getUser();
        if (!"APPROVED".equals(u.getIdentityAuditStatus())) {
            throw new BusinessException(403, "账号身份审核通过后方可使用此功能");
        }
    }
}
