package com.haier.hailian.contract.entity;

import java.io.Serializable;

/**
 * (ZHrChainInfo)实体类
 *
 * @author makejava
 * @since 2019-12-19 17:33:19
 */
public class ZHrChainInfo implements Serializable {
    private static final long serialVersionUID = -83285888149021595L;
    
    private Integer id;
    //链群编码
    private String chainCode;
    //链群名称
    private String chainName;
    
    private String xwCode;
    //小微名称
    private String xwName;
    
    private String masterCode;
    //链群主姓名
    private String masterName;
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

    public String getXwCode() {
        return xwCode;
    }

    public void setXwCode(String xwCode) {
        this.xwCode = xwCode;
    }

    public String getXwName() {
        return xwName;
    }

    public void setXwName(String xwName) {
        this.xwName = xwName;
    }

    public String getMasterCode() {
        return masterCode;
    }

    public void setMasterCode(String masterCode) {
        this.masterCode = masterCode;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
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