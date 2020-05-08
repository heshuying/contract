package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 01431594 on 2020/4/15.
 */
@Data
public class EventMiddleCdDTO {

    private String littleXwCode;
    private String littleXwName;
    private String userName;
    private String pjM;
    private String pjY;
    private String shareSpace;
    private String sharePercent;

    /** 目标列表*/
    private List<TargetViewDTO> targetList = new ArrayList<>();
    /** 预案列表*/
    private List<ReservePlanDetailDTO> planList = new ArrayList<>();
}
