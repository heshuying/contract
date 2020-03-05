package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 19033323
 */
@Data
public class CDGrabDataDTO {
    private String contractId;
    private String nodeCode;
    private String nodeName;
    private String xwCode;
    private String xwName;
    private String sharePercent;

    /** 目标列表*/
    private List<TargetViewDTO> targetList;
    /** 预案列表*/
    private List<ReservePlanDetailDTO> planList;
}
