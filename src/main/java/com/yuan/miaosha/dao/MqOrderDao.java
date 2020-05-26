package com.yuan.miaosha.dao;




import com.yuan.miaosha.entity.MqOrder;

import java.util.List;

/**
 * DAO - MqOrder(订单)
 * 
 * @author
 * @version 2.0
 */
public interface MqOrderDao extends GenericDao<MqOrder, Long> {
	List<MqOrder> findListByPage(Object parameter);
	List<MqOrder> findListNewByPage(Object parameter);
    Long deletes(Object parameter);
}
