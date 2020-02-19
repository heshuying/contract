package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.entity.TargetBasic;
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
    private List<TOdsMinbu> childCenter;
    private List<ContractProductDTO> childProduct;
}
