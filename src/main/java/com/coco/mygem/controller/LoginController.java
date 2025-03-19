package com.coco.mygem.controller;

/**
 * @Author: MOHE
 * @Description: 登录控制器
 * @Date: 2025/3/13 16:09
 * @Version: 1.0
 */



import com.coco.mygem.dto.LoginRequest;
import com.coco.mygem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) throws Exception {
        Map<String, Object> response = new HashMap<>();
        // 调用 Service 层进行用户校验
        boolean valid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (valid) {
            // 模拟生成 token
            String token = UUID.randomUUID().toString();
            response.put("success", true);
            response.put("token", token);
            response.put("message", "登录成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
