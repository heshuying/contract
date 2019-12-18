package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "网格抢单明细数据")
public class MeshGrabInfoDto {
    private  String meshCode;
    private String meshName;
    //抢单收入
    private BigDecimal income;
    private BigDecimal struHighPercent;
    private BigDecimal struLowPercent;

}
