package com.coco.mygem.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //给登录和注册两个接口放行
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 启用 CORS 并配置源
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 禁用 CSRF（API 项目通常不需要）
                .csrf(AbstractHttpConfigurer::disable)
                // 配置请求授权
                .formLogin(AbstractHttpConfigurer::disable)

                // 2. 添加 JWT 过滤器
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)

                // 3. 配置权限
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/gem/","/gem/login", "/gem/register").permitAll()
                        .anyRequest().authenticated()
                )
        ;
        return http.build();
    }

    // CORS 配置源
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 允许的前端地址
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // 允许携带凭证（如 cookies）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 应用到所有路径
        return source;
    }
}