package com.coco.mygem.exception;

/**
 * 余额不足异常
 */
public class InsufficientBalanceException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public InsufficientBalanceException(Long userId, Long currentBalance, Long requiredAmount) {
        super("INSUFFICIENT_BALANCE", 
              String.format("用户(ID: %d)余额不足，当前余额: %d, 所需金额: %d", 
                            userId, currentBalance, requiredAmount));
    }
} 