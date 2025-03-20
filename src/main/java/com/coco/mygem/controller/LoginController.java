package com.coco.mygem.controller;

/**
 * @Author: MOHE
 * @Description: 登录控制器
 * @Date: 2025/3/13 16:09
 * @Version: 1.0
 */
import com.coco.mygem.dto.LoginRequest;
import com.coco.mygem.service.UserService;
import com.coco.mygem.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/gem")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws Exception {
        // 1. 验证用户凭证
        boolean isValid = userService.validateUser(request.getPhoneNumber(), request.getPassword());

        if (!isValid) {
            return ResponseEntity.status(401).body("用户名或密码错误");
        }

        // 2. 生成 Token
        String token = JwtUtil.generateToken(request.getPhoneNumber());

        // 3. 返回 Token
        return ResponseEntity.ok().body(Map.of(
                "code", 200,
                "token", token,
                "expiresIn", JwtUtil.EXPIRATION / 1000 // 前端需要的秒数

        ));
    }
}