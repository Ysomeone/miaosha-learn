package com.yuan.miaosha.entity;



import com.yuan.miaosha.service.GenericEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity - 红包
 *
 * @author wmj
 * @version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "红包")
public class RedEnvelope implements GenericEntity {

	private static final long serialVersionUID = 5081846432919091193L;

	/** ID */
	@ApiModelProperty(value = "ID", required = true)
	private Long id;

	/** 金额 */
	@ApiModelProperty(value = "金额", required = true)
	private java.math.BigDecimal amount;

	/** 数量(分割成几分) */
	@ApiModelProperty(value = "数量(分割成几分)", required = true)
	private Integer num;

	/** 创建时间 */
	@ApiModelProperty(value = "创建时间", required = true)
	private java.util.Date createTime;

	/** 更新时间 */
	@ApiModelProperty(value = "更新时间", required = true)
	private java.util.Date updateTime;

}
