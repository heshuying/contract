package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "体验链群主抢单链群信息实体类")
public class TyMasterGrabChainInfoDto {
    private String chainName;
    private String start;
    private String end;
    private String shareQuota;
    private String targetIncome;
    private String targetHighPercent;
    private String targetLowPercent;
}
