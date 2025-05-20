package com.sh.my.jdkthread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock Condition
 * 将 log.info 替换成 System.out.println
 * 来确保输出顺序更真实地反映线程行为（因为 System.out.println 是同步阻塞的，不易乱序）
 */
public class HelloCondition {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock(true);
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("[T-A] acquired the lock");
                System.out.println("[T-A] is waiting for the condition...");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[T-A] continues execution, holdsLock=" + reentrantLock.isHeldByCurrentThread());
            } finally {
                reentrantLock.unlock();
                System.out.println("[T-A] released the lock");
            }
        }, "Thread-A").start();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("[T-B] acquired the lock");
                try {
                    Thread.sleep(2000); // Simulate some work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                condition.signal();
                System.out.println("[T-B] sent the wake-up signal");
            } finally {
                reentrantLock.unlock();
                System.out.println("[T-B] released the lock");
            }
        }, "Thread-B").start();
    }
}
