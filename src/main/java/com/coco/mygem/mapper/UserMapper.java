package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: MOHE
 * @Description: 用户数据访问接口
 * @Date: 2025/3/13 16:20
 * @Version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username} AND deleted = 0")
    boolean existsByUsername(String username);
}