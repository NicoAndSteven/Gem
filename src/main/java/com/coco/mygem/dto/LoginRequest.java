package com.coco.mygem.dto;

/**
 * @Author:     MOHE
 * @Description:  传输登录请求的参数
 * @Date:    2025/3/26 16:40
 * @Version:    1.0
 */
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
