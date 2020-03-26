package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2020/2/19.
 */
@Data
public class ReturnTargetDTO {

    private String childChainCode;
    private String childChainName;
    private List<TargetBasic> childTarget;
    private List<ZNodeTargetPercentInfo> childCenter;
    private List<ContractProductDTO> childProduct;
    private List<ContractXwType3DTO> childXwType3;
}
