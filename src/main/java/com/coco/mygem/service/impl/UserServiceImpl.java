package com.coco.mygem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco.mygem.entity.User;
import com.coco.mygem.exception.InsufficientBalanceException;
import com.coco.mygem.exception.ResourceNotFoundException;
import com.coco.mygem.exception.UserAlreadyExistException;
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
        User user = userMapper.findByUsername(username);
        if (user == null) {
            LogUtil.warn("用户不存在: " + username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        LogUtil.debug("用户认证: " + username);
        return UserPrincipal.create(user);
    }

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            LogUtil.warn("注册失败，用户名已存在: " + user.getUsername());
            throw new UserAlreadyExistException(user.getUsername());
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
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("用户", "id", userId);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("用户", "username", username);
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 先检查用户是否存在
        if (userMapper.selectById(user.getUserId()) == null) {
            throw new ResourceNotFoundException("用户", "id", user.getUserId());
        }
        
        userMapper.updateById(user);
        LogUtil.info("用户信息更新成功: " + user.getUsername());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userMapper.deleteById(userId);
        LogUtil.info("用户删除成功: " + user.getUsername() + "(ID: " + userId + ")");
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    @Transactional
    public void updateBalance(Long userId, Long amount) {
        User user = getUserById(userId);
        
        // 如果是扣款操作，需要检查余额是否足够
        if (amount < 0 && user.getBalance() + amount < 0) {
            throw new InsufficientBalanceException(userId, user.getBalance(), Math.abs(amount));
        }
        
        user.setBalance(user.getBalance() + amount);
        updateUser(user);
        LogUtil.info("用户余额更新成功: " + user.getUsername() + 
                    " 变动: " + (amount >= 0 ? "+" : "") + amount + 
                    " 新余额: " + user.getBalance());
    }

    @Override
    @Transactional
    public void updateCreditScore(Long userId, Integer score) {
        User user = getUserById(userId);
        user.setCreditScore(user.getCreditScore() + score);
        
        // 信用分不应该低于0
        if (user.getCreditScore() < 0) {
            user.setCreditScore(0);
        }
        
        updateUser(user);
        LogUtil.info("用户信用分更新成功: " + user.getUsername() + 
                    " 变动: " + (score >= 0 ? "+" : "") + score + 
                    " 新信用分: " + user.getCreditScore());
    }
} 