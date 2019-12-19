package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "网格抢单查询实体类")
public class MeshSummaryDto {
    private String xwcode;
    private BigDecimal income;
    private BigDecimal struHighPercent;
    private BigDecimal struLowPercent;
    List<MeshGrabInfoDto> meshDetail;
}
