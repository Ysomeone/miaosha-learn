package com.yuan.miaosha.service.impl;

import java.util.Date;

import com.yuan.miaosha.controller.common.ApiConstants;
import com.yuan.miaosha.controller.common.Paramap;
import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.controller.exception.ApiException;
import com.yuan.miaosha.dao.GoodDao;
import com.yuan.miaosha.dao.OrderDao;
import com.yuan.miaosha.entity.Good;
import com.yuan.miaosha.entity.Order;
import com.yuan.miaosha.service.OrderService;
import com.yuan.miaosha.utils.RedissLockUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * SERVICE - Order(订单)
 *
 * @Transactional ***对表进行修改，添加时，记得加这个事务处理，尤其是下订单的业务，多表的业务处理***例子看GenericServiceImpl
 */
@Service
public class OrderServiceImpl extends GenericServiceImpl<Order, Long> implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodDao goodDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //定义ZooKeeper客户端CuratorFramework实例
    @Autowired
    private CuratorFramework client;


    private static final String pathPrefix = "/yuan/zkLock/";

    @Autowired
    public void setGenericDao() {
        super.setGenericDao(orderDao);
    }

    @Override
    public List<Order> findListByPage(Object parameter) {
        return orderDao.findListByPage(parameter);
    }

    @Override
    public List<Order> findListNewByPage(Object parameter) {
        return orderDao.findListNewByPage(parameter);
    }

    @Override
    public Long deletes(Object parameter) {
        return orderDao.deletes(parameter);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public Result<String> buyGood1(Long userId, Long goodId) throws Exception {
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
        saveOrder1(userId, goodId);
        return Result.jsonStringOk();
    }

    @Override
    @Transactional
    public Result<String> buyGood2(Long userId, Long goodId) throws Exception {
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
        saveOrder2(userId, goodId);
        return Result.jsonStringOk();
    }

    @Override
    @Transactional
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


    private void saveOrder(Long userId, Long goodId) {
        Good good = goodDao.find(goodId);
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
        }
    }


    private void saveOrder1(Long userId, Long goodId) throws Exception {
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
    }


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


}
