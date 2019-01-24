package com.neo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class TboderBean {

    private Long id;

    private Long adzone_id;

    private String adzoneName;

    private String alipayTotalPrice;


    private Double commission;
    private Double pub_share_pre_fee;

    private String commissionRate;
    private String orderType;
    private String itemTitle;
    private String income_rate;

    private Long itemNum;

    private Long numIid;

    private String payPrice;

    private String price;

    private Integer tkStatus;
    private String siteId;
    private String siteName;
    private String totalCommissionRate;
    private Long trade_id;
    private Long tradeParentId;
    private Date odercreateTime;
    private Date createtime;
    private Date updatetime;
    private Integer settle;

}