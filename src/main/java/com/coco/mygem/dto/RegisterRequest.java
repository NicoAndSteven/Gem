package com.coco.mygem.dto;

/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/26 16:40
 * @Version: 1.0
 */
 public class RegisterRequest {
    private String username;
    private String password;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
