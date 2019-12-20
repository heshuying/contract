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
    @ApiModelProperty(value="chainName",name="链群名称")
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
    //链群对应的节点分享比例和目标数据
    private List<ZNodeTargetPercentInfo> zNodeTargetPercentInfos ;
}
