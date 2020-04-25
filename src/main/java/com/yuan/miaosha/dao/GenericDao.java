package com.yuan.miaosha.dao;


import com.yuan.miaosha.service.GenericEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author yuan
 * @Date 2020/3/25 22:24
 * @Version 1.0
 */
public interface GenericDao<T extends GenericEntity, PK extends Serializable> {

    Long insertEntity(T var1);

    Long updateEntity(T var1);

    Long deleteEntity(T var1);

    Long insert(T var1);

    Long update(T var1);

    Long delete(PK var1);

    Long deleteAll(Map<String, Object> var1);

    Long count();

    Long count(Map<String, Object> var1);

    T find(PK var1);

    List<T> findAll();

    List<T> findByParams(Map<String, Object> var1);


}
