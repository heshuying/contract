package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by J.wind
 * At 2019-12-18 14:59
 * Email: jiangzd102@outlook.com
 */
@Data
@ApiModel(value = "ZHrChainInfoDto",description = "链群名称关键字")
public class ZHrChainInfoDto {
    @ApiModelProperty(value="chainName",name="链群名称")
    private String chainName;//合约名称
}
