package com.haier.hailian.contract.dto;

import lombok.Data;

import java.math.BigDecimal;
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
     * 是否已抢单 0：未抢单 1：已抢单
     */
    private String isGrab;

    /**
     * 抢单完成率
     */
    private BigDecimal grabRate = BigDecimal.ZERO;

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
