package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
    // 根据用户ID查询帖子列表
    @Select("SELECT * FROM posts WHERE user_id = #{userId}")
    List<Post> findByUserId(Long userId);
    
    // 根据类型查询帖子列表
    @Select("SELECT * FROM posts WHERE type = #{type} AND status = 'PUBLISHED'")
    List<Post> findByType(String type);
    
    // 根据技术标签查询帖子
    @Select("SELECT * FROM posts WHERE tech_tags LIKE CONCAT('%', #{tag}, '%') AND status = 'PUBLISHED'")
    List<Post> findByTechTag(String tag);
    
    // 更新帖子状态
    @Update("UPDATE posts SET status = #{status}, reviewer_id = #{reviewerId}, " +
            "review_comment = #{reviewComment}, review_time = #{reviewTime} " +
            "WHERE post_id = #{postId}")
    int updateStatus(Long postId, String status, Long reviewerId, 
                    String reviewComment, Long reviewTime);
    
    // 更新项目进度
    @Update("UPDATE posts SET progress = #{progress}, update_time = #{updateTime} " +
            "WHERE post_id = #{postId}")
    int updateProgress(Long postId, Integer progress, Long updateTime);
    
    // 更新已筹金额
    @Update("UPDATE posts SET raised_amount = raised_amount + #{amount}, " +
            "update_time = #{updateTime} WHERE post_id = #{postId}")
    int updateRaisedAmount(Long postId, Long amount, Long updateTime);
} 