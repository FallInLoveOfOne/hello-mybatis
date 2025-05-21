package com.sh.my.spring.context;

import com.sh.my.mybatis.model.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class ABusyService {
    @Resource
    private BusyService busyService;

    /**
     * 事务回滚
     */
    public void insertUser(User user) {
        try {
            busyService.insertUser(user);
        } catch (Exception e) {
            log.error("捕捉事务传播异常", e);
            // 吞掉异常，会导致Transaction rolled back because it has been marked as rollback-only
        }
    }
}
