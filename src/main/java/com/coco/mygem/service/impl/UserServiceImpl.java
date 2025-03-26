package com.coco.mygem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco.mygem.entity.User;
import com.coco.mygem.mapper.UserMapper;
import com.coco.mygem.security.UserPrincipal;
import com.coco.mygem.service.UserService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return UserPrincipal.create(user);
    }

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userMapper.selectCount(new QueryWrapper<User>().eq("username", user.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认值
        user.setCreditScore(100);
        user.setBalance(0L);
        user.setRole("ROLE_USER");

        // 保存用户
        userMapper.insert(user);
        LogUtil.info("新用户注册成功: " + user.getUsername());
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userMapper.updateById(user);
        LogUtil.info("用户信息更新成功: " + user.getUsername());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userMapper.deleteById(userId);
        LogUtil.info("用户删除成功: " + userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    @Transactional
    public void updateBalance(Long userId, Long amount) {
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setBalance(user.getBalance() + amount);
        updateUser(user);
    }

    @Override
    @Transactional
    public void updateCreditScore(Long userId, Integer score) {
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setCreditScore(user.getCreditScore() + score);
        updateUser(user);
    }
} 