package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Data
@ApiModel(value = "网格抢单查询实体类")
public class MeshSummaryDto {
    private String xwcode;
    private String income;
    private String struHighPercent;
    private String struLowPercent;
    List<MeshGrabInfoDto> meshDetail;
}
