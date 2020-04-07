package com.haier.hailian.contract.dto.homepage;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChainDataInfo {

    private String chainCode;

    private String chainName;

    private String xwCode;

    private String xwName;

    private String masterCode;

    private String masterName;

    private String chainPtCode;

    //是否删除
    private int deleted;

    //群组ID
    private String groupId;

    //定位
    private String fixedPosition;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String zzfxRate;//增值分享比例

    private String cdShareRate; // 创单分享比例

    private String tyShareRate; // 体验分享比例

    private String parentCode; //主链群编码

    private String grabFlag; // 是否在oms抢单
}
