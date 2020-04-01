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
    //链群主复核分享比例截止时间
    private String checkTime;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    //是否是保存草稿
    private String isDraft;//0.提交 1 草稿

    private List<ChainGroupTargetDTO> chainGroupTargetList;

    private List<MarketTargetDTO> marketTargetList;

    private List<ContractProductDTO> productList;

    private List<ContractXwType3DTO> xwType3List;

    private List<ChildTargetDTO> children;
}
