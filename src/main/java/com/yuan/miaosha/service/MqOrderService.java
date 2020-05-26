package com.yuan.miaosha.service;


import com.yuan.miaosha.entity.MqOrder;

import java.util.List;

/**
 * SERVICE - MqOrder(订单)
 *
 * @author wmj
 * @version 2.0
 */
public interface MqOrderService extends GenericService<MqOrder, Long> {
    public List<MqOrder> findListByPage(Object parameter);

    public List<MqOrder> findListNewByPage(Object parameter);

    public Long deletes(Object parameter);

    Long saveOrder(Long userId);
}
