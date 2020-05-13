package com.yuan.miaosha.service;

import com.yuan.miaosha.entity.RedEnvelopeRecord;

import java.util.List;

/**
 * SERVICE - RedEnvelopeRecord(红包领取记录)
 *
 * @author wmj
 * @version 2.0
 */
public interface RedEnvelopeRecordService extends GenericService<RedEnvelopeRecord, Long> {

    public List<RedEnvelopeRecord> findListByPage(Object parameter);

    public List<RedEnvelopeRecord> findListNewByPage(Object parameter);

    public Long deletes(Object parameter);

    void saveRecord(Long userId, Long redEnvelopeId,String result);
}
