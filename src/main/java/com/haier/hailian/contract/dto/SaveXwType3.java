package com.haier.hailian.contract.dto;


import lombok.Data;

import java.util.List;

@Data
public class SaveXwType3 {

    //链群编码
    private String lqCode;
    //链群名称
    private String lqName;
    //模块 - 主链群编码
    private String parentChainCode;
    // 新增小微类型3数组
    private List<XwType3Info> XwType3List;
    // 链群所在平台
    private String ptCode;

}
