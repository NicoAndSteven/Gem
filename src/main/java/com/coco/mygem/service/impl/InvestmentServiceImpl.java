package com.coco.mygem.service.impl;

import com.coco.mygem.entity.Investment;
import com.coco.mygem.entity.InvestmentStatus;
import com.coco.mygem.exception.BusinessException;
import com.coco.mygem.exception.ResourceNotFoundException;
import com.coco.mygem.mapper.InvestmentMapper;
import com.coco.mygem.service.InvestmentService;
import com.coco.mygem.service.UserService;
import com.coco.mygem.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    @Autowired
    private InvestmentMapper investmentMapper;
    
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Investment createInvestment(Investment investment) {
        try {
            // 验证投资金额必须大于0
            if (investment.getAmount() <= 0) {
                throw new BusinessException("INVALID_AMOUNT", "投资金额必须大于0");
            }
            
            // 检查用户是否存在
            userService.getUserById(investment.getUserId());
            
            // 先冻结用户账户中的资金
            userService.updateBalance(investment.getUserId(), -investment.getAmount());
            
            // 设置初始状态
            investment.setStatus(InvestmentStatus.PENDING.name());
            
            // 保存投资记录
            investmentMapper.insert(investment);
            
            LogUtil.info("创建投资成功，投资项目ID: " + investment.getPostId() + 
                        ", 用户ID: " + investment.getUserId() +
                        ", 金额: " + investment.getAmount());
            return investment;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (DataAccessException e) {
            LogUtil.error("创建投资时数据库操作失败", e);
            throw new BusinessException("DB_ERROR", "数据库操作失败: " + e.getMessage(), e);
        } catch (Exception e) {
            LogUtil.error("创建投资失败", e);
            throw new BusinessException("SYSTEM_ERROR", "创建投资失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Investment getInvestmentById(Long investmentId) {
        Investment investment = investmentMapper.selectById(investmentId);
        if (investment == null) {
            throw new ResourceNotFoundException("投资记录", "id", investmentId);
        }
        return investment;
    }

    @Override
    public List<Investment> getInvestmentsByUserId(Long userId) {
        return investmentMapper.findByUserId(userId);
    }

    @Override
    public List<Investment> getInvestmentsByPostId(Long postId) {
        return investmentMapper.findByPostId(postId);
    }

    @Override
    @Transactional
    public boolean updateInvestmentStatus(Long investmentId, InvestmentStatus status) {
        try {
            // 获取当前投资记录
            Investment investment = getInvestmentById(investmentId);
            
            // 如果状态没变，直接返回成功
            if (status.name().equals(investment.getStatus())) {
                return true;
            }
            
            // 处理不同的状态变化
            if (status == InvestmentStatus.CONFIRMED) {
                // 项目确认投资成功，不需要释放资金，资金已经从用户账户扣除
                // 可能需要执行额外的奖励逻辑
                investment.setConfirmTime(LocalDateTime.now());
            } else if (status == InvestmentStatus.CANCELLED) {
                // 取消投资，需要归还用户资金
                if (InvestmentStatus.PENDING.name().equals(investment.getStatus())) {
                    userService.updateBalance(investment.getUserId(), investment.getAmount());
                    LogUtil.info("投资取消，资金已返还，用户ID: " + investment.getUserId() + 
                               ", 金额: " + investment.getAmount());
                }
            }
            
            // 更新状态
            boolean result = investmentMapper.updateStatus(investmentId, status.name(), 
                                                        status == InvestmentStatus.CONFIRMED ? 
                                                        LocalDateTime.now() : null) > 0;
            
            if (result) {
                LogUtil.info("更新投资状态成功，投资ID: " + investmentId + ", 新状态: " + status);
            } else {
                LogUtil.warn("更新投资状态失败，投资ID: " + investmentId);
            }
            return result;
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            LogUtil.error("更新投资状态失败", e);
            throw new BusinessException("UPDATE_FAILED", "更新投资状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean confirmInvestment(Long investmentId) {
        return updateInvestmentStatus(investmentId, InvestmentStatus.CONFIRMED);
    }

    @Override
    @Transactional
    public boolean cancelInvestment(Long investmentId) {
        return updateInvestmentStatus(investmentId, InvestmentStatus.CANCELLED);
    }

    @Override
    public Long getTotalInvestment(Long postId) {
        return investmentMapper.getTotalInvestment(postId);
    }

    @Override
    public boolean hasInvested(Long userId, Long postId) {
        return investmentMapper.findByUserIdAndPostId(userId, postId) != null;
    }

    @Override
    public Investment getUserInvestment(Long userId, Long postId) {
        return investmentMapper.findByUserIdAndPostId(userId, postId);
    }
} 