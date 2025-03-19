package com.coco.mygem.controller;

import com.coco.mygem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: MOHE
 * @Description: 注册控制器
 * @Date: 2025/3/17 16:59
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String phoneNumber = request.get("phoneNumber");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("用户名和密码不能为空");
        }

        try {
            if (userService.registerUser(username, password,email,phoneNumber)) {
                return ResponseEntity.ok().body("注册成功");
            }
            return ResponseEntity.badRequest().body("用户名已存在");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("注册失败");
        }
    }
}
