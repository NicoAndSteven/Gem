package com.coco.mygem.exception;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s 不存在，%s: '%s'", resourceName, fieldName, fieldValue));
    }
} 