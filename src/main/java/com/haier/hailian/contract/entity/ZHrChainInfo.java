package com.haier.hailian.contract.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (ZHrChainInfo)实体类
 *
 * @author makejava
 * @since 2019-12-17 14:52:10
 */
public class ZHrChainInfo implements Serializable {
    private static final long serialVersionUID = 541726502170429052L;
    
    private Integer id;
    //链群编码
    private String chainCode;
    //链群名称
    private String chainName;
    //体验链群小微编码
    private String tyMasterXwCode;
    //体验链群小微名称
    private String tyMasterXwName;
    //体验链群主工号
    private String tyMasterEmpsn;
    //体验链群主姓名
    private String tyMasterEmpname;
    //体验链群所属区域编码
    private String tyRegionCode;
    //创单链群小微编码
    private String cdMasterXwCode;
    //创单链群小微名称
    private String cdMasterXwName;
    //创单链群主工号
    private String cdMasterEmpsn;
    //创单链群主姓名
    private String cdMasterEmpname;
    //合约id
    private Integer contractsId;
    
    private Date createDate;
    //体验链群实际分享比例
    private String tyChainPercents;
    //创单链群实际分享比例
    private String cdChainPercents;
    //市场整体分享比例（体验链群分享比例中）
    private String marketPercents;
    //市场整体分享比例（体验链群分享比例中）
    private String netPercents;
    //预计分享台阶比例
    private String planSetps;
    //实际分享台阶比例
    private String shareSetps;
    //链群平台code
    private String chainPtCode;
    //是否删除
    private Byte deleted;


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

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getTyMasterXwCode() {
        return tyMasterXwCode;
    }

    public void setTyMasterXwCode(String tyMasterXwCode) {
        this.tyMasterXwCode = tyMasterXwCode;
    }

    public String getTyMasterXwName() {
        return tyMasterXwName;
    }

    public void setTyMasterXwName(String tyMasterXwName) {
        this.tyMasterXwName = tyMasterXwName;
    }

    public String getTyMasterEmpsn() {
        return tyMasterEmpsn;
    }

    public void setTyMasterEmpsn(String tyMasterEmpsn) {
        this.tyMasterEmpsn = tyMasterEmpsn;
    }

    public String getTyMasterEmpname() {
        return tyMasterEmpname;
    }

    public void setTyMasterEmpname(String tyMasterEmpname) {
        this.tyMasterEmpname = tyMasterEmpname;
    }

    public String getTyRegionCode() {
        return tyRegionCode;
    }

    public void setTyRegionCode(String tyRegionCode) {
        this.tyRegionCode = tyRegionCode;
    }

    public String getCdMasterXwCode() {
        return cdMasterXwCode;
    }

    public void setCdMasterXwCode(String cdMasterXwCode) {
        this.cdMasterXwCode = cdMasterXwCode;
    }

    public String getCdMasterXwName() {
        return cdMasterXwName;
    }

    public void setCdMasterXwName(String cdMasterXwName) {
        this.cdMasterXwName = cdMasterXwName;
    }

    public String getCdMasterEmpsn() {
        return cdMasterEmpsn;
    }

    public void setCdMasterEmpsn(String cdMasterEmpsn) {
        this.cdMasterEmpsn = cdMasterEmpsn;
    }

    public String getCdMasterEmpname() {
        return cdMasterEmpname;
    }

    public void setCdMasterEmpname(String cdMasterEmpname) {
        this.cdMasterEmpname = cdMasterEmpname;
    }

    public Integer getContractsId() {
        return contractsId;
    }

    public void setContractsId(Integer contractsId) {
        this.contractsId = contractsId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTyChainPercents() {
        return tyChainPercents;
    }

    public void setTyChainPercents(String tyChainPercents) {
        this.tyChainPercents = tyChainPercents;
    }

    public String getCdChainPercents() {
        return cdChainPercents;
    }

    public void setCdChainPercents(String cdChainPercents) {
        this.cdChainPercents = cdChainPercents;
    }

    public String getMarketPercents() {
        return marketPercents;
    }

    public void setMarketPercents(String marketPercents) {
        this.marketPercents = marketPercents;
    }

    public String getNetPercents() {
        return netPercents;
    }

    public void setNetPercents(String netPercents) {
        this.netPercents = netPercents;
    }

    public String getPlanSetps() {
        return planSetps;
    }

    public void setPlanSetps(String planSetps) {
        this.planSetps = planSetps;
    }

    public String getShareSetps() {
        return shareSetps;
    }

    public void setShareSetps(String shareSetps) {
        this.shareSetps = shareSetps;
    }

    public String getChainPtCode() {
        return chainPtCode;
    }

    public void setChainPtCode(String chainPtCode) {
        this.chainPtCode = chainPtCode;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

}