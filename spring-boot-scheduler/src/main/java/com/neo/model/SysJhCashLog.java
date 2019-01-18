package com.neo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heguoliang
 * @Description: TODO(结算记录表)
 * @date 2019-01-18 10:25:33
 */
public class SysJhCashLog implements Serializable {

    //id
    private Long id;
    //用户id
    private Integer userid;
    //结算平台
    private String src;
    //结算金额
    private Double amount;
    //用户名
    private String username;
    //用户身份
    private String role;
    //分成比例
    private Integer score;
    //结算时间
    private Long settletime;
    //订单号
    private String odersn;
    //创建时间
    private Date createtime;
    //修改时间
    private Date updatetime;
    //状态
    private Integer status;

    /**
     * 设置：id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置：用户id
     */
    public SysJhCashLog setUserid(Integer userid) {
        this.userid = userid;
        return this;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置：结算平台
     */
    public SysJhCashLog setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 获取：结算平台
     */
    public String getSrc() {
        return src;
    }

    /**
     * 设置：结算金额
     */
    public SysJhCashLog setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    /**
     * 获取：结算金额
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * 设置：用户名
     */
    public SysJhCashLog setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * 获取：用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置：用户身份
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取：用户身份
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置：分成比例
     */
    public SysJhCashLog setScore(Integer score) {
        this.score = score;
        return this;
    }

    /**
     * 获取：分成比例
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置：结算时间
     */
    public void setSettletime(Long settletime) {
        this.settletime = settletime;
    }

    /**
     * 获取：结算时间
     */
    public Long getSettletime() {
        return settletime;
    }

    /**
     * 设置：订单号
     */
    public void setOdersn(String odersn) {
        this.odersn = odersn;
    }

    /**
     * 获取：订单号
     */
    public String getOdersn() {
        return odersn;
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

    /**
     * 设置：修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置：状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态
     */
    public Integer getStatus() {
        return status;
    }

}
