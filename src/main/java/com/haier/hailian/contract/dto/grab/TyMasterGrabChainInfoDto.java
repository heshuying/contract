package com.haier.hailian.contract.dto.grab;

import com.haier.hailian.contract.dto.FactorDto;
import com.haier.hailian.contract.entity.ZContractsFactor;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "体验链群主抢单链群信息实体类")
public class TyMasterGrabChainInfoDto {
    private Integer contractId;
    private String chainName;
    private String contractName;
    private String contractOwner;
    private String start;
    private String end;
    private String grabEnd;
    private BigDecimal shareQuota;
    private String regionCode;
    private String littleXwName;

    private Boolean canSubmit;
    private Boolean canEdit;

    private String errorMsg;

    List<FactorDto> targetList;
    List<FactorDto> grabList;
    List<FactorDto> e2eList;
}
