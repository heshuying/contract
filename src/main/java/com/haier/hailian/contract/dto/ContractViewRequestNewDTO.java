package com.haier.hailian.contract.dto;

import lombok.Data;

@Data
public class ContractViewRequestNewDTO {
    /** 合约id*/
    String contractId;
    /** 小微名称*/
    String xwName;
    /** 筛选：grabed, notGrab, lessthanJD, lessthanE2E*/
    String filterStr;
    /** 排序：incomeDesc，highDesc, grabRateDesc*/
    String orderStr;
}
