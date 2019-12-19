package com.haier.hailian.contract.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "网格抢单明细数据")
public class MeshGrabEntity {
    private  String meshCode;
    private String meshName;
    private String productCode;
    private String productStru;

    //抢单收入
    private String income;
}
