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
public class MeshStatisticQueryDto {
    @ApiModelProperty(value = "小微编码", required = true)
    private String xwCode;
    @ApiModelProperty(value = "合约id", required = true)
    private Integer contractId;
    @ApiModelProperty(value = "型号编码", required = false)
    private String modelCode;
    @ApiModelProperty(value = "平台编码", required = false)
    private String ptCode;
}
