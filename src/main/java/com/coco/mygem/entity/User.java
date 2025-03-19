package com.coco.mygem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: MOHE
 * @Description: 用户实体
 * @Date: 2025/3/13 16:17
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String uuid;
    private String phoneNumber;
}
