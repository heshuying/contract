package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 19033323
 */
@Data
@ApiModel(value = "创单节点抢单页面返回数据")
public class CDGrabInfoResponseDto {
    @ApiModelProperty(value="分享比例",name="分享比例",example="5%")
    private String sharePercent;
    @ApiModelProperty(value="达成目标预计分享酬",name="达成目标预计分享酬",example="3000")
    private String targetShareMoney;
    @ApiModelProperty(value="链群目标",name="链群目标",example="400")
    private String chainGoal;
    @ApiModelProperty(value="链群名称",name="链群名称")
    private String chainName;
    @ApiModelProperty(value="合约有效期开始",name="合约有效期开始")
    private String startTime;
    @ApiModelProperty(value="合约有效期结束",name="合约有效期结束")
    private String endTime;
}
