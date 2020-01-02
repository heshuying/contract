package com.haier.hailian.contract.entity;

import lombok.Data;

/**
 * Created by J.wind
 * At 2019-12-25 18:02
 * Email: jiangzd102@outlook.com
 */
@Data
public class IhaierTask {
    //任务创建者的oid
    private String openId;
    //任务内容
    private String content;
    //任务结束时间戳
    private Long endDate;
    //提醒时间(-1:不提醒、0:开始时间提醒、15:开始时间前15分钟提醒、60:开始时间前1小时提醒)
    private Integer noticeTime;
    //任务定时提醒时间(0:不提醒、1:每天、2:每工作日、3:每周、4:每两周、5:每月)
    private Integer timingNoticeTime;
    //是否重要(0:不重要、1:重要)
    private Integer important;
    //执行人oid的集合
    private String[] executors;
    //抄送人oid的集合
    private String[] ccs;
    //回调地址
    private String callBackUrl;
    //渠道
    private String channel;
    //自渠道
    private String createChannel;
}
