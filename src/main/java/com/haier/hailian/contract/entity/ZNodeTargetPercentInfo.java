package com.haier.hailian.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * (ZNodeTargetPercentInfo)实体类
 *
 * @author makejava
 * @since 2019-12-19 17:28:53
 */
public class ZNodeTargetPercentInfo implements Serializable {
    private static final long serialVersionUID = 302307902555996840L;
    
    private Integer id;
    //链群编码
    private String lqCode;
    //链群名称
    private String lqName;
    //节点编码
    private String nodeCode;
    //节点名称
    private String nodeName;
    //小微编码
    private String xwCode;
    //小微名称
    private String xwName;
    //目标编码
    private String targetCode;
    //目标名称
    private String targetName;
    //目标线
    private String targetLine;
    //分享比例
    private String sharePercent;
    //模块
    private String parentChainCode;
    // xwType3Code
    @TableField(value = "xwType3Code")
    private String xwType3Code;
    // xwType3
    @TableField(value = "xwType3")
    private String xwType3;


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

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    public String getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(String sharePercent) {
        this.sharePercent = sharePercent;
    }

    public String getParentChainCode() {
        return parentChainCode;
    }

    public void setParentChainCode(String parentChainCode) {
        this.parentChainCode = parentChainCode;
    }

    public String getXwType3Code() {
        return xwType3Code;
    }

    public void setXwType3Code(String xwType3Code) {
        this.xwType3Code = xwType3Code;
    }

    public String getXwType3() {
        return xwType3;
    }

    public void setXwType3(String xwType3) {
        this.xwType3 = xwType3;
    }
}