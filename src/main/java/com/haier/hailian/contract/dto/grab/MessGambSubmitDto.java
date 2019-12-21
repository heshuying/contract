package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by 19012964
 * 网格抢单提交
 */
@Data
@ApiModel(value = "网格抢单提交实体类")
public class MessGambSubmitDto {
    MeshSummaryDto meshSummaryDto;
    TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto;
    
}
