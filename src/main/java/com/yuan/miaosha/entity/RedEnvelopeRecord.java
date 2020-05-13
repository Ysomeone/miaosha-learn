package com.yuan.miaosha.entity;


import com.yuan.miaosha.service.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity - 红包领取记录
 *
 * @version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "红包领取记录")
public class RedEnvelopeRecord implements GenericEntity {

    private static final long serialVersionUID = 5081846432919091193L;

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 领取到奖励
     */
    @ApiModelProperty(value = "领取到奖励", required = true)
    private java.math.BigDecimal reward;

    /**
     * 红包id
     */
    @ApiModelProperty(value = "红包id", required = true)
    private Long redEnvelopeId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = true)
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", required = true)
    private java.util.Date updateTime;

}
