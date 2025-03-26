package com.coco.mygem.service.impl;

import com.coco.mygem.entity.Investment;
import com.coco.mygem.entity.InvestmentStatus;
import com.coco.mygem.mapper.InvestmentMapper;
import com.coco.mygem.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    @Autowired
    private InvestmentMapper investmentMapper;

    @Override
    @Transactional
    public Investment createInvestment(Investment investment) {
        investment.setCreateTime(System.currentTimeMillis());
        investment.setStatus(InvestmentStatus.PENDING.name());
        investmentMapper.insert(investment);
        return investment;
    }

    @Override
    public Investment getInvestmentById(Long investmentId) {
        return investmentMapper.selectById(investmentId);
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
        Long confirmTime = status == InvestmentStatus.CONFIRMED ? 
                          System.currentTimeMillis() : null;
        return investmentMapper.updateStatus(investmentId, status.name(), confirmTime) > 0;
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