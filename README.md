在一个系统中，有并发的购买请求，如果不加以控制，会出现数量超卖或者库存数量对不上购买数量，造成数据紊乱，接下来，介绍几种防止库存超卖的方式

关于并发操作的模拟工具可以使用jmeter实现,使用方式可以浏览文章

[链接](https://juejin.im/post/5e92cc9ae51d4546ba662d4d)

github代码链接

[链接](https://github.com/Ysomeone/miaosha-learn.git)

目录结构

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4f331385069c?w=377&h=430&f=png&s=50958)


## 数据库表的设计

简单两张表，一张商品表t_good，一张订单表 t_order

~~~mysql
CREATE TABLE `t_good` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `good_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品名称',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COMMENT='商品'

CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_number` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `good_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1538 DEFAULT CHARSET=latin1 COMMENT='订单'

~~~

一般的实现中，service实现代码

```java
    public Result<String> buyGood0(Long userId, Long goodId) throws Exception {
        Good good = goodDao.find(goodId);
        if (good == null) {
            return Result.jsonStringError("不存在该商品", ApiConstants.ERROR100);
        }
        List<Order> orderList = orderDao.findByParams(Paramap.create().put("userId", userId).put("goodId", goodId));
        if (!CollectionUtils.isEmpty(orderList)) {
            return Result.jsonStringError("你已经抢购成功，请勿重复购买", ApiConstants.ERROR200);
        }
        if (good.getStock() <= 0) {
            return Result.jsonStringError("该商品库存不足", ApiConstants.ERROR300);
        }
        saveOrder(userId, goodId);
        return Result.jsonStringOk();
    }
     
     
        private void saveOrder(Long userId, Long goodId) {
        Good good = goodDao.find(goodId);
        good.setStock(good.getStock() - 1);
        Long flag = goodDao.update(good);
        if(flag>0){
            Order order = new Order();
            order.setOrderNumber("");
            order.setUserId(userId);
            order.setGoodId(goodId);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderDao.insert(order);
        }
    }
```

如果我们使用jmeter对该代码发起并发购买请求，1秒内发起100个请求

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4eac87fbdb47?w=744&h=515&f=png&s=23143))

设定商品库存为100

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4eb4da15633c?w=1069&h=168&f=png&s=20231))


结果：在插入100条订单记录（即卖出数量为100），库存数量为87

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4ebab0a18d66?w=658&h=282&f=png&s=28112))


## 基于数据库级别的乐观锁和悲观锁

###  乐观锁的实现

 乐观锁顾名思义就是在操作时很乐观，认为操作不会产生并发问题(不会有其他线程对数据进行修改)，因此不会上锁 ，在查询、更新数据记录的时候带上一个标识字段version，如果查询出来version和带上的version值相同，则更新数据记录

把saveOrder方法改造成

```
        Good good = goodDao.find(goodId);
        Long flag = goodDao.updateStock(good);
        if (flag > 0) {
            Order order = new Order();
            order.setOrderNumber("");
            order.setUserId(userId);
            order.setGoodId(goodId);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderDao.insert(order);
        } else {
            throw new ApiException("抢购失败，请重新操作", ApiConstants.ERROR400);
        }
```

其中updateStock方法的语句

```
  UPDATE T_GOOD
        <set>
            <if test="stock != null ">
                stock = stock-1,
            </if>
            <if test="version != null ">
                version = version+1,
            </if>
        </set>
        WHERE ID = #{id,jdbcType=BIGINT} and version=#{version,jdbcType=INTEGER} and stock>0
```

以500个线程并发执行接口，发现结果如下，并没有发现超卖问题,但是同一时间请求太多，导致许多请求失败

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4ec4c5b561f4?w=709&h=290&f=png&s=23143))



### 悲观锁的实现

每次获取数据的时候，都会担心数据被修改，所以每次获取数据的时候都会进行加锁，确保在自己使用的过程中数据不会被别人修改，使用完成后进行数据解锁。由于数据进行加锁，期间对该数据进行读写的其他线程都会进行等待。

在mysql中用语句实现如下

~~~mysql
 select * from table where 索引限制 for update
~~~

锁的释放

* 非事务中，语句执行完毕，立即释放锁

* 行锁在事务中，只有等当前事务进行了commit or rollback 操作才能释放锁



把saveOrder修改为

~~~
    private void saveOrder2(Long userId, Long goodId) throws Exception {
        Good good = goodDao.findGoodForUpdate(goodId);
        good.setStock(good.getStock() - 1);
        Long flag = goodDao.update(good);
        if (flag > 0) {
            Order order = new Order();
            order.setOrderNumber("");
            order.setUserId(userId);
            order.setGoodId(goodId);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderDao.insert(order);
        } else {
            throw new ApiException("抢购失败，请重新操作", ApiConstants.ERROR400);
        }
    }
~~~

其中findGoodForUpdate方法的sql语句如下

~~~java
SELECT *  FROM T_GOODWHERE ID=#{id,jdbcType=BIGINT} for update
~~~

用jmeter以500个线程并发执行接口，发现结果如下，并没有发现超卖问题,并且库存为0

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4ecd500ce364?w=767&h=313&f=png&s=27142)


## 基于redis的原子操作

redis的原子操作可以实现分布式锁的功能，因为其是单线程。可以使用命令setnx 和expire，将资源标识作为key,比如商品id,每次只有redis中不存在其值时才设置成功，如果设置成功后，再加上有效时间；但如果在设置成功后，获取锁的进程崩溃，那就成了死锁，可以在设置值成功时顺便设置过期时间，就不会产生该问题了

  在2.6.12版本开始，redis为SET命令增加了一系列选项，支持在设置值成功时顺便设置过期时间

~~~redis
SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]

SET命令支持一组修改其行为的选项：
EX seconds-设置指定的到期时间，以秒为单位。
PX毫秒-设置指定的到期时间（以毫秒为单位）。
NX-仅在不存在的情况下设置密钥。
XX-仅设置密钥（如果已存在）。
KEEPTTL-保留与密钥关联的生存时间。
~~~

代码实现

~~~
    public Result<String> buyGood3(Long userId, Long goodId) throws Exception {
        String lockkey = "miaosha:goodId:" + goodId;
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(lockkey, "1", 1, TimeUnit.SECONDS);
        if (success) {
            Good good = goodDao.find(goodId);
            if (good == null) {
                return Result.jsonStringError("不存在该商品", ApiConstants.ERROR100);
            }
            List<Order> orderList = orderDao.findByParams(Paramap.create().put("userId", userId).put("goodId", goodId));
            if (!CollectionUtils.isEmpty(orderList)) {
                return Result.jsonStringError("你已经抢购成功，请勿重复购买", ApiConstants.ERROR200);
            }
            if (good.getStock() <= 0) {
                return Result.jsonStringError("该商品库存不足", ApiConstants.ERROR300);
            }
            saveOrder(userId, goodId);
        } else {
            return Result.jsonStringError("抢购失败，请重新操作", ApiConstants.ERROR400);
        }
        return Result.jsonStringOk();
    }
~~~

以500个线程并发执行接口，发现结果如下，并没有发现超卖问题，但同一时间并发量大 ，购买成功的并不多

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4ed451260f29?w=805&h=231&f=png&s=21612)



## 基于开源框架Redisson的分布式锁

  Redisson 是一个在 Redis 的基础上实现的 Java 驻内存数据网格，Redisson提供了一系列的分布式的 Java 常用对象，还提供了许多分布式服务

以下为分布式锁的可重入实现，指在高并发产生多线程时，如果当前线程不能获得分布式锁，并不会立即被抛弃”抛弃“，而是等待设定的一段时间，重新尝试去获取分布式锁，如果可以获取成功，则执行后续业务代码；如果不能获取锁，而且重试的时间达到上限，则意味着该线程执行完毕

  ~~~java
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
  ~~~

业务代码的实现

~~~java
    @Transactional
    @Override
    public Result<String> buyGood4(Long userId, Long goodId) throws Exception {
        boolean flag = false;
        flag = RedissLockUtil.tryLock("miaosha:goodId:redisson:"+goodId, TimeUnit.SECONDS, 3, 10);
        try {
            if (flag) {
                Good good = goodDao.find(goodId);
                if (good == null) {
                    return Result.jsonStringError("不存在该商品", ApiConstants.ERROR100);
                }
                List<Order> orderList = orderDao.findByParams(Paramap.create().put("userId", userId).put("goodId", goodId));
                if (!CollectionUtils.isEmpty(orderList)) {
                    return Result.jsonStringError("你已经抢购成功，请勿重复购买", ApiConstants.ERROR200);
                }
                if (good.getStock() <= 0) {
                    return Result.jsonStringError("该商品库存不足", ApiConstants.ERROR300);
                }
                saveOrder(userId, goodId);
            } else {
                return Result.jsonStringError("抢购失败，请重新操作", ApiConstants.ERROR400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                RedissLockUtil.unlock("miaosha:goodId:redisson:" + goodId);
            }
        }
        return Result.jsonStringOk();
    }
~~~

以500个线程并发执行接口，结果如下，在等待时间为3秒、获取锁后10秒后的可重入锁，没有发生超卖问题

![](https://user-gold-cdn.xitu.io/2020/4/26/171b4edfe1e4714f?w=711&h=269&f=png&s=26835)

## 基于Zookeeper的互斥排他锁

ZooKeeper 是一个典型的分布式数据一致性解决方案，分布式应用程序可以基于 ZooKeeper 实现诸如数据发布/订阅、负载均衡、命名服务、分布式协调/通知、集群管理、Master 选举、分布式锁和分布式队列等功能。

在此使用Zookeeper实现分布式锁，从而控制多线程对共享资源的并发访问

~~~java
 @Transactional
    @Override
    public Result<String> buyGood5(Long userId, Long goodId) throws Exception {
        InterProcessMutex mutex = new InterProcessMutex(client, pathPrefix + goodId + "-lock");
        try {
            /**
             * 采用互斥锁组件尝试获取分布式锁-其中尝试的最大时间在这里设置为15s
             */
            if (mutex.acquire(15L, TimeUnit.SECONDS)) {
                Good good = goodDao.find(goodId);
                if (good == null) {
                    return Result.jsonStringError("不存在该商品", ApiConstants.ERROR100);
                }
                List<Order> orderList = orderDao.findByParams(Paramap.create().put("userId", userId).put("goodId", goodId));
                if (!CollectionUtils.isEmpty(orderList)) {
                    return Result.jsonStringError("你已经抢购成功，请勿重复购买", ApiConstants.ERROR200);
                }
                if (good.getStock() <= 0) {
                    return Result.jsonStringError("该商品库存不足", ApiConstants.ERROR300);
                }
                saveOrder(userId, goodId);
            } else {
                return Result.jsonStringError("抢购失败，请重新操作", ApiConstants.ERROR400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
        return Result.jsonStringOk();
    }
~~~



以500个线程并发执行接口，结果如下


![](https://user-gold-cdn.xitu.io/2020/4/26/171b4ee51eef2583?w=740&h=304&f=png&s=28054)

#  总结

关于避免超卖的实现，介绍几种实现方式，有基于数据库级别的乐观锁和悲观锁、基于redis的原子性操作、基于开源框架Redisson的分布式锁和基于Zookeeper的互斥排他锁，根据不同的业务需求采用不同的方法。在高并发的情况下，大量的数据库读写对性能和DB都有很大的压力，一般会使用redis、zookeeper等进行协助。







