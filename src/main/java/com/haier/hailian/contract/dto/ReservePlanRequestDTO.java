package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "预案信息")
public class ReservePlanRequestDTO {

    /**
     * 标题
     */
//    private String title;

    /**
     * 任务开始时间
     */
    @ApiModelProperty(value="开始时间",name="开始时间")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @ApiModelProperty(value="结束时间",name="结束时间")
    private Date endTime;

    /**
     * 提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天
     */
    @ApiModelProperty(value="提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天",name="提醒类型：0不提醒，1截止提醒，2截止前1小时，3截止前1天")
    private Integer remindType;

    /**
     * 提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月
     */
    @ApiModelProperty(value="提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月",name="提醒时间，0不提醒，1每工作日，2每日，3每周，4每两周，5每月")
    private Integer remindTime;

    /**
     * 是否重要：0/1 - 是/否
     */
    @ApiModelProperty(value="是否重要：0/1 - 是/否",name="是否重要：0/1 - 是/否")
    private Integer isImportant;

    /**
     * 执行人（多选时，逗号分隔）
     */
    @ApiModelProperty(value="执行人（多选时，逗号分隔）",name="执行人（多选时，逗号分隔）")
    private String executer;

    /**
     * 抄送人（多选时，号分割）
     */
    @ApiModelProperty(value="抄送人（多选时，号分割）",name="抄送人（多选时，号分割）")
    private String teamworker;

    @ApiModelProperty(value="发送人（多选时，号分割）",name="发送人（多选时，号分割）")
    private String senduser;
    /**
     * 创建人编码
     */
    @ApiModelProperty(value="创建人编码",name="创建人编码")
    private String createUserCode;

    /**
     * 创建人名字
     */
    @ApiModelProperty(value="创建人名字",name="创建人名字")
    private String createUserName;

    @ApiModelProperty(value="预案标题内容列表",name="预案标题内容列表")
    private List<ReservePlanDetailDTO> planDetail;

}
