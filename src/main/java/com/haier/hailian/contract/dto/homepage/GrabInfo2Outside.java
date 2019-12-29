package com.haier.hailian.contract.dto.homepage;


import com.haier.hailian.contract.entity.ZContractsFactor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GrabInfo2Outside {

    private int id;

    private String orgCode;

    private String orgName;

    private String orgType;

    private String xiaoweiCode;

    private String xiaoweiName;

    private String regionCode;

    private String createCode;

    private String createName;

    //private String grabTargetCode;

    //private String grabTargetName;

    private String grabTargetBottom;

    //private String grabTargetIncom;

    //private String grabTargetUnit;

    // 新增抢单拓展信息字段
    private List<ZContractsFactor> grab2XW;

    private String sharePercent;

    private BigDecimal shareSpace;

    private String grabTargetPlan;

    private String groupId;

    private List<PlanTeamWorkInfo> planTeamWorkInfos;

}
