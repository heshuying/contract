package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.ZContractsFactor;
import lombok.Data;

/**
 * @author 19033323
 */
@Data
public class ContractViewDataTY extends ZContractsFactor {
    private String contractName;
    private String xwName;
}
