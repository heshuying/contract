package com.haier.hailian.contract.dto.grab;

import com.haier.hailian.contract.dto.FactorDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by 19012964
 */
@Data
@ApiModel(value = "网格抢单查询实体类")
public class TyMasterGrabQueryDto {
    @ApiModelProperty(value = "合约id", required = true)
    private Integer contractId;
    @ApiModelProperty(value = "指标维度", required = true)
    private List<FactorDto> factorDtos;
    @ApiModelProperty(value = "合约创建人", required = false)
    private String contractOwner;
    @ApiModelProperty(value = "登陆人xw编码", required = false)
    private String loginXwCode;
    private String year;
    private List<String> month;
    private List<String> yearMonth;
}
