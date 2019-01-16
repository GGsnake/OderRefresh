package com.neo.jsonbean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by liujupeng on 2018/11/16.
 */
@Setter
@Getter
@ToString
public class PddOderBean  {

    /**
     * order_sn : 180123-401424225280479
     * goods_id : 10071567
     * goods_name : 话费优惠28
     * goods_thumbnail_url : www.123.com
     * goods_quantity : 1
     * goods_price : 1
     * order_amount : 10000
     * order_create_time : 1521184942
     * order_verify_time : 1521475200
     * order_pay_time : 1516695225
     * promotion_rate : 50
     * promotion_amount : 500
     * order_status : 1
     * order_status_desc : 已成团
     * order_group_success_time : 1521184942
     * order_modify_at : 1518161603
     * p_id : 60005_1
     */

    private String order_sn;
    private Integer goods_id;
    private String goods_name;
    private String goods_thumbnail_url;
    private Integer goods_quantity;
    private Integer goods_price;
    private Integer order_amount;
    private Integer order_create_time;
    private Integer order_verify_time;
    private Integer order_pay_time;
    private Integer promotion_rate;
    private Integer promotion_amount;
    private Integer order_status;
    private String order_status_desc;
    private Integer order_group_success_time;
    private Integer order_modify_at;
    private String p_id;
    private Integer id;
   private Date createTime;
   private Date updateTime;
   private Integer status;

}
