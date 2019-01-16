package com.neo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(订单通知表)
 * @date 2019-01-11 16:28:45
 */
public class SysJhAdviceOder implements Serializable {
	
	//
	private Integer id;
	//用户id
	private Integer userid;
	//订单编号
	private String odersn;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//平台类型
	private Integer src;
	//平台名称
	private String srcName;
	//订单标题
	private String name;
	//用户名称
	private String userName;
	//推广位
	private String pid;
	//订单状态
	private Integer orderStatus;
	//订单状态描述
	private String orderStatusDesc;
	//订单的创建时间
	private Date oderCreatetime;
	//创建时间
	private Date createtime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	/**
	 * 获取：用户id
	 */
	public Integer getUserid() {
		return userid;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOdersn(String odersn) {
		this.odersn = odersn;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOdersn() {
		return odersn;
	}
	/**
	 * 设置：平台类型
	 */
	public void setSrc(Integer src) {
		this.src = src;
	}
	/**
	 * 获取：平台类型
	 */
	public Integer getSrc() {
		return src;
	}
	/**
	 * 设置：平台名称
	 */
	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}
	/**
	 * 获取：平台名称
	 */
	public String getSrcName() {
		return srcName;
	}
	/**
	 * 设置：订单标题
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：订单标题
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：推广位
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**
	 * 获取：推广位
	 */
	public String getPid() {
		return pid;
	}
	/**
	 * 设置：订单状态
	 */
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * 获取：订单状态
	 */
	public Integer getOrderStatus() {
		return orderStatus;
	}
	/**
	 * 设置：订单状态描述
	 */
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	/**
	 * 获取：订单状态描述
	 */
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	/**
	 * 设置：订单的创建时间
	 */
	public void setOderCreatetime(Date oderCreatetime) {
		this.oderCreatetime = oderCreatetime;
	}
	/**
	 * 获取：订单的创建时间
	 */
	public Date getOderCreatetime() {
		return oderCreatetime;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatetime() {
		return createtime;
	}

}
