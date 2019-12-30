package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.ZContractsFactor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 19033323
 */
@Data
public class ContractViewResultDTO {
    @ApiModelProperty(value="链群名称",name="链群名称")
    private String chainName;
    @ApiModelProperty(value="链群主名字",name="链群主名字")
    private String chainMasterName;
    @ApiModelProperty(value="开始时间",name="开始时间")
    private String startDate;
    @ApiModelProperty(value="结束时间",name="结束时间")
    private String endDate;
    @ApiModelProperty(value="分享空间",name="分享空间")
    private BigDecimal shareSpace;
    @ApiModelProperty(value="状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已过期",name="状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已过期")
    private String status;
    @ApiModelProperty(value="状态名称",name="状态名称")
    private String statusName;
    @ApiModelProperty(value="底线目标",name="底线目标")
    private List<ZContractsFactor> bottomList;
    @ApiModelProperty(value="e2e目标",name="e2e目标")
    private List<ZContractsFactor> e2eList;
    @ApiModelProperty(value="抢单目标",name="抢单目标")
    private List<ZContractsFactor> grabList;
    @ApiModelProperty(value="体验抢单目标",name="体验抢单目标")
    private List<FactorConfigDTO> grabTYList;

}
