package com.yuan.miaosha.dao;


import com.yuan.miaosha.entity.RedEnvelope;

import java.util.List;


/**
 * DAO - RedEnvelope(红包)
 *
 * @version 2.0
 */
public interface RedEnvelopeDao extends GenericDao<RedEnvelope, Long> {

    List<RedEnvelope> findListByPage(Object parameter);

    List<RedEnvelope> findListNewByPage(Object parameter);

    Long deletes(Object parameter);
}
