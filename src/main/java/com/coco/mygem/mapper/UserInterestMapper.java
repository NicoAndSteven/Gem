package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.UserInterest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserInterestMapper extends BaseMapper<UserInterest> {
    @Select("SELECT tag, weight FROM user_interests WHERE user_id = #{userId} ORDER BY weight DESC")
    List<UserInterest> findByUserId(Long userId);
    
    @Select("SELECT tag, SUM(weight) as weight FROM user_interests GROUP BY tag ORDER BY weight DESC LIMIT #{limit}")
    List<UserInterest> findTopTags(int limit);
} 