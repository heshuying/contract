package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 01431594 on 2020/2/20.
 */
@Data
public class SaveGamblingContractDTO {

    private Integer id;

    private String contractName;

    private BigDecimal shareSpace;
    /**
     * 链群编码
     */
    private String chainCode;

    /**
     * 抢入截止时间
     */
    private String joinTime;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    private List<ChainGroupTargetDTO> chainGroupTargetList;

    private List<MarketTargetDTO> marketTargetList;

    private List<ContractProductDTO> productList;

    private List<ChildTargetDTO> children;
}
