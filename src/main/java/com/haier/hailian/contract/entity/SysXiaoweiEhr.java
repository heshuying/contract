package com.haier.hailian.contract.entity;

import lombok.Data;

@Data
public class SysXiaoweiEhr {
    private Integer id;
    /**
     * 小微编码
     */
    private String xwcode;
    /**
     * 小微名称
     */
    private String xwname;
    /**
     * 小微主工号
     */
    private String xwmastercode;
    /**
     * 小微主姓名
     */
    private String xwmastername;
    /**
     * 平台编码
     */
    private String ptcode;
    /**
     * 平台名称
     */
    private String ptname;
    /**
     * 领域编码
     */
    private String bucode;
    /**
     * 领域名称
     */
    private String buname;
    /**
     * 链群类型编码  例:1 2 1,2   1：创单  2：体验
     */
    private String xwgrouptype;
    /**
     * 链群类型名称 例:创单 体验 创单,体验
     */
    private String xwgroupname;
    /**
     * 小微行业
     */
    private String xwstyle;
    /**
     * 小微行业编码
     */
    private String xwstylecode;
}