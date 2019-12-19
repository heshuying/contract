package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "创单节点抢单页面数据")
public class CDGrabInfoRequestDto {
    private Integer contractId;
    private String year;
    private String month;
    private String yearMonth;
}
