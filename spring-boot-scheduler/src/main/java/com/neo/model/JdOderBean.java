package com.neo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@Builder
public class JdOderBean {
    private Integer id;
    private String positionId;
    private Double actualCosPrice;
    private Double actualFee;
    private Long commissionRate;
    private Double estimateCosPrice;
    private Double estimateFee;
    private Double finalRate;
    private Double price;
    private Long skuId;
    private String skuName;
    private Long orderId;
    private Long payMonth;
    private Long finishTime;
    private Long orderTime;
    private Integer validCode;
    private Date createTime;
    private Date updateTime;
    private Integer settle;


}
