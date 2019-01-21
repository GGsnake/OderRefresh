package com.neo.jsonbean.JdJson;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(京东订单表)
 * @date 2019-01-21 15:10:10
 */
@ToString
@Setter
@Getter
@Accessors(chain = true)
public class Jdoder implements Serializable {
	
	//
	private Integer id;
	//京东pid
	private String positionid;
	//1
	private BigDecimal actualcosprice;
	//推客获得的实际佣金
	private BigDecimal actualfee;
	//佣金比例
	private BigDecimal commissionrate;
	//预估计佣金额
	private BigDecimal estimatecosprice;
	//推客的预估佣金额
	private BigDecimal estimatefee;
	//最终比例
	private BigDecimal finalrate;
	//商品单价
	private BigDecimal price;
	//商品Id
	private Long skuid;
	//商品名称
	private String skuname;
	//订单ID
	private Long orderid;
	//结算时间
	private Long paymonth;
	//订单完成时间
	private Long finishtime;
	//下单时间
	private Long ordertime;
	//15.待付款,16.已付款,17.已完成,18.已结算
	private Integer validcode;
	//创建时间
	private Date createtime;
	//修改时间
	private Date updatetime;
	//结算状态
	private Integer settle;



}
