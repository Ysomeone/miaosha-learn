package com.yuan.miaosha.controller;

import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.service.OrderService;
import io.swagger.annotations.*;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author yuan
 * @Date 2020/4/12 19:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/kill")
public class KillController {

    @Resource
    private OrderService orderService;


    /**
     * 错误方法
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品0", notes = "购买商品1")
    @RequestMapping(value = "/buyGood0.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood0(Long userId, Long goodId) throws Exception {
        return orderService.buyGood0(userId, goodId);
    }


    /**
     * 基于数据库乐观锁实现
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品1", notes = "购买商品1")
    @RequestMapping(value = "/buyGood1.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood1(Long userId, Long goodId) throws Exception {
        return orderService.buyGood1(userId, goodId);
    }

    /**
     * 基于数据库悲观锁实现
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品2", notes = "购买商品2")
    @RequestMapping(value = "/buyGood2.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood2(Long userId, Long goodId) throws Exception {
        return orderService.buyGood2(userId, goodId);
    }

    /**
     * 基于reids原子性操作实现
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品3", notes = "购买商品3")
    @RequestMapping(value = "/buyGood3.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood3(Long userId, Long goodId) throws Exception {
        return orderService.buyGood3(userId, goodId);
    }

    /**
     * 基于开源框架redisson实现
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品4", notes = "购买商品4")
    @RequestMapping(value = "/buyGood4.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood4(Long userId, Long goodId) throws Exception {
        return orderService.buyGood4(userId, goodId);
    }

    /**
     * 基于Zookeeper的互斥排他锁
     *
     * @param userId
     * @param goodId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "购买商品5", notes = "购买商品4")
    @RequestMapping(value = "/buyGood5.json", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "goodId", value = "商品id", required = true, dataType = "Long")
    })
    public Result<String> buyGood5(Long userId, Long goodId) throws Exception {
        return orderService.buyGood5(userId, goodId);
    }


}
