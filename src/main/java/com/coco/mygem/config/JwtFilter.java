package com.coco.mygem.config;

import com.coco.mygem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @Author: MOHE
 * @Description:  JWT 过滤器
 * @Date: 2025/3/20 14:06
 * @Version: 1.0
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException, java.io.IOException {

        // 1. 从 Header 提取 Token
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 解析并验证 Token
        String token = header.substring(7);
        try {
            Claims claims = JwtUtil.parseToken(token);

            // 3. 构建 Authentication 对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), // 手机号
                    null, // 凭证（密码不需要）
                    AuthorityUtils.NO_AUTHORITIES // 权限列表（可从数据库加载）
            );

            // 4. 设置安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效 Token");
            return;
        }

        chain.doFilter(request, response);
    }
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/gem/login") || path.startsWith("/gem/register");
    }
}