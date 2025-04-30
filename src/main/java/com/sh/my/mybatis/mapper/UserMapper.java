package com.sh.my.mybatis.mapper;

// import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sh.my.mybatis.model.User;

public interface UserMapper {

    // 执行成功，支持注解+xml混搭
    @Select("SELECT id, name FROM user WHERE id = #{id}")
    User selectUserById(Integer id);

    // @Insert("INSERT INTO user (name) VALUES (#{name})")
    boolean insertUser(User user);
}
