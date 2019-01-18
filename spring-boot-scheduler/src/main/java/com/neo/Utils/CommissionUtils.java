package com.neo.Utils;

import java.math.BigDecimal;

public class CommissionUtils {

    public static Double pddCommission(Integer score, Integer range, Integer promotion_amount) {
        BigDecimal var1 = new BigDecimal(Double.valueOf(range )/ 100);
        //分成比例
        BigDecimal var2 = new BigDecimal(Double.valueOf(score )/ 100);
        //佣金金额
        BigDecimal var3 = new BigDecimal(promotion_amount);
        BigDecimal commission = var3.multiply(var1).multiply(var2);
        return commission.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
    }
    public static Double taoBaoCommission(Integer score, Integer range, Double promotion_amount) {
        BigDecimal var1 = new BigDecimal(Double.valueOf(range )/ 100);
        //分成比例
        BigDecimal var2 = new BigDecimal(Double.valueOf(score )/ 100);
        //佣金金额
        BigDecimal var3 = new BigDecimal(promotion_amount*100);
        BigDecimal commission = var3.multiply(var1).multiply(var2);
        return commission.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
    }


}
