package com.haier.hailian.contract.entity;

import java.io.Serializable;

/**
 * (XChainInfo)实体类
 *
 * @author makejava
 * @since 2019-12-19 14:36:54
 */
public class XChainInfo implements Serializable {
    private static final long serialVersionUID = 223082634719554778L;
    //主键
    private Integer id;
    //链群编码
    private String lqCode;
    //链群名称
    private String lqName;
    //链群主工号
    private String lqMasterEmpsn;
    //链群主姓名
    private String lqMasterName;
    //链群主小微编码
    private String lqMasterXwcode;
    //链群主小微名称
    private String lqMasterXwname;
    //链群平台编码
    private String lqPtCode;
    //链群区域编码
    private String lqRegionCode;
    //链群区域名称
    private String lqRegionName;
    //创单链群比例
    private String cdChainPercents;
    //体验链群比例
    private String tyChainPercents;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLqCode() {
        return lqCode;
    }

    public void setLqCode(String lqCode) {
        this.lqCode = lqCode;
    }

    public String getLqName() {
        return lqName;
    }

    public void setLqName(String lqName) {
        this.lqName = lqName;
    }

    public String getLqMasterEmpsn() {
        return lqMasterEmpsn;
    }

    public void setLqMasterEmpsn(String lqMasterEmpsn) {
        this.lqMasterEmpsn = lqMasterEmpsn;
    }

    public String getLqMasterName() {
        return lqMasterName;
    }

    public void setLqMasterName(String lqMasterName) {
        this.lqMasterName = lqMasterName;
    }

    public String getLqMasterXwcode() {
        return lqMasterXwcode;
    }

    public void setLqMasterXwcode(String lqMasterXwcode) {
        this.lqMasterXwcode = lqMasterXwcode;
    }

    public String getLqMasterXwname() {
        return lqMasterXwname;
    }

    public void setLqMasterXwname(String lqMasterXwname) {
        this.lqMasterXwname = lqMasterXwname;
    }

    public String getLqPtCode() {
        return lqPtCode;
    }

    public void setLqPtCode(String lqPtCode) {
        this.lqPtCode = lqPtCode;
    }

    public String getLqRegionCode() {
        return lqRegionCode;
    }

    public void setLqRegionCode(String lqRegionCode) {
        this.lqRegionCode = lqRegionCode;
    }

    public String getLqRegionName() {
        return lqRegionName;
    }

    public void setLqRegionName(String lqRegionName) {
        this.lqRegionName = lqRegionName;
    }

    public String getCdChainPercents() {
        return cdChainPercents;
    }

    public void setCdChainPercents(String cdChainPercents) {
        this.cdChainPercents = cdChainPercents;
    }

    public String getTyChainPercents() {
        return tyChainPercents;
    }

    public void setTyChainPercents(String tyChainPercents) {
        this.tyChainPercents = tyChainPercents;
    }

}