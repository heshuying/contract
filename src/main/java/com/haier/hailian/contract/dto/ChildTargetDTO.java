package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 01431594 on 2020/2/20.
 */
@Data
public class ChildTargetDTO {

    private Integer id;
    private String childChainCode;
    private String childChainName;
    private BigDecimal shareSpace;
    private List<ChainGroupTargetDTO> childTargetList;
    private List<MarketTargetDTO> childMarketList;
    private List<ContractProductDTO> childProductList;
    private List<ContractXwType3DTO> childXwType3List;
}
