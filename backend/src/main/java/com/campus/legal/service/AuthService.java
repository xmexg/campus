package com.campus.legal.service;

import com.campus.legal.common.BusinessException;
import com.campus.legal.dto.AuthResponse;
import com.campus.legal.dto.LoginRequest;
import com.campus.legal.dto.RegisterRequest;
import com.campus.legal.dto.UserProfileVO;
import com.campus.legal.entity.SysUser;
import com.campus.legal.mapper.SysUserMapper;
import com.campus.legal.security.JwtUtil;
import com.campus.legal.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (sysUserMapper.findByUsername(req.getUsername()) != null) {
            throw new BusinessException("账号已存在");
        }
        SysUser u = new SysUser();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setNickname(req.getNickname() != null ? req.getNickname() : req.getUsername());
        u.setPhone(req.getPhone());
        u.setRole("STUDENT");
        u.setStatus(1);
        u.setIdentityAuditStatus("PENDING");
        u.setTeacherAuditStatus("NONE");
        sysUserMapper.insert(u);
        String token = jwtUtil.createToken(u.getId(), u.getUsername(), u.getRole());
        return AuthResponse.builder().token(token).user(UserProfileVO.from(sysUserMapper.findById(u.getId()))).build();
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        LoginUser lu = (LoginUser) auth.getPrincipal();
        SysUser u = lu.getUser();
        String token = jwtUtil.createToken(u.getId(), u.getUsername(), u.getRole());
        return AuthResponse.builder().token(token).user(UserProfileVO.from(u)).build();
    }
}
