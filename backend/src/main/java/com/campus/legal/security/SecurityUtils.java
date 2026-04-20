package com.campus.legal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static LoginUser currentUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !(a.getPrincipal() instanceof LoginUser)) {
            return null;
        }
        return (LoginUser) a.getPrincipal();
    }

    public static Long requireUserId() {
        LoginUser u = currentUser();
        if (u == null) {
            throw new com.campus.legal.common.BusinessException(401, "请先登录");
        }
        return u.getUserId();
    }
}
