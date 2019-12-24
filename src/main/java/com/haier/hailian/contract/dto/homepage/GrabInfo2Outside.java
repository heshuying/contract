package com.haier.hailian.contract.dto.homepage;


import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GrabInfo2Outside {

    private int id;

    private String orgCode;

    private String orgName;

    private String orgType;

    private String createCode;

    private String createName;

    private String grabTargetCode;

    private String grabTargetName;

    private String grabTargetBottom;

    private String grabTargetIncom;

    private String grabTargetUnit;

    private String sharePercent;

    private BigDecimal shareSpace;

    private String grabTargetPlan;

    private String problemCode;

    private String taskCode;

    private List<Object> details;

    private String groupId;

}
