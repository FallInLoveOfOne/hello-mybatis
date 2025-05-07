package com.sh.my.jdkthread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
class UserData {
    String name;
}

@Slf4j
public class HelloReadWriteLock {

    private static Thread readTask(Lock lock, UserData userData) {
        return new Thread(() -> {
            lock.lock();
            try {
                log.info("read thread={} userData name={}", Thread.currentThread().getName(), userData.getName());
                Thread.sleep(1000); // 模拟任务时间
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            } finally {
                lock.unlock(); // 释放锁
            }
        });

    }

    private static Thread writeTask(Lock lock, UserData userData) {
        return new Thread(() -> {
            lock.lock();
            try {
                String uuid = UUID.randomUUID().toString();
                userData.setName(uuid);
                log.info("write thread={} userData name={}", Thread.currentThread().getName(), uuid);
                Thread.sleep(5000); // 模拟任务时间
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            } finally {
                lock.unlock(); // 释放锁
            }
        });

    }

    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        UserData userData = new UserData();
        List<Thread> threadsList = new ArrayList<>(10);
        for (int i = 0; i < 5; i++) {
            threadsList.add(readTask(readWriteLock.readLock(), userData));
            threadsList.add(writeTask(readWriteLock.writeLock(), userData));
        }
        // 可能会出现写饥饿问题
        threadsList.forEach(Thread::start);
    }
}
