package com.coco.mygem.config;

import com.coco.mygem.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: MOHE
 * @Description:  JWT 过滤器
 * @Date: 2025/3/20 14:06
 * @Version: 1.0
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
        
        // 如果是OPTIONS请求，直接放行
        if (request.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // 从 Header 提取 Token
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 解析并验证 Token
        String token = header.substring(7);
        try {
            Claims claims = JwtUtil.parseToken(token);

            // 构建 Authentication 对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), // 手机号
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );

            // 设置安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleError(response, HttpStatus.UNAUTHORIZED, "Token已过期");
        } catch (JwtException e) {
            handleError(response, HttpStatus.UNAUTHORIZED, "无效Token");
        } catch (Exception e) {
            handleError(response, HttpStatus.INTERNAL_SERVER_ERROR, "服务器错误");
        }
    }

    private void handleError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("code", status.value());
        error.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/auth/login") || 
               path.equals("/api/auth/register") || 
               path.equals("/") ||
               path.equals("/error");
    }
}