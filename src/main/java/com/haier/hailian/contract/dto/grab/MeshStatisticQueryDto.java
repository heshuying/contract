package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by 19012964
 */
@Data
@ApiModel(value = "网格抢单查询实体类")
public class MeshStatisticQueryDto {
    private String xwCode;
    private Integer contractId;
    private String modelCode;
    private String ptCode;

    private String year;
    private List<String> month;
    private List<String> yearMonth;
    private String orderFiled;
    private boolean isDesc =false; //desc

}
