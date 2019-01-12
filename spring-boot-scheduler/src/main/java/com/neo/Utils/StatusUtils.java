package com.neo.Utils;


/**
 * Created by liujupeng on 2019/1/11.
 */
public class StatusUtils {
    public static Integer getStatus(Integer devId, Integer status) {
        switch (status) {
            //淘宝
            case 0:
                if (devId ==12||devId==14) {
                    return 0;
                }
                if (devId == 3) {
                    return 1;
                }
                if (devId == 2) {
                }

            case 1:
                if (devId == 0) {

                }
                if (devId == 1) {

                }
                if (devId == 2) {

                }
            case 2:



        }
        return 0;
    }
    public static String getStatusDesc(Integer devId, Integer status) {
        switch (status) {
            //淘宝
            case 0:
                if (devId ==12||devId==14) {
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



        }
        return "已结算";
    }
}
