package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class MarketTargetDTO {

    private String xwCode;

    private String xwName;

    private BigDecimal income;

    private BigDecimal high;

    private BigDecimal low;
}
