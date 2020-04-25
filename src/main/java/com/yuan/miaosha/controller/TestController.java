package com.yuan.miaosha.controller;



import com.yuan.miaosha.controller.common.Paramap;
import com.yuan.miaosha.controller.common.Result;
import com.yuan.miaosha.entity.Test;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * swagger 文档演示
 */
@RestController
@RequestMapping("/api/common")
@Api(value = "swagger演示", tags = "用来演示Swagger的一些注解")
public class TestController {


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
    public Result<Test>  saveUserInfo(Test test) {
        Test t = new Test();
        Paramap t1 = Paramap.create().put("t", t);
        return Result.jsonStringOk(t);
    }
}