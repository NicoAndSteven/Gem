package com.coco.mygem.service;

import com.coco.mygem.entity.Investment;
import com.coco.mygem.entity.InvestmentStatus;
import java.util.List;

public interface InvestmentService {
    
    /**
     * 创建投资记录
     */
    Investment createInvestment(Investment investment);

    /**
     * 获取投资记录详情
     */
    Investment getInvestmentById(Long investmentId);

    /**
     * 获取用户的所有投资记录
     */
    List<Investment> getInvestmentsByUserId(Long userId);

    /**
     * 获取项目的所有投资记录
     */
    List<Investment> getInvestmentsByPostId(Long postId);

    /**
     * 更新投资状态
     */
    boolean updateInvestmentStatus(Long investmentId, InvestmentStatus status);

    /**
     * 确认投资
     */
    boolean confirmInvestment(Long investmentId);

    /**
     * 取消投资
     */
    boolean cancelInvestment(Long investmentId);

    /**
     * 获取项目的总投资金额
     */
    Long getTotalInvestment(Long postId);

    /**
     * 检查用户是否已投资该项目
     */
    boolean hasInvested(Long userId, Long postId);

    /**
     * 获取用户对特定项目的投资记录
     */
    Investment getUserInvestment(Long userId, Long postId);
} 