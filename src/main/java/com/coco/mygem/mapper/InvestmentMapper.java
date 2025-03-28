package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.Investment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InvestmentMapper extends BaseMapper<Investment> {
    
    // 查询用户的所有投资
    @Select("SELECT * FROM investments WHERE user_id = #{userId}")
    List<Investment> findByUserId(Long userId);
    
    // 查询项目的所有投资
    @Select("SELECT * FROM investments WHERE post_id = #{postId}")
    List<Investment> findByPostId(Long postId);
    
    // 更新投资状态
    @Update("UPDATE investments SET status = #{status}, confirm_time = #{confirmTime} " +
            "WHERE investment_id = #{investmentId}")
    int updateStatus(Long investmentId, String status, LocalDateTime confirmTime);
    
    // 查询用户对特定项目的投资
    @Select("SELECT * FROM investments WHERE user_id = #{userId} AND post_id = #{postId}")
    Investment findByUserIdAndPostId(Long userId, Long postId);
    
    // 统计项目的总投资金额
    @Select("SELECT COALESCE(SUM(amount), 0) FROM investments " +
            "WHERE post_id = #{postId} AND status = 'CONFIRMED'")
    Long getTotalInvestment(Long postId);
} 