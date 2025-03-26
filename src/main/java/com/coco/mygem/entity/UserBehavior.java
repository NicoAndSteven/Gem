package com.coco.mygem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_behaviors")
public class UserBehavior {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long postId;
    
    private String behaviorType; // VIEW, LIKE, INVEST, COMMENT
    
    private String content;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
} 