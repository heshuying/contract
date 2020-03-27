package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

@Data
public class TargetReachSaveReqDTO {
    private String contractId;

    /**
     * 目标列表
     */
    List<FactorGrabResDTO> targetList;
}
