package com.yuan.miaosha.service.impl;

import java.util.Date;

import com.yuan.miaosha.controller.common.LuaScript;
import com.yuan.miaosha.dao.RedEnvelopeDao;
import com.yuan.miaosha.entity.RedEnvelope;
import com.yuan.miaosha.service.RedEnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;

/**
 * SERVICE - RedEnvelope(红包)
 *
 * @version 2.0
 */
@Service
public class RedEnvelopeServiceImpl extends GenericServiceImpl<RedEnvelope, Long> implements RedEnvelopeService {

    @Autowired
    private RedEnvelopeDao redEnvelopeDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setGenericDao() {
        super.setGenericDao(redEnvelopeDao);
    }


    @Override
    public List<RedEnvelope> findListByPage(Object parameter) {
        return redEnvelopeDao.findListByPage(parameter);
    }

    @Override
    public List<RedEnvelope> findListNewByPage(Object parameter) {
        return redEnvelopeDao.findListNewByPage(parameter);
    }

    @Override
    public Long deletes(Object parameter) {
        return redEnvelopeDao.deletes(parameter);
    }

    @Override
    public Long divideRedEnvelope(int amount, int num) {
        /**
         * 每个人至少分到一分钱,如果有2000分，6人，随机得到五个小于1994（2000-6）的数
         * 比如 a1=4，a2=120，a3=324，a4=500，a5=700(随机拿到的五个数进行排序)，那么红包钱分别为： a1+1,a2-a1+1,a3-a2+1,a4-a3+1,a5-a4+1,1994-a5+1(总和刚好为2000)
         */
        RedEnvelope redEnvelope = new RedEnvelope();
        redEnvelope.setAmount(new BigDecimal(amount));
        redEnvelope.setNum(num);
        redEnvelope.setCreateTime(new Date());
        redEnvelope.setUpdateTime(new Date());
        redEnvelopeDao.insert(redEnvelope);
        /**
         * 拿来随机分的，按分来算
         */
        int totalAmount = amount * 100 - num;
        /**
         * 随机数
         */
        int[] randomNum = new int[num - 1];
        /**
         * 红包金额
         */
        int[] redEnvelopeAmount = new int[num];

        for (int i = 0; i < num - 1; i++) {
            int rand = new Random().nextInt(totalAmount);
            randomNum[i] = rand;
        }
        Arrays.sort(randomNum);
        /**
         * 条件语句分别分配的第一个、最后一个、中间的红包
         */
        for (int i = 0; i < num; i++) {
            if (i == 0) {
                redEnvelopeAmount[i] = randomNum[i] + 1;
            } else if (i == num - 1) {
                redEnvelopeAmount[i] = totalAmount - randomNum[i - 1] + 1;
            } else {
                redEnvelopeAmount[i] = randomNum[i] - randomNum[i - 1] + 1;
            }
        }
        /**
         * 产生的小红包key,以list存储在reids中
         */
        String key = "envelope:redEnvelopeId:" + redEnvelope.getId();
        Boolean flag = stringRedisTemplate.hasKey(key);
        if (!flag) {
            for (Integer i : redEnvelopeAmount) {
                stringRedisTemplate.opsForList().leftPush(key, i + "");
            }
        }
        return redEnvelope.getId();
    }

    @Override
    public String grabRedEnvelope(Long userId, Long redEnvelopeId) {

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptText(LuaScript.redLua);
        List<String> keyList = new ArrayList();
        /**
         * 产生的小红包key
         */
        keyList.add("envelope:redEnvelopeId:" + redEnvelopeId);
        /**
         * 红包领取记录key
         */
        keyList.add("envelope:record:" + redEnvelopeId);
        keyList.add("" + userId);
        keyList.add(String.valueOf(userId));
        /**
         *    -1 已经抢到红包   -2 红包已经完了   ，其余是抢到红包并返回红包余额
         */
        String result = stringRedisTemplate.execute(redisScript, keyList);
        return result;
    }


}
