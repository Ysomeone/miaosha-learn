package com.yuan.miaosha.entity;


import com.yuan.miaosha.service.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity - 商品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "商品")
public class Good implements GenericEntity {

    private static final long serialVersionUID = 5081846432919091193L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = true)
    private String goodName;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存", required = true)
    private Integer stock;

    /**
     * 版本
     */
    @ApiModelProperty(value = "版本", required = true)
    private Integer version;

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
