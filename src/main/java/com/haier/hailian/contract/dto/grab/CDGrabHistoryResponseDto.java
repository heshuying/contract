package com.haier.hailian.contract.dto.grab;

import com.haier.hailian.contract.dto.ReservePlanResultDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 19033323
 */
@Data
@ApiModel(value = "创单节点抢单查看页面返回数据")
public class CDGrabHistoryResponseDto {
    @ApiModelProperty(value="抢单id",name="抢单id")
    private String grabId;
    @ApiModelProperty(value="创建时间",name="创建时间")
    private String createTime;
    @ApiModelProperty(value="状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已过期，5:已撤销，6：已删除",name="状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已过期，5:已撤销，6：已删除")
    private String status;
    @ApiModelProperty(value="预案信息",name="预案信息")
    private List<ReservePlanResultDTO> planList = new ArrayList<>();
    @ApiModelProperty(value="目标列表",name="目标列表")
    private List<CDGrabTargetDto> targetList = new ArrayList<>();
}
