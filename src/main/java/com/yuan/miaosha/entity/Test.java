package com.yuan.miaosha.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "测试数据")
public class Test {
    /** 姓名 */
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /** 联系手机 */
    @ApiModelProperty(value = "联系手机", required = true)
    private String telephone;

    /** 头像 */
    @ApiModelProperty(value = "头像", required = true)
    private String avatar;

    /** 性别 1、男 2、女 */
    @ApiModelProperty(value = "性别 1、男 2、女", required = true)
    private Integer sex;


}
