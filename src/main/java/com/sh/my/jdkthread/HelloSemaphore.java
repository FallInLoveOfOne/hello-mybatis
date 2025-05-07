package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @author sh
 * @since 2025/05/07
 */
@Slf4j
public class HelloSemaphore {


    public static void main(String[] args) {
        Semaphore parkingLot = new Semaphore(3);
        for (int i = 0; i < 5; i++) {
            int carNum = i + 1;
            new Thread(() -> {
                try {
                    log.info("🚗 车 " + carNum + " 来了，等待车位...");
                    parkingLot.acquire(); // 获取许可，没车位就阻塞
                    log.info("✅ 车 " + carNum + " 停进来了");

                    Thread.sleep(2000); // 停一会儿

                    log.info("🚙 车 " + carNum + " 离开车位");
                    parkingLot.release(); // 释放许可
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
