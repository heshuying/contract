package com.haier.hailian.contract.entity;

import lombok.Data;

@Data
public class SysNodeEhr {
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
     * 节点编码
     */
    private String nodecode;
    /**
     * 节点名称
     */
    private String nodename;
    /**
     * 节点直线工号
     */
    private String nodemanagercode;
    /**
     * 节点直线姓名
     */
    private String nodemanagername;
    /**
     * 节点直线岗位编码
     */
    private String nodemanagerpos;
    private String ptcode;
    private String ptname;

    //数据类型（01：ODS自动更新数据；02：自定义数据）
    private String dataType;

}