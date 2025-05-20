package com.sh.my.spring.context;

import com.sh.my.mybatis.mapper.UserMapper;
import com.sh.my.mybatis.model.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BusyService {

    @Resource
    private UserMapper userMapper;

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
}
