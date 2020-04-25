package com.yuan.miaosha.service;

import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.entity.Order;

import java.util.List;

/**
 * SERVICE - Order(订单)
 */
public interface OrderService extends GenericService<Order, Long> {
    public List<Order> findListByPage(Object parameter);

    public List<Order> findListNewByPage(Object parameter);

    public Long deletes(Object parameter);

    public Result<String> buyGood0(Long userId, Long goodId) throws Exception;

    public Result<String> buyGood1(Long userId, Long goodId) throws Exception;

    public Result<String> buyGood2(Long userId, Long goodId) throws Exception;

    public Result<String> buyGood3(Long userId, Long goodId) throws Exception;

    public Result<String> buyGood4(Long userId, Long goodId) throws Exception;

    public Result<String> buyGood5(Long userId, Long goodId) throws Exception;
}