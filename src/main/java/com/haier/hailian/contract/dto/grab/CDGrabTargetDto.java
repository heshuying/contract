package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "创单节点抢单页面目标数据")
public class CDGrabTargetDto {
    @ApiModelProperty(value="目标名称",name="目标名称")
    private String targetName;
    @ApiModelProperty(value="目标编码",name="目标编码")
    private String targetCode;
    @ApiModelProperty(value="链群目标",name="链群目标",example="400")
    private BigDecimal chainGoal;
    @ApiModelProperty(value="链群抢单目标",name="链群抢单目标",example="450")
    private BigDecimal chainGrabGoal;
    @ApiModelProperty(value="目标单位",name="目标单位",example="元")
    private String targetUnit;
    @ApiModelProperty(value="目标方向（1：正向；0：负向）",name="目标方向")
    private String targetTo;
}
