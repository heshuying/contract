package com.haier.hailian.contract.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 01440590 on 2019/6/19.
 */
@Data
@ApiModel(value = "ValidateChainNameDTO", description = "链群名称校验DTO")
public class ValidateChainNameDTO {
    public ValidateChainNameDTO(){}
    @ApiModelProperty(value = "id", name = "id")
    private Integer id;//合约id 新增是不传 编辑时必传

    @ApiModelProperty(value = "chainName", name = "链群名称")
    private String chainName;//合约名称

    @ApiModelProperty(value = "chainNameFlag", name = "连群名称标识")
    private String chainNameFlag;//合约名称标识 0:选择 1：编辑

    public ValidateChainNameDTO(String chainName) {
        this.chainName = chainName;
    }
}
