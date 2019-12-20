package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 19033323
 */
@Data
@ApiModel(value = "创单节点抢单保存")
public class CDGrabInfoSaveRequestDto {
    @ApiModelProperty(value="合约id",name="合约id")
    private Integer contractId;
    @ApiModelProperty(value="分享比例",name="分享比例",example="5%")
    private String sharePercent;
    @ApiModelProperty(value="达成目标预计分享酬",name="达成目标预计分享酬",example="3000")
    private String targetShareMoney;
    @ApiModelProperty(value="链群目标",name="链群目标",example="400")
    private String chainGoal;
    @ApiModelProperty(value="链群抢单目标",name="链群抢单目标",example="450")
    private String chainGrabGoal;
    @ApiModelProperty(value="预案标题",name="预案标题")
    private String planTitle;
    @ApiModelProperty(value="预案内容",name="预案内容")
    private String planContent;
}
