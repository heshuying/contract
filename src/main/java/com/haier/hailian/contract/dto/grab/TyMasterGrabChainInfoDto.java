package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "体验链群主抢单链群信息实体类")
public class TyMasterGrabChainInfoDto {
    private Integer contractId;
    private String chainName;
    private String start;
    private String end;
    private BigDecimal shareQuota;
    private String regionCode;
    //目标是底线收入
    private BigDecimal targetIncome;
    private BigDecimal targetHighPercent;
    private BigDecimal targetLowPercent;
}
