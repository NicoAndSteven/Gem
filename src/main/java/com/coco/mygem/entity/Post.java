package com.coco.mygem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("posts")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long postId;
    
    private Long userId; // 发布者ID
    private String title;
    private String content;
    
    // 帖子类型：TECH_COLLABORATION, FLASH_ACTIVITY, CREATIVE_CROWD
    private String type;
    
    // 帖子状态：DRAFT, PENDING_REVIEW, PUBLISHED, REJECTED
    private String status;
    
    // 技术标签（JSON格式存储）
    @TableField("tech_tags")
    private String techTags;
    
    // 项目预算（创意币）
    private Long budget;
    
    // 已筹金额
    private Long raisedAmount;
    
    // 项目截止时间
    private Long deadline;
    
    // GitHub仓库信息
    private String githubRepo;
    
    // 项目进度（0-100）
    private Integer progress;
    
    // 审核信息
    private Long reviewerId;
    private String reviewComment;
    private Long reviewTime;
    
    // 投资人数
    private Integer investorCount;
    
    // 评论人数
    private Integer commentCount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
    
    // 项目里程碑（JSON格式存储）
    @TableField("milestones")
    private String milestones;

    @TableField(exist = false)
    private Double score;
} 