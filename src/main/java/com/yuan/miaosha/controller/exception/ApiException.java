package com.yuan.miaosha.controller.exception;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 *
 * @author yuan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "接口异常抛出")
public class ApiException extends Exception {
    /**
     * 错误码
     */
    @ApiModelProperty(value = "错误码", required = true)
    private String code;


    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息", required = true)
    private String msg;
}
