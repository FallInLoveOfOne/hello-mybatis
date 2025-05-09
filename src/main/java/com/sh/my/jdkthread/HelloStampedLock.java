package com.sh.my.jdkthread;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.StampedLock;

class FinancialProductCache {
    private final StampedLock sl = new StampedLock();
    private BigDecimal price = BigDecimal.ZERO;

    // 乐观锁不独占锁
    public BigDecimal getPrice() {
        long stamp = sl.tryOptimisticRead();
        BigDecimal currentPrice = this.price;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currentPrice = this.price;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return currentPrice;
    }

    public void updatePrice(BigDecimal newPrice) {
        long stamp = sl.writeLock();
        try {
            this.price = newPrice;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // 使用同一把锁，保证顺序、可见性
    public synchronized void syncSetP(BigDecimal newPrice) {
        this.price = newPrice;
    }

    public synchronized BigDecimal syncGetP() {
        return this.price;
    }
}


@Slf4j
public class HelloStampedLock {
    public static void main(String[] args) throws InterruptedException {
        FinancialProductCache cache = new FinancialProductCache();
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                Long p = Long.valueOf(finalI + 1);
                log.info("set price={} thread={}", p, Thread.currentThread().getName());
                cache.updatePrice(BigDecimal.valueOf(p));
                log.info("get price={} thread={}", cache.getPrice(), Thread.currentThread().getName());
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
        log.info("get price={} thread={}", cache.getPrice(), Thread.currentThread().getName());
    }
}

