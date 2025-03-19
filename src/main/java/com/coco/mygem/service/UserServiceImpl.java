package com.coco.mygem.service;

/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/13 16:35
 * @Version: 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coco.mygem.entity.User;
import com.coco.mygem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 使用 BCrypt 加密校验密码
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone_number", phoneNumber);
        return userMapper.selectOne(wrapper);
    }


    @Override
    public boolean validateUser(String username, String rawPassword) throws Exception {
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new Exception("用户不存在");
        }
        // 校验输入的密码是否与数据库中存储的加密密码匹配
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    @Override
    public boolean registerUser(String username, String rawPassword, String email, String mobile) {
        if (findByUsername(username) != null) {
            return false;
        }

        // 创建新用户对象
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);


        // 使用 BCrypt 加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(encodedPassword);

        // 插入数据库（需要确保 UserMapper 有对应的插入方法）
        return userMapper.insertUser(newUser) > 0;
    }

}

