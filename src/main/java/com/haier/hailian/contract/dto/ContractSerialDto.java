package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合约型号汇总统计
 */
@Data
public class ContractSerialDto {
    private String serial;
    private BigDecimal yearPlan;
    private BigDecimal monthPlan;
    private BigDecimal yearSales;
    private BigDecimal monthSales;
}
