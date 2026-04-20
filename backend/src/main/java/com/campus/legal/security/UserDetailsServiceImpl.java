package com.campus.legal.security;

import com.campus.legal.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.campus.legal.entity.SysUser u = sysUserMapper.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(u);
    }
}
