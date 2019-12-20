package com.haier.hailian.contract.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by 01431594 on 2019/12/19.
 */
@Data
public class GamblingContractDTO {

    private Integer id;
    /**
     * 合约名称
     */
    private String contractsName;

    /**
     * 链群编码
     */
    private String chainCode;
    /**
     * 链群名称
     */
    private String chainName;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    private String xiaoweiCode;

    private String xiaoweiName;

    /**
     * 合同状态   0：已过期(不可抢入） 1：有效  2：作废（主动）
     */
    private String contractStatus;

    /**
     * 1.爆款产品   2.新需求 3.按照系列举单  4.按型号举单
     */
    private String contractType;

    /**
     * 合同用户群,1冰箱用户，2洗衣机用户
     */
    private String contractGroup;

    /**
     * 组织类型orgType,//1节点  2小微  3创客  4网格
     */
    private String orgType;

    /**
     * 上链关键字
     */
    private String hashCode;
    


}
