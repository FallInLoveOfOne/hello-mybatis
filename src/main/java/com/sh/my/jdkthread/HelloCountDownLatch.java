package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author sh
 * @since 2025/05/07
 */
@Slf4j
public class HelloCountDownLatch {

    /**
     * 工作线程
     *
     * @param latch
     */
    private static void taskWork(CountDownLatch latch) {
        for (int i = 1; i <= 3; i++) {
            int workerId = i;
            new Thread(() -> {
                log.info("👷 工人 " + workerId + " 正在执行任务...工作线程={}", Thread.currentThread().getName());
                try {
                    Thread.sleep(1000 * workerId); // 模拟任务时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("✅ 工人 " + workerId + " 完成任务！工作线程={}", Thread.currentThread().getName());
                latch.countDown(); // 任务完成，计数减1
            }).start();
        }
    }

    /**
     * 等待线程
     *
     * @param latch
     * @throws InterruptedException
     */
    private static void waitTask(CountDownLatch latch) throws InterruptedException {
        new Thread(() -> {
            log.info("🕒 等待线程={}等待所有工人完成任务...", Thread.currentThread().getName());
            try {
                latch.await(); // 等待所有工人完成任务
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("🎉 所有任务完成，等待线程={}继续执行！", Thread.currentThread().getName());
        }).start();
    }

    /**
     * 主线程
     *
     * @param latch
     * @throws InterruptedException
     */
    private static void mainTask(CountDownLatch latch) throws InterruptedException {
        log.info("🕒 主线程={}等待所有工人完成任务...", Thread.currentThread().getName());
        latch.await(); // 等待所有工人完成任务
        log.info("🎉 所有任务完成，主线程={}继续执行！", Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3); // 设置计数为3
        taskWork(latch);
        waitTask(latch);
        mainTask(latch);
        log.info("🎉 所有任务完成，主线程={}继续执行！", Thread.currentThread().getName());
    }
}
