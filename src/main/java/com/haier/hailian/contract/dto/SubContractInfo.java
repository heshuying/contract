package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自合约信息
 * @author 19033323
 */
@Data
public class SubContractInfo {
    @ApiModelProperty(value="子合约id",name="子合约id")
    private String contractId;
    @ApiModelProperty(value="链群名称",name="链群名称")
    private String chainName;
    @ApiModelProperty(value="体验抢单百分比",name="体验抢单百分比")
    private String countTY;
    @ApiModelProperty(value="创单抢单百分比",name="创单抢单百分比")
    private String countCD;
    @ApiModelProperty(value="爆款数",name="爆款数")
    private String countBK;
}
