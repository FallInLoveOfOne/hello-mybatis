package com.sh.my.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;
import com.sh.my.spring.beans.HelloUserBean;
import com.sh.my.spring.context.MainConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "classpath:applicationContext.xml")
@ContextConfiguration(classes = { MainConfig.class })
public class SpringTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HelloUserBean helloUserBean;

    @Test
    public void testSelectById() {
        User user = userMapper.selectUserById(1);
        log.info("用户={}", user == null ? "null" : user.getName());
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    @Rollback(false) // 事务不回滚
    public void testInsert() {
        User user = new User();
        user.setName("sun");
        userMapper.insertUser(user);
        log.info("插入成功");
        // int i = 1 / 0; // 故意制造异常
    }

    @Test
    public void testFactoryBean() {
        int age = helloUserBean.getAge();
        String name = helloUserBean.getName();
        log.info("HelloUserBean name={}, age={}", name, age);
    }
}
