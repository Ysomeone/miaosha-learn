package com.yuan.miaosha.controller.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回报文格式
 *
 * @author yuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty(value = "信息", required = true)
    private String message;

    @ApiModelProperty(value = "状态码", required = true)
    private String status;

    private T data;

    /**
     * 错误返回信息
     *
     * @param message
     * @param status
     * @return
     */
    public static Result jsonStringError(String message, String status) {
        Result result = new Result(message, status, null);
        return result;
    }

    /**
     * 成功返回数据（带数据）
     *
     * @param object
     * @return
     */
    public static Result jsonStringOk(Object object) {
        Result result = new Result("ok", ApiConstants.ok, object);
        return result;
    }

    /**
     * 成功返回数据（不带数据）
     *
     * @return
     */
    public static Result jsonStringOk() {
        Result result = new Result("ok", ApiConstants.ok, null);
        return result;
    }

}
