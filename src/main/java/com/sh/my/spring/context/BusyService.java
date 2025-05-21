package com.sh.my.spring.context;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
public class BusyService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Transactional(rollbackFor = Exception.class)
    public void insertUser(User user) {
        userMapper.insertUser(user);
        log.info("插入成功");
        throw new RuntimeException("插入失败");
    }

    /**
     * 事务不会回滚案例
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertWithLogic(User user) {
        try {
            this.insertUser(user);
        } catch (Exception e) {
            log.error("非代理调用，事务回滚失效={}", e.getMessage());
        }
    }

    @Transactional
    public void insertByJdbcTemplate(User user) {
        jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", user.getName());
        //int i = 1 / 0;
    }

    /**
     * 通过TransactionManager手动控制事务
     * TransactionTemplate是对TransactionManager的封装简化
     */
    public void insertByTransactionManager(User user) {
        // 创建事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("saveUserTx");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 开启事务
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            // 执行数据库操作
            jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", user.getName());


            //int i = 1 / 0;
            // 提交事务
            transactionManager.commit(status);
        } catch (Exception e) {
            // 回滚事务
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * JdbcTemplate 是用来执行 SQL 的工具：query(), update(), execute() 等。
     * TransactionTemplate 是用来包裹一段逻辑，使其运行在一个受控事务上下文中。
     */
    public void saveUserAndAccount(User user) {
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

        txTemplate.executeWithoutResult(status -> {
            jdbcTemplate.update("INSERT INTO user (name) VALUES (?)", user.getName());
            // 模拟异常触发回滚
            // int x = 1 / 0;
        });
    }
}
