package com.sh.my.spring.context;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

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

            //ctx.getBean(ABusyService.class).insertUser(addUser2);

            // jdbcTemplate
            JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
            List<User> userList = jdbcTemplate.query("select * from user", (rs, rowNum) -> new User(rs.getInt(1), rs.getString(2)));
            log.info("userList={}", userList);

            busyService.insertByJdbcTemplate(new User(null, "jdbcTemplate transactional"));

            busyService.saveUserAndAccount(new User(null, "TransactionTemplate managed"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ctx.close();
        }
    }

}
