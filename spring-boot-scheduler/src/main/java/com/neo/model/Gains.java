package com.neo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class Gains {

    private String order_id;
    private BigDecimal totalmoney;
    private BigDecimal withdraw;
    private BigDecimal gainleft;
    private Date createTime;
    private Integer uid;
}
