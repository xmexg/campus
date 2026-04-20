package com.campus.legal.security;

import com.campus.legal.entity.SysUser;
import com.campus.legal.mapper.SysUserMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SysUserMapper sysUserMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.parse(token);
                String username = claims.getSubject();
                /*
                 * 匿名过滤器 AnonymousAuthenticationFilter 会先放入“已认证”的匿名用户，
                 * 若此处仍判断 getAuthentication()==null，则永远不会写入 JWT 登录态，导致带 Token 的请求仍是匿名 -> /user/me 等接口 403。
                 */
                if (username != null) {
                    SysUser u = sysUserMapper.findByUsername(username);
                    if (u != null) {
                        LoginUser principal = new LoginUser(u);
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception ignored) {
                // 无效 token：不设置认证，由后续 Security 处理
            }
        }
        chain.doFilter(request, response);
    }
}
