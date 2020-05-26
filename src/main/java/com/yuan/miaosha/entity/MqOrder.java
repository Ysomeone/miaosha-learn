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
 * @author
 * @version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "订单")
public class MqOrder implements GenericEntity {

	private static final long serialVersionUID = 5081846432919091193L;

	/** ID */
	@ApiModelProperty(value = "ID", required = true)
	private Long id;
	
	/** 订单编号 */
	@ApiModelProperty(value = "订单编号", required = true)
	private String orderNum;
	
	/** 状态 1、待支付 2、已支付 3、已取消 */
	@ApiModelProperty(value = "状态 1、待支付 2、已支付 3、已取消", required = true)
	private Integer status;
	
	/** 用户id */
	@ApiModelProperty(value = "用户id", required = true)
	private Long userId;
	
	/** 创建时间 */
	@ApiModelProperty(value = "创建时间", required = true)
	private java.util.Date createTime;
	
	/** 更新时间 */
	@ApiModelProperty(value = "更新时间", required = true)
	private java.util.Date updateTime;
	
}
