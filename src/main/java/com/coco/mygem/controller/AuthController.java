package com.coco.mygem.controller;

import com.coco.mygem.dto.ApiResponse;
import com.coco.mygem.dto.LoginRequest;
import com.coco.mygem.dto.RegisterRequest;
import com.coco.mygem.entity.User;
import com.coco.mygem.exception.UserAlreadyExistException;
import com.coco.mygem.security.JwtTokenProvider;
import com.coco.mygem.service.UserService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        // 参数验证
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("用户名不能为空"));
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码不能为空"));
        }
        
        // 登录验证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", jwt);
        tokenData.put("type", "Bearer");

        LogUtil.info("用户登录成功: " + loginRequest.getUsername());
        return ResponseEntity.ok(ApiResponse.success("登录成功", tokenData));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        // 参数验证
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("用户名不能为空"));
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("密码不能为空"));
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("邮箱不能为空"));
        }

        // 创建用户并注册
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());

        User registeredUser = userService.register(user);
        LogUtil.info("新用户注册成功: " + registeredUser.getUsername());

        // 隐藏敏感字段
        registeredUser.setPassword(null);
        
        return ResponseEntity.ok(ApiResponse.success("注册成功", registeredUser));
    }
}

