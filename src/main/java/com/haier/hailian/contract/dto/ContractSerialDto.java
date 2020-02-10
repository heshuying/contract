package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * 合约型号汇总统计
 */
@Data
public class ContractSerialDto {
    private String serial;
    private Integer yearPlan;
    private Integer monthPlan;
    private Integer yearSales;
    private Integer monthSales;
}
