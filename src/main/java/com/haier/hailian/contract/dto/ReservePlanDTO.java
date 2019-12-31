package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReservePlanDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天
     */
    private Integer remindType;

    /**
     * 提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月
     */
    private Integer remindTime;

    /**
     * 是否重要：0/1 - 是/否
     */
    private Integer isImportant;

    /**
     * 执行人（多选时，逗号分隔）
     */
    private String executer;

    /**
     * 抄送人（多选时，号分割）
     */
    private String teamworker;

    /**
     * 创建人编码
     */
    private String createUserCode;

    /**
     * 创建人名字
     */
    private String createUserName;

    private List<ReservePlanDetailDTO> planDetail;

}
