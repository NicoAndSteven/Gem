package com.coco.mygem.controller;

import com.coco.mygem.dto.LoginRequest;
import com.coco.mygem.entity.User;
import com.coco.mygem.service.UserService;
import com.coco.mygem.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 验证用户凭证
            boolean isValid = userService.validateUser(request.getPhoneNumber(), request.getPassword());
            if (!isValid) {
                return ResponseEntity.status(401).body(Map.of(
                    "code", 401,
                    "message", "用户名或密码错误"
                ));
            }

            // 获取用户信息
            User user = userService.findUserByPhoneNumber(request.getPhoneNumber());


            // 生成 Token
            String token = JwtUtil.generateToken(request.getPhoneNumber());

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getUserId(),
                "phoneNumber", user.getPhoneNumber(),
                "username", user.getUsername(),
                "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "code", 500,
                "message", "登录失败：" + e.getMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            String phoneNumber = request.get("phoneNumber");

            if (username == null || password == null || phoneNumber == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", "必要信息不能为空"
                ));
            }

            // 检查手机号是否已存在
            if (userService.findUserByPhoneNumber(phoneNumber) != null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", "该手机号已注册"
                ));
            }

            // 注册用户
            boolean success = userService.registerUser(username, password, email, phoneNumber);
            if (!success) {
                return ResponseEntity.status(500).body(Map.of(
                    "code", 500,
                    "message", "注册失败"
                ));
            }

            // 获取新注册的用户信息
            User user = userService.findUserByPhoneNumber(phoneNumber);
            String token = JwtUtil.generateToken(phoneNumber);

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getUserId(),
                "phoneNumber", user.getPhoneNumber(),
                "username", user.getUsername(),
                "email", user.getEmail()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "code", 500,
                "message", "注册失败：" + e.getMessage()
            ));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "无效的token格式"
                ));
            }

            String actualToken = token.substring(7);
            JwtUtil.parseToken(actualToken);
            
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "message", "token有效"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "valid", false,
                "message", "token无效或已过期"
            ));
        }
    }
} 