package com.trick.security.user.dao;

import com.trick.security.user.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(id,username,password,email,phone,birthday) VALUES(#{id},#{username},#{password},#{email},#{phone},#{birthday})")
    void insert(User user);

    @Select("SELECT * FROM user WHERE id=#{id}")
    @ResultType(User.class)
    User selectById(String id);

    @Select("SELECT * FROM user WHERE username=#{username}")
    @ResultType(User.class)
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE email=#{email}")
    @ResultType(User.class)
    User selectByEmail(String email);

    @Select("SELECT * FROM user WHERE phone=#{phone}")
    @ResultType(User.class)
    User selectByPhone(String phone);

}
