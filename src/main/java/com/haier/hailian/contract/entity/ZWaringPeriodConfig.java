package com.haier.hailian.contract.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 链群抢单举单预警配置表(ZWaringPeriodConfig)实体类
 *
 * @author makejava
 * @since 2020-01-17 09:22:04
 */
public class ZWaringPeriodConfig implements Serializable {
    private static final long serialVersionUID = -12976099249892595L;
    
    private Integer id;
    //链群编码
    private String chainCode;
    //0-举单周期,1-抢单周期,-1停止使用
    private Integer type;
    //周期开始时间
    private Date startDate;
    //周期结束时间
    private Date endDate;
    //操作时间月份
    private String opDate;
    //是否发送过，0-未发送，1-第一阶段发送，2-第二阶段发送（升级）
    private Integer isSend;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOpDate() {
        return opDate;
    }

    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

}