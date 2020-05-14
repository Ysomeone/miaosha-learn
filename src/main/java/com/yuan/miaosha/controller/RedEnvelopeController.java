package com.yuan.miaosha.controller;

import com.yuan.miaosha.controller.common.ApiConstants;
import com.yuan.miaosha.controller.common.Paramap;
import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.service.RedEnvelopeRecordService;
import com.yuan.miaosha.service.RedEnvelopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author yuan
 * @Date 2020/5/13 21:52
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/redEnvelope")
@Api(value = "redis Lua 抢红包", tags = "redis Lua 抢红包")
public class RedEnvelopeController {


    @Resource
    private RedEnvelopeService redEnvelopeService;

    @Resource
    private RedEnvelopeRecordService redEnvelopeRecordService;

    /**
     * @param amount 红包金额
     * @param num  数量
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分配红包", notes = "分配红包")
    @RequestMapping(value = "/divideRedEnvelope.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "amount", value = "红包金额", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "num", value = "数量", required = true, dataType = "int")
    })
    public Result<String> divideRedEnvelope(Integer amount, Integer num) {
        redEnvelopeService.divideRedEnvelope(amount, num);
        return Result.jsonStringOk();
    }


    /**
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "抢红包", notes = "抢红包")
    @RequestMapping(value = "/grabRedEnvelope.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "long"),
            @ApiImplicitParam(paramType = "query", name = "redEnvelopeId", value = "红包id", required = true, dataType = "long")
    })
    public Result<String> grabRedEnvelope(Long userId, Long redEnvelopeId) {
        String result = redEnvelopeService.grabRedEnvelope(userId, redEnvelopeId);
        if (result.equals("-2")) {
            return Result.jsonStringError("红包已经完了", ApiConstants.ERROR500);
        } else if (result.equals("-1")) {
            return Result.jsonStringError("已经抢到红包", ApiConstants.ERROR600);
        } else {
            String reward = redEnvelopeRecordService.saveRecord(userId, redEnvelopeId, result);
            Paramap.create().put("info", "用户" + userId + "：抢到红包" + reward + "元");
            return Result.jsonStringOk();
        }

    }


}
