package com.coco.mygem.service;

import com.coco.mygem.entity.User;
import java.util.List;

/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/13 16:12
 * @Version: 1.0
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    User register(User user);

    /**
     * 根据ID获取用户
     */
    User getUserById(Long userId);

    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    void updateUser(User user);

    /**
     * 删除用户
     */
    void deleteUser(Long userId);

    /**
     * 获取所有用户
     */
    List<User> getAllUsers();

    /**
     * 更新用户余额
     */
    void updateBalance(Long userId, Long amount);

    /**
     * 更新用户信用分
     */
    void updateCreditScore(Long userId, Integer score);
}
