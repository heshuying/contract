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
    @ApiModelProperty(value = "小微编码", required = true)
    private String xwCode;
    @ApiModelProperty(value = "合约id", required = true)
    private Integer contractId;

    private String year;
    private List<String> month;
    private List<String> yearMonth;
}
