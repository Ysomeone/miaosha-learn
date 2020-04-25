package com.yuan.miaosha.service.impl;

import com.yuan.miaosha.dao.GoodDao;
import com.yuan.miaosha.entity.Good;
import com.yuan.miaosha.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * SERVICE - Good(商品)
 */
@Service
public class GoodServiceImpl extends GenericServiceImpl<Good, Long> implements GoodService {

    @Autowired
    private GoodDao goodDao;


    @Autowired
    public void setGenericDao() {
        super.setGenericDao(goodDao);
    }

    public List<Good> findListByPage(Object parameter) {
        return goodDao.findListByPage(parameter);
    }

    public List<Good> findListNewByPage(Object parameter) {
        return goodDao.findListNewByPage(parameter);
    }

    public Long deletes(Object parameter) {
        return goodDao.deletes(parameter);
    }


}
