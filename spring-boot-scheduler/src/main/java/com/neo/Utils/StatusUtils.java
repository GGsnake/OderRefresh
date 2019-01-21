package com.neo.Utils;


/**
 * Created by liujupeng on 2019/1/11.
 */
public class StatusUtils {
    public static Integer getStatus(Integer devId, Integer status) {
        switch (status) {
            //淘宝
            case 0:
                if (devId == 12 || devId == 14) {
                    return 0;
                }
                if (devId == 3) {
                    return 1;
                }

                //拼多多
                //订单状态： -1 未支付; 0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（
                             // 不可提现）；5-已经结算；8-非多多进宝商品（无佣金订单）
            case 1:
                if (devId == 1|| devId == 2|| devId ==3) {
                    return 0;
                }
                if (devId == 5) {
                    return 1;
                }
            case 2:


        }
        return 0;
    }

    public static String getStatusDesc(Integer devId, Integer status) {
        switch (status) {
            //淘宝
            case 0:
                if (devId == 12 || devId == 14) {
                    return "已付款";
                }
                if (devId == 3) {
                    return "已结算";
                }
            case 1:
                if (devId == 0) {

                }
                if (devId == 1) {

                }
                if (devId == 2) {

                }
            case 2:
                if (devId == 16) {
                    return  "已付款";
                }
                if (devId == 17) {
                    return  "已完成";
                }
                if (devId ==18) {
                    return  "已结算";
                }


        }
        return "已结算";
    }
}
