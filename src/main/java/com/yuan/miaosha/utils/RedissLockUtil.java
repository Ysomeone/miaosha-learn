package com.yuan.miaosha.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;


public class RedissLockUtil {
    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient locker) {
        redissonClient = locker;
    }

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        /**
         *  在解锁之前先判断要解锁的key是否已被锁定并且是否被当前线程保持。 如果满足条件时才解锁
         */
        if (lock.isLocked()) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void test() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("sample");
        /**
         * 初始化布隆过滤器，预计统计元素数量为10000000，期望误差率为0.03
         */
        int num = 100000;
        bloomFilter.tryInit(num, 0.03);
        for (int i = num; i < num; i++) {
            bloomFilter.add(i + "");
        }
        int count = 0;
        for (int i = num; i < num + 10000; i++) {
            if (bloomFilter.contains(i + "")) {
                count++;
            }
        }
        /**
         * 测试结果（一共误判了286个，误判率：0.0286）
         */
        System.out.println("一共误判了" + count);
        System.out.println("误判率：" + (double) count / 10000);
    }

    public static void main(String[] args) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("sample");

    }
}