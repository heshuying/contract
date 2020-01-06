package com.haier.hailian.contract.dto;

import lombok.Data;

@Data
public class FactorConfigDTO {
    /**
     * 编码
     */
    private String factorCode;

    /**
     * 名称
     */
    private String factorName;

    /**
     * 值
     */
    private String factorValue;

    /**
     * 类型 1：底线、2：抢单、3：E2E
     */
    private String factorType;

    /**
     * 单位
     */
    private String factorUnit;

}
