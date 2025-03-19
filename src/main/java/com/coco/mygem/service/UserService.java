package com.coco.mygem.service;

import com.coco.mygem.entity.User;

/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/13 16:12
 * @Version: 1.0
 */
public interface UserService {
    User findByUsername(String username);
    boolean validateUser(String username, String rawPassword) throws Exception;
    boolean registerUser(String username, String rawPassword, String email, String mobile);
}
