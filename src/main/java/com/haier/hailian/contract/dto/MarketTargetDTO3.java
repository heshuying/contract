package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 01431594 on 2020/1/15.
 */
@Data
public class MarketTargetDTO3 {

    private String xwCode;
    private String xwName;
    private BigDecimal income;
    private BigDecimal high;
}

