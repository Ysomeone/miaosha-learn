package com.yuan.miaosha.dao;


import com.yuan.miaosha.entity.Order;

import java.util.List;

/**
 * DAO - Order(订单)
 *
 */
public interface OrderDao extends GenericDao<Order, Long> {
    List<Order> findListByPage(Object parameter);

    List<Order> findListNewByPage(Object parameter);

    Long deletes(Object parameter);
}
