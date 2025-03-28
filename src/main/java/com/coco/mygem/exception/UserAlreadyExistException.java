package com.coco.mygem.exception;

/**
 * 用户已存在异常
 */
public class UserAlreadyExistException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public UserAlreadyExistException(String username) {
        super("USER_ALREADY_EXIST", "用户名 '" + username + "' 已存在");
    }
} 