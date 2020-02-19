package com.haier.hailian.contract.entity;

import lombok.Data;

@Data
public class ContractExportEntity {
    private String chainCode;
    private String chainName;
    private String startDate;
    private String endDate;
    private String parentId;
    private String id;

    private String orgCode;
    private String orgName;
    private String createCode;
    private String createName;
    private String createTime;
    private String bottomInc;
    private String bottomHigh;
    private String grabInc;
    private String grabHigh;

    private String factorName;
    private String factorValue;
    private String content;

}
