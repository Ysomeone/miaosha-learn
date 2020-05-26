package com.yuan.miaosha.service.impl;


import com.yuan.miaosha.dao.MqOrderDao;
import com.yuan.miaosha.entity.MqOrder;
import com.yuan.miaosha.rabbitmq.publisher.MqOrderPublisher;
import com.yuan.miaosha.service.MqOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * SERVICE - MqOrder(订单)
 *

 */
@Service
public class MqOrderServiceImpl extends GenericServiceImpl<MqOrder, Long> implements MqOrderService {

    @Autowired
    private MqOrderDao mqOrderDao;

    @Autowired
    private MqOrderPublisher mqOrderPublisher;


    @Autowired
    public void setGenericDao() {
        super.setGenericDao(mqOrderDao);
    }

	@Override
    public List<MqOrder> findListByPage(Object parameter) {
        return mqOrderDao.findListByPage(parameter);
    }

	@Override
	public List<MqOrder> findListNewByPage(Object parameter) {
        return mqOrderDao.findListNewByPage(parameter);
    }


	@Override
    public Long deletes(Object parameter) {
        return mqOrderDao.deletes(parameter);
    }

    @Override
    public Long saveOrder(Long userId) {
        MqOrder mqOrder = new MqOrder();
        mqOrder.setOrderNum("");
        mqOrder.setStatus(1);
        mqOrder.setUserId(userId);
        mqOrder.setCreateTime(new Date());
        mqOrder.setUpdateTime(new Date());
        mqOrderDao.insert(mqOrder);
        mqOrderPublisher.sendMsg(mqOrder.getId());
        return 1L;
    }


}
