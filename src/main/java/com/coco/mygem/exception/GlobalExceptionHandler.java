package com.coco.mygem.exception;

import com.coco.mygem.dto.ApiResponse;
import com.coco.mygem.util.LogUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        LogUtil.warn("业务异常: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        LogUtil.warn("资源不存在: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistException(UserAlreadyExistException ex, WebRequest request) {
        LogUtil.warn("用户已存在: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
        LogUtil.warn("余额不足: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        LogUtil.warn("认证失败: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, "用户名或密码错误", "AUTHENTICATION_FAILED");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        LogUtil.warn("用户不存在: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, "用户名或密码错误", "AUTHENTICATION_FAILED");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        LogUtil.warn("访问拒绝: " + ex.getMessage());
        ApiResponse response = new ApiResponse(false, "没有权限访问此资源", "ACCESS_DENIED");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, WebRequest request) {
        LogUtil.error("系统错误: " + ex.getMessage(), ex);
        ApiResponse response = new ApiResponse(false, "系统内部错误，请联系管理员", "SYSTEM_ERROR");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 