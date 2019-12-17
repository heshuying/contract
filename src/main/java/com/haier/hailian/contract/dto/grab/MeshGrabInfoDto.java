package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "网格抢单明细数据")
public class MeshGrabInfoDto {
    private  String meshCode;
    private String meshName;
    private String income;
    private String struHighPercent;
    private String struLowPercent;

}
