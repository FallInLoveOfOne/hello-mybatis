package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;

/**
 * @author sh
 * @since 2025/05/07
 */
@Slf4j
public class HelloCyclicBarrier {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            log.info("🔔 所有线程到达屏障，继续执行！");
        });

        for (int i = 1; i <= 3; i++) {
            final int threadNum = i;
            new Thread(() -> {
                log.info("🚶‍♂️ 线程 " + threadNum + " 正在前往集合点...");
                try {
                    Thread.sleep(1000 * threadNum); // 模拟不同到达时间
                    log.info("✅ 线程 " + threadNum + " 到达集合点，等待...");
                    barrier.await(); // 等待其他线程
                    log.info("🚀 线程 " + threadNum + " 继续执行任务！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
