package com.yuan.miaosha.entity;



import com.yuan.miaosha.service.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity - 订单
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "订单")
public class Order implements GenericEntity {

	private static final long serialVersionUID = 5081846432919091193L;

	/** id */
	@ApiModelProperty(value = "id", required = true)
	private Long id;
	
	/** 订单编号 */
	@ApiModelProperty(value = "订单编号", required = true)
	private String orderNumber;
	
	/** 用户id */
	@ApiModelProperty(value = "用户id", required = true)
	private Long userId;
	
	/** 商品id */
	@ApiModelProperty(value = "商品id", required = true)
	private Long goodId;
	
	/** 创建时间 */
	@ApiModelProperty(value = "创建时间", required = true)
	private java.util.Date createTime;
	
	/** 更新时间 */
	@ApiModelProperty(value = "更新时间", required = true)
	private java.util.Date updateTime;
	
}
