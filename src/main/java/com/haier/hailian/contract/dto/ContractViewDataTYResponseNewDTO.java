package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 19033323
 */
@Data
public class ContractViewDataTYResponseNewDTO {
    /**
     * 小微名称
     */
    private String xwName;

    /**
     * 举单
     */
    List<TargetConfigDTO> jdList;
    /**
     * e2e
     */
    List<TargetConfigDTO> e2eList;
    /**
     * 抢单
     */
    List<TargetConfigDTO> qdList;
}
