package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2020/2/19.
 */
@Data
public class ReturnTargetAllDTO {

    private List<TargetBasic> parentTarget;
    private List<ZNodeTargetPercentInfo> parentCenter;
    private List<TargetBasic> centerTarget;
    private List<ContractProductDTO> parentProduct;
    private List<ContractXwType3DTO> parentXwType3;
    private List<ReturnTargetDTO> children;

}
