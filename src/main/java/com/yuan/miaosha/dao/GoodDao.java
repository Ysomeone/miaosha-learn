package com.yuan.miaosha.dao;


import com.yuan.miaosha.entity.Good;

import java.util.List;

/**
 * DAO - Good(商品)
 */
public interface GoodDao extends GenericDao<Good, Long> {
    List<Good> findListByPage(Object parameter);

    List<Good> findListNewByPage(Object parameter);

    Long deletes(Object parameter);

    Long updateStock(Object parameter);

    Good findGoodForUpdate(Object parameter);
}
