package com.haier.hailian.contract.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by 19012964 on 2019/12/18.
 */
public class AmountFormat {
    public static String ZERO="0.0";

    public static String amountFormat(String amount, int scale){
        if(StringUtils.isBlank(amount)){
            return ZERO;
        }
        BigDecimal amt=new BigDecimal(amount);
        BigDecimal res= amt.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return res.toString();
    }

    public static double amtStr2D(String amount){
        if(StringUtils.isBlank(amount)){
            return 0d;
        }
        return   Double.parseDouble(amount);
    }


    public static BigDecimal amtStr2BD(String amount){
        if(StringUtils.isBlank(amount)){
            return BigDecimal.ZERO;
        }
        return   new BigDecimal(amount);
    }
}
