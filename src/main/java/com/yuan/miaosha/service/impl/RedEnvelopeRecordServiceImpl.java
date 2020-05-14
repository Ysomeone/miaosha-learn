package com.yuan.miaosha.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.yuan.miaosha.dao.RedEnvelopeRecordDao;
import com.yuan.miaosha.entity.RedEnvelopeRecord;
import com.yuan.miaosha.service.RedEnvelopeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * SERVICE - RedEnvelopeRecord(红包领取记录)
 *
 * @version 2.0
 */
@Service
public class RedEnvelopeRecordServiceImpl extends GenericServiceImpl<RedEnvelopeRecord, Long> implements RedEnvelopeRecordService {

    @Autowired
    private RedEnvelopeRecordDao redEnvelopeRecordDao;


    @Autowired
    public void setGenericDao() {
        super.setGenericDao(redEnvelopeRecordDao);
    }

    @Override
    public List<RedEnvelopeRecord> findListByPage(Object parameter) {
        return redEnvelopeRecordDao.findListByPage(parameter);
    }

	@Override
	public List<RedEnvelopeRecord> findListNewByPage(Object parameter) {
        return redEnvelopeRecordDao.findListNewByPage(parameter);
    }

	@Override
    public Long deletes(Object parameter) {
        return redEnvelopeRecordDao.deletes(parameter);
    }

    @Override
    public String saveRecord(Long userId, Long redEnvelopeId,String result) {
        RedEnvelopeRecord redEnvelopeRecord = new RedEnvelopeRecord();
        redEnvelopeRecord.setUserId(userId);
        redEnvelopeRecord.setReward(new BigDecimal(result).multiply(new BigDecimal(0.01)));
        redEnvelopeRecord.setRedEnvelopeId(redEnvelopeId);
        redEnvelopeRecord.setCreateTime(new Date());
        redEnvelopeRecord.setUpdateTime(new Date());
        redEnvelopeRecordDao.insert(redEnvelopeRecord);
        return new BigDecimal(result).multiply(new BigDecimal(0.01)).toString();
    }


}
