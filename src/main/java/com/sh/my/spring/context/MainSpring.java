package com.sh.my.spring.context;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class MainSpring {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        try {
            UserMapper userMapper = ctx.getBean(UserMapper.class);

            User user = userMapper.selectUserById(1);
            log.info("用户={}", user.getName());

            //事务回滚演示
            BusyService busyService = ctx.getBean(BusyService.class);
            User addUser = new User(null, "事务回滚失效");
            busyService.insertWithLogic(addUser);
            User addUser2 = new User(null, "事务回滚");
            busyService.insertUser(addUser2);

            ctx.getBean(ABusyService.class).insertUser(addUser2);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ctx.close();
        }
    }

}
