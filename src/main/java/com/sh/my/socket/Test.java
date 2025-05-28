package com.sh.my.socket;

import com.sh.my.socket.bean.TcpClientRunner;
import com.sh.my.spring.context.MainConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author sh
 * @since 2025/05/28
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        try {
            TcpClientRunner tcpClientRunner = ctx.getBean(TcpClientRunner.class);
            tcpClientRunner.sendMessage("hello Socket!");
            log.info("发送成功");
        } catch (Exception e) {
            log.error("发送失败", e.getMessage(), e);
        } finally {
            ctx.close();
        }
    }
}
