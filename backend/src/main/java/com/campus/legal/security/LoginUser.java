package com.campus.legal.security;

import com.campus.legal.entity.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class LoginUser implements UserDetails {

    private final SysUser user;

    public LoginUser(SysUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String r = user.getRole() != null ? user.getRole() : "STUDENT";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + r));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != null && user.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != null && user.getStatus() == 1;
    }

    public Long getUserId() {
        return user.getId();
    }

//    public SysUser getUser(){
//        return this.user;
//    }

    public String getUserName(){
        return user.getUsername();
    }
}
