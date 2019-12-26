package com.haier.hailian.contract.entity;

import lombok.Data;

/**
 * @author 19033323
 */
@Data
public class CDGrabTargetEntity {
    /** 目标编码*/
    private String targetCode;
    /** 目标名称*/
    private String targetName;
    /** 目标单位*/
    private String targetUnit;
    /** 目标底线*/
    private String targetBottomLine;
    /** 分享比例*/
    private String sharePercent;
    /** 目标方向（1：正向；0：负向）*/
    private String targetTo;

}
