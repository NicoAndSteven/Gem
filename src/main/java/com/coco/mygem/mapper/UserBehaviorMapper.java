package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    @Select("SELECT COUNT(*) FROM user_behaviors WHERE user_id = #{userId} AND behavior_type = #{behaviorType}")
    int countByUserIdAndType(Long userId, String behaviorType);
    
    @Select("SELECT COUNT(*) FROM user_behaviors WHERE user_id = #{userId} AND post_id = #{postId} AND behavior_type = #{behaviorType}")
    int countByUserIdAndPostIdAndType(Long userId, Long postId, String behaviorType);
    
    @Select("SELECT COUNT(*) FROM user_behaviors WHERE user_id = #{userId} AND behavior_type = 'VIEW'")
    int countViews(Long userId);
    
    @Select("SELECT COUNT(*) FROM user_behaviors WHERE user_id = #{userId} AND behavior_type = 'INVEST'")
    int countInvestments(Long userId);
} 