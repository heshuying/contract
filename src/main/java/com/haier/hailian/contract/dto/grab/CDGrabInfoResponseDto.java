package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 19033323
 */
@Data
@ApiModel(value = "创单节点抢单页面返回数据")
public class CDGrabInfoResponseDto {
    @ApiModelProperty(value="分享比例",name="分享比例",example="5%")
    private String sharePercent;
//    @ApiModelProperty(value="链群目标",name="链群目标",example="400")
//    private String chainGoal;

    @ApiModelProperty(value="链群名称",name="链群名称")
    private String chainName;
    @ApiModelProperty(value="合约有效期开始",name="合约有效期开始")
    private String startTime;
    @ApiModelProperty(value="合约有效期结束",name="合约有效期结束")
    private String endTime;
    @ApiModelProperty(value="链群主编码",name="链群主编码")
    private String masterCode;
    @ApiModelProperty(value="链群主名字",name="链群主名字")
    private String masterName;
    @ApiModelProperty(value="小微编码",name="小微编码")
    private String xwCode;
    @ApiModelProperty(value="小微名字",name="小微名字")
    private String xwName;
    @ApiModelProperty(value="增值分享比例",name="增值分享比例",example="1%")
    private String zzfxRate;//增值分享比例（新增字段）
    private List<CDGrabTargetDto> targetList = new ArrayList<>();
}
