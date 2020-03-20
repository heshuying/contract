package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.entity.TargetBasic;
import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2020/2/19.
 */
@Data
public class ReturnTargetAllDTO {

    private List<TargetBasic> parentTarget;
    private List<TOdsMinbu> parentCenter;
    private List<ContractProductDTO> parentProduct;
    private List<ContractXwType3DTO> parentXwType3;
    private List<ReturnTargetDTO> children;

}
