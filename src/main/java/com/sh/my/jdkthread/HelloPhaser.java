package com.sh.my.jdkthread;

import java.util.concurrent.Phaser;

public class HelloPhaser {

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3); // 3个线程参与

        for (int i = 0; i < 3; i++) {
            int threadId = i;
            new Thread(() -> {
                System.out.println("线程 " + threadId + " 到达阶段 0");
                phaser.arriveAndAwaitAdvance(); // 等待其他线程

                System.out.println("线程 " + threadId + " 到达阶段 1");
                phaser.arriveAndAwaitAdvance();

                System.out.println("线程 " + threadId + " 到达阶段 2");
                phaser.arriveAndAwaitAdvance();

                System.out.println("线程 " + threadId + " 完成任务");
            }).start();
        }
    }
}
// 运行结果：
// 线程 1 到达阶段 0
// 线程 0 到达阶段 0
// 线程 2 到达阶段 0
// 线程 2 到达阶段 1
// 线程 1 到达阶段 1
// 线程 0 到达阶段 1
// 线程 0 到达阶段 2
// 线程 2 到达阶段 2
// 线程 1 到达阶段 2
// 线程 1 完成任务
// 线程 2 完成任务
// 线程 0 完成任务
