package com.coco.mygem.utils;

/**
 * @Author: MOHE
 * @Description: JWT 工具类
 * @Date: 2025/3/20 11:48
 * @Version: 1.0
 */

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    // 密钥（至少 256 位，推荐从环境变量读取）
    private static final String SECRET = "mySecretKey-1234567890abcdefghijklmnopqrstuvwxyz";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token 有效期（单位：毫秒）
    public static final long EXPIRATION = 3600_000; // 1 小时

    // 生成 Token（使用手机号作为主体）
    public static String generateToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber) // 关键修改点
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    // 解析 Token 获取手机号
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}