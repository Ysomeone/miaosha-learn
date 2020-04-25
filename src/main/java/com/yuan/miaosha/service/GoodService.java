package com.yuan.miaosha.service;


import com.yuan.miaosha.entity.Good;

import java.util.List;

/**
 * SERVICE - Good(商品)
 */
public interface GoodService extends GenericService<Good, Long> {
    public List<Good> findListByPage(Object parameter);

    public List<Good> findListNewByPage(Object parameter);

    public Long deletes(Object parameter);
}
