package com.haier.hailian.contract.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (ZReservePlanTeamwork)实体类
 *
 * @author makejava
 * @since 2019-12-24 17:29:54
 */
public class ZReservePlanTeamwork implements Serializable {
    private static final long serialVersionUID = 992358289080385319L;
    //主键
    private Integer id;
    //合约id
    private Integer parentId;
    //问题编码
    private String problemCode;
    //问题描述
    private String problemContent;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //提醒时间
    private String remindTime;
    //是否重复：0/1 - 是/否
    private String isRepeat;
    //是否重要：0/1 - 是/否
    private String isImportant;
    //执行者
    private String executer;
    //协同者
    private String teamworker;
    //创建者编码
    private String createUserCode;
    //创建者名称
    private String createUserName;
    //创建时间
    private Date createUserTime;
    //任务编码
    private String taskCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getProblemCode() {
        return problemCode;
    }

    public void setProblemCode(String problemCode) {
        this.problemCode = problemCode;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(String isImportant) {
        this.isImportant = isImportant;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public String getTeamworker() {
        return teamworker;
    }

    public void setTeamworker(String teamworker) {
        this.teamworker = teamworker;
    }

    public String getCreateUserCode() {
        return createUserCode;
    }

    public void setCreateUserCode(String createUserCode) {
        this.createUserCode = createUserCode;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateUserTime() {
        return createUserTime;
    }

    public void setCreateUserTime(Date createUserTime) {
        this.createUserTime = createUserTime;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

}