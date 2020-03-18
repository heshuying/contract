package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 *  举单爆款目标dto
 * Created by 01431594 on 2020/3/17.
 */
@Data
public class ProductTargetDTO {

    private String targetCode;

    private String targetName;

    private String targetUnit;

    private BigDecimal qtyYear;

    private BigDecimal qtyMonth;

    private BigDecimal actualYear;

    private BigDecimal actualMonth;
    private String productSeries;
    private String sceneName;
}
