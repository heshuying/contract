package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractsShareSpaceInfo {

    private Integer contractId;

    private BigDecimal shareSpace;

    private BigDecimal shareMoney;

}
