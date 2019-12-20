package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class CalculateSharingDTO {

    private BigDecimal bottom;
    private BigDecimal grab;
    private BigDecimal share;
}
