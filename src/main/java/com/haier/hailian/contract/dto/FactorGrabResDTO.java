package com.haier.hailian.contract.dto;

import lombok.Data;

@Data
public class FactorGrabResDTO {
    private String factId;
    private String grabId;
    private String factorCode;
    private String factorName;
    private String factorValue;
    private String factorUnit;
    private String factorDirecton;
    private String factorType;
    private String nodeName;
    private String nodeCode;
    // 计算逻辑
    private String computeLogic;

    private String targetXwCategoryCode;
    private String factorValueActual;
    private String sharePercent;

}
