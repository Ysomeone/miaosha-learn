package com.yuan.miaosha.service;

import com.yuan.miaosha.entity.RedEnvelope;

import java.util.List;

/**
 * SERVICE - RedEnvelope(红包)
 *
 * @version 2.0
 */
public interface RedEnvelopeService extends GenericService<RedEnvelope, Long> {

    public List<RedEnvelope> findListByPage(Object parameter);

    public List<RedEnvelope> findListNewByPage(Object parameter);

    public Long deletes(Object parameter);

    /**
     * 创建一个红包，把红包划分并存放redis的列表中
     *
     * @param amount 红包金额(单位：元)
     * @param num    数量
     * @return
     */
    public Long divideRedEnvelope(int amount, int num);

    /**
     *
     *抢红包
     * @param userId
     * @param redEnvelopeId
     */
    public String grabRedEnvelope(Long userId,Long redEnvelopeId);




}
