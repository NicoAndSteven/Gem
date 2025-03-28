package com.coco.mygem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {
    @TableId(value = "investment_id", type = IdType.AUTO)
    private Long investmentId;
    
    private Long postId; // 投资项目ID
    private Long userId; // 投资者ID
    private Long amount; // 投资金额（创意币）
    
    // 投资状态：PENDING, CONFIRMED, CANCELLED
    private String status;
    
    // 投资时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 确认时间
    private LocalDateTime confirmTime;
    
    // 投资回报率（百分比）
    private Integer returnRate;
    
    // 投资说明
    private String description;
} 