package com.haier.hailian.contract.dto.grab;

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
    @ApiModelProperty(value = "合约创建人", required = false)
    private String contractOwner;

    private String year;
    private List<String> month;
    private List<String> yearMonth;
}
