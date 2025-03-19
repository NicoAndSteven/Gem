package com.coco.mygem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco.mygem.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * @Author: MOHE
 * @Description: TODO
 * @Date: 2025/3/13 16:20
 * @Version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{

    @Select("SELECT user_id, username, password FROM users WHERE username = #{username}")
    User findByUsername(String username);


    @Insert("INSERT INTO users (user_id,username,password,email,mobile) VALUES (#{uid},#{username}, #{password},#{email},#{mobile})")
    int insertUser(User user);

    @Select("SELECT 1 FROM users WHERE username = #{username}")
    boolean existsByUsername(String username);

}