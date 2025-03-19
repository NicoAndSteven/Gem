package com.coco.mygem.dto;

import lombok.Data;

/**
 * @Author: MOHE
 * @Description: 用户传输类
 * @Date: 2025/3/13 16:09
 * @Version: 1.0
 */

@Data
public class LoginRequest {
    private Long uid;
    private String username;
    private String password;
}
