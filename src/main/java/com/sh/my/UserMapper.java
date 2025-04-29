package com.sh.my;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT id, name FROM user WHERE id = #{id}")
    User selectUserById(Integer id);

    @Insert("INSERT INTO user (name) VALUES (#{name})")
    boolean insertUser(User user);
}
