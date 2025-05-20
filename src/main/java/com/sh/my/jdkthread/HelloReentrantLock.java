package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sh
 * @since 2025/05/07
 */
@Slf4j
public class HelloReentrantLock {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock(true);
        for (int in = 0; in < 3; in++) {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                new Thread(() -> {
                    reentrantLock.lock(); // 获取锁
                    try {
                        log.info("👷 工人 " + finalI + " 正在执行任务...工作线程={}", Thread.currentThread().getName());
                        Thread.sleep(1000 * finalI); // 模拟任务时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        log.info("✅ 工人 " + finalI + " 完成任务！工作线程={}", Thread.currentThread().getName());
                        reentrantLock.unlock(); // 释放锁
                    }
                }).start();
            }
        }
    }
}
