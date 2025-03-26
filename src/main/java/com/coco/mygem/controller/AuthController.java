package com.coco.mygem.controller;

import com.coco.mygem.dto.LoginRequest;
import com.coco.mygem.dto.RegisterRequest;
import com.coco.mygem.entity.User;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");

            LogUtil.info("用户登录成功: " + loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LogUtil.error("用户登录失败: " + loginRequest.getUsername(), e);
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(registerRequest.getEmail());

            User registeredUser = userService.register(user);
            LogUtil.info("新用户注册成功: " + registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            LogUtil.error("用户注册失败", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

