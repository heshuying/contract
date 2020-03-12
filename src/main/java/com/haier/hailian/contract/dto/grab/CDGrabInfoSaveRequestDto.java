package com.haier.hailian.contract.dto.grab;

import com.haier.hailian.contract.dto.ReservePlanRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 19033323
 */
@Data
@ApiModel(value = "创单节点抢单保存")
public class CDGrabInfoSaveRequestDto {
    @ApiModelProperty(value="是否更新：0 保存 1 更新, 默认 0",name="是否更新：0 保存 1 更新, 默认 0")
    private String isUpdate = "0";
    @ApiModelProperty(value="合约id",name="合约id")
    private Integer contractId;
    @ApiModelProperty(value="分享比例",name="分享比例",example="5%")
    private String sharePercent;
    @ApiModelProperty(value="达成目标预计分享酬",name="达成目标预计分享酬",example="3000")
    private String targetShareMoney;
    @ApiModelProperty(value="xwType3编码",name="xwType3编码")
    private String xwType3Code;
    @ApiModelProperty(value="xwType3名字",name="xwType3名字")
    private String xwType3;
//    @ApiModelProperty(value="链群目标",name="链群目标",example="400")
//    private BigDecimal chainGoal;
//    @ApiModelProperty(value="链群抢单目标",name="链群抢单目标",example="450")
//    private BigDecimal chainGrabGoal;
//    @ApiModelProperty(value="预案标题",name="预案标题")
//    private String planTitle;
//    @ApiModelProperty(value="预案内容",name="预案内容")
//    private String planContent;
    @ApiModelProperty(value="目标列表",name="目标列表")
    private List<CDGrabTargetDto> targetList;
    @ApiModelProperty(value="预案信息",name="预案信息")
    private List<ReservePlanRequestDTO> planInfo;

}
