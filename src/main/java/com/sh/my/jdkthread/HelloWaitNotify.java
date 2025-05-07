package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Restaurant {
    private boolean hasCalled = false;

    public synchronized void waitForCall() throws InterruptedException {
        while (!hasCalled) {
            log.info("🧍 顾客等待叫号中...={}", Thread.currentThread().getName());
            wait();  // 坐下等
        }
        log.info("✅ 顾客听到叫号，起身用餐={}", Thread.currentThread().getName());
        hasCalled = false;  // 清除状态，轮下一位
    }

    public synchronized void callNext() {
        hasCalled = true;
        log.info("🔔 服务员叫号了！={}", Thread.currentThread().getName());
        notify();  // 叫一个顾客
    }
}


@Slf4j
public class HelloWaitNotify {


    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        // 顾客线程（多个顾客也可以）
        Thread customer = new Thread(() -> {
            try {
                log.info("👤 顾客来了={}", Thread.currentThread().getName());
                restaurant.waitForCall();
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        });

        // 服务员线程
        Thread waiter = new Thread(() -> {
            try {
                log.info("👤 服务员来了={}", Thread.currentThread().getName());
                Thread.sleep(2000); // 模拟准备时间
                restaurant.callNext();  // 叫号
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        });

        customer.start();
        waiter.start();

    }
}
