package com.sh.my.spring.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainSpring {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        UserMapper userMapper = ctx.getBean(UserMapper.class);

        User user = userMapper.selectUserById(1);
        log.info("用户={}", user.getName());
    }

}
