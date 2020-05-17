package com.yuan.miaosha.controller;


import com.yuan.miaosha.annotation.CurrentLimiting;
import com.yuan.miaosha.controller.common.ApiConstants;
import com.yuan.miaosha.controller.common.Paramap;
import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.service.TokenService;
import com.yuan.miaosha.utils.RedissLockUtil;
import com.yuan.miaosha.entity.*;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * swagger 文档演示
 */
@RestController
@RequestMapping("/api/common")
@Api(value = "swagger演示", tags = "用来演示Swagger的一些注解")
public class TestController {

    @Resource
    private TokenService tokenService;


    @ApiOperation(value = "修改用户密码", notes = "根据用户id修改密码", authorizations = {@Authorization("sessionId")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "password", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updatePassword.json", method = RequestMethod.POST)
    public Result<String> updatePassword(HttpServletRequest request, String password,
                                         String newPassword) {

        return Result.jsonStringOk();
    }

    @ApiOperation(value = "保存用户信息", notes = "保存用户信息")
    @RequestMapping(value = "/test.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    public Result<Test> saveUserInfo(Test test) {
        Test t = new Test();
        Paramap t1 = Paramap.create().put("t", t);
        return Result.jsonStringOk(t);
    }

    @ApiOperation(value = "测试布隆过滤器", notes = "测试布隆过滤器")
    @RequestMapping(value = "/testBloomFilter.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    public Result<Test> testBloomFilter() {
        RedissLockUtil.test();
        return Result.jsonStringOk();
    }

    @ApiOperation(value = "测试redis Lua+springAop 实现限流", notes = "测试redis Lua+springAop 实现限流")
    @RequestMapping(value = "/limit.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    @CurrentLimiting
    public Result<String> limit() {
        return Result.jsonStringOk();
    }



    @ApiOperation(value = "获取token", notes = "获取token")
    @RequestMapping(value = "/getToken.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    public Result<String> getToken(){
        String token = tokenService.createToken();
        Paramap paramap = Paramap.create().put("token", token);
        return Result.jsonStringOk(paramap);
    }

    @ApiOperation(value = "校验token", notes = "获取token")
    @RequestMapping(value = "/checkToken.json", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 5000001, message = "参数错误")})
    public Result<String> checkToken(String token){
        boolean flag = tokenService.checkToken(token);
        if(flag){
            /**
             * 做业务逻辑等操作
             */
            return Result.jsonStringOk();
        }else{
            return Result.jsonStringError("请勿重新提交", ApiConstants.ERROR800);
        }
    }




}