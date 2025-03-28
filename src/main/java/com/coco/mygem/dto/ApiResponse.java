package com.coco.mygem.dto;

import lombok.Data;

import java.util.Date;

/**
 * API统一响应格式
 */
@Data
public class ApiResponse {
    private Boolean success;
    private String message;
    private String errorCode;
    private Object data;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = new Date().getTime();
    }

    public ApiResponse(Boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, String errorCode) {
        this(success, message);
        this.errorCode = errorCode;
    }

    public ApiResponse(Boolean success, String message, Object data) {
        this(success, message);
        this.data = data;
    }

    public static ApiResponse success() {
        return new ApiResponse(true, "操作成功");
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }

    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse(true, "操作成功");
        response.setData(data);
        return response;
    }

    public static ApiResponse success(String message, Object data) {
        ApiResponse response = new ApiResponse(true, message);
        response.setData(data);
        return response;
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }

    public static ApiResponse error(String message, String errorCode) {
        return new ApiResponse(false, message, errorCode);
    }
} 