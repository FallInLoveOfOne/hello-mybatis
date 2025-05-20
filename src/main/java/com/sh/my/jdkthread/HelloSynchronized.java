package com.sh.my.jdkthread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author sh
 * @since 2025/05/09
 */

@Slf4j
@Data
class UserSync {
    private String name = "none";

    public synchronized void setName(String name) throws InterruptedException {
        log.info(" thread={} start set", Thread.currentThread().getName());
        //Thread.sleep() 不会释放锁！
        //Thread.sleep(1000);
        log.info("thread={} name={}", Thread.currentThread().getName(), name);
        //Thread.sleep(1000);
        this.name = name;
        log.info("thread={} end set", Thread.currentThread().getName());
        //Thread.sleep(1000);
        log.info("thread={} return", Thread.currentThread().getName());
    }
}

@Slf4j
public class HelloSynchronized {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        UserSync userSync = new UserSync();
        // write
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    userSync.setName("hai@" + finalI);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    log.error("InterruptedException", e);
                }
            }).start();
        }

        // read
        for (int i = 0; i < 9; i++) {
            new Thread(() -> log.info("thread={} get name={}", Thread.currentThread().getName(), userSync.getName())).start();
        }
        countDownLatch.await();
        log.info("thread={} get name={}", Thread.currentThread().getName(), userSync.getName());
    }
}
//某次运行结果
//D:\jdk\17.0.7\bin\java.exe -javaagent:D:\ideaIU-2023.1.2.win\lib\idea_rt.jar=59450:D:\ideaIU-2023.1.2.win\bin -Dfile.encoding=UTF-8 -classpath D:\0-code\repositories\private\hello-mybatis\target\classes;D:\maven-resp\org\mybatis\mybatis\3.5.19\mybatis-3.5.19.jar;D:\maven-resp\com\mysql\mysql-connector-j\9.2.0\mysql-connector-j-9.2.0.jar;D:\maven-resp\com\google\protobuf\protobuf-java\4.29.0\protobuf-java-4.29.0.jar;D:\maven-resp\ch\qos\logback\logback-classic\1.5.18\logback-classic-1.5.18.jar;D:\maven-resp\ch\qos\logback\logback-core\1.5.18\logback-core-1.5.18.jar;D:\maven-resp\org\slf4j\slf4j-api\2.0.17\slf4j-api-2.0.17.jar;D:\maven-resp\cglib\cglib\3.3.0\cglib-3.3.0.jar;D:\maven-resp\org\ow2\asm\asm\7.1\asm-7.1.jar;D:\maven-resp\net\bytebuddy\byte-buddy\1.17.5\byte-buddy-1.17.5.jar;D:\maven-resp\net\bytebuddy\byte-buddy-agent\1.17.5\byte-buddy-agent-1.17.5.jar;D:\maven-resp\org\springframework\spring-context\6.2.6\spring-context-6.2.6.jar;D:\maven-resp\org\springframework\spring-aop\6.2.6\spring-aop-6.2.6.jar;D:\maven-resp\org\springframework\spring-beans\6.2.6\spring-beans-6.2.6.jar;D:\maven-resp\org\springframework\spring-core\6.2.6\spring-core-6.2.6.jar;D:\maven-resp\org\springframework\spring-jcl\6.2.6\spring-jcl-6.2.6.jar;D:\maven-resp\org\springframework\spring-expression\6.2.6\spring-expression-6.2.6.jar;D:\maven-resp\io\micrometer\micrometer-observation\1.14.5\micrometer-observation-1.14.5.jar;D:\maven-resp\io\micrometer\micrometer-commons\1.14.5\micrometer-commons-1.14.5.jar;D:\maven-resp\org\springframework\spring-tx\6.2.6\spring-tx-6.2.6.jar;D:\maven-resp\org\springframework\spring-jdbc\6.2.6\spring-jdbc-6.2.6.jar;D:\maven-resp\org\mybatis\mybatis-spring\3.0.4\mybatis-spring-3.0.4.jar;D:\maven-resp\com\zaxxer\HikariCP\6.3.0\HikariCP-6.3.0.jar;D:\maven-resp\jakarta\annotation\jakarta.annotation-api\3.0.0\jakarta.annotation-api-3.0.0.jar com.sh.my.jdkthread.HelloSynchronized
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-9 get name=none
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-15 get name=none
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-16 get name=none
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-10 get name=none
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-0 start set
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-12 get name=none
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-0 name=hai@0
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-13 get name=none
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-11 get name=none
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-14 get name=none
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-0 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-0 return
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=Thread-17 get name=hai@0
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-8 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-8 name=hai@8
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-8 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-8 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-7 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-7 name=hai@7
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-7 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-7 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-6 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-6 name=hai@6
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-6 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-6 return
//        2025-05-09 11:18:17 [INFO] - c.sh.my.jdkthread.HelloSynchronized - thread=main get name=hai@6
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-5 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-5 name=hai@5
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-5 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-5 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-4 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-4 name=hai@4
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-4 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-4 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-2 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-2 name=hai@2
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-2 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-2 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-3 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-3 name=hai@3
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-3 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-3 return
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync -  thread=Thread-1 start set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-1 name=hai@1
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-1 end set
//        2025-05-09 11:18:17 [INFO] - com.sh.my.jdkthread.UserSync - thread=Thread-1 return
//
//        进程已结束,退出代码0

