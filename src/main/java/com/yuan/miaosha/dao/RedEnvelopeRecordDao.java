package com.yuan.miaosha.dao;


import com.yuan.miaosha.entity.RedEnvelopeRecord;

import java.util.List;

/**
 * DAO - RedEnvelopeRecord(红包领取记录)
 *
 * @version 2.0
 */
public interface RedEnvelopeRecordDao extends GenericDao<RedEnvelopeRecord, Long> {
    List<RedEnvelopeRecord> findListByPage(Object parameter);

    List<RedEnvelopeRecord> findListNewByPage(Object parameter);

    Long deletes(Object parameter);
}
