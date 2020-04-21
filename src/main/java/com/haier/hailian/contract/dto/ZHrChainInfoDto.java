package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by J.wind
 * At 2019-12-18 14:59
 * Email: jiangzd102@outlook.com
 */
@Data
@ApiModel(value = "ZHrChainInfoDto",description = "链群名称关键字")
public class ZHrChainInfoDto {
    public ZHrChainInfoDto(){}
    private int id;
    //链群编码
    private String chainCode;
    //链群名称
    @ApiModelProperty(value="chainName",name="链群名称")
    private String chainName;

    private String xwCode;
    //小微名称
    private String xwName;

    private String masterCode;
    //链群主姓名
    private String masterName;
    //链群平台code
    private String chainPtCode;
    //链群对应的节点分享比例和目标数据
    //群组ID
    private String groupId;
    //定位
    private String fixedPosition;
    private List<ZNodeTargetPercentInfo> zNodeTargetPercentInfos ;

    // XwType3选中数组
    private SaveXwType3 saveXwType3;

    private String zzfxRate;//增值分享比例

    private String cdShareRate; // 创单分享比例

    private String tyShareRate; // 体验分享比例

    private String parentCode; // 主链群编码

    private String parentName; // 主链群名称

    private List<ZHrChainInfoDto> zHrChainInfoDtos; //模块数据

    private String isModel; // 是否开启模块

    private String grabFlag; // 是否在oms抢单

    /* 2020年4月20日 10:33:38 */

    private String in_MAJOR_CLASS;  // 项目大类

    private String in_SUB_CLASS;  // 子项目

    private String in_CHAIN_ATTR;   //  链群属性

    private String in_SHORT_NAME;   //  简称



}
