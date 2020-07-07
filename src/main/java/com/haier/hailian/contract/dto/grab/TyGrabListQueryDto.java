package com.haier.hailian.contract.dto.grab;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/29.
 */
@Data
public class TyGrabListQueryDto {
    private String startDate;
    private String endDate;
    //根据名称查询
    private String name;
    private List<String> chainCodes;
    private String empSn;
    @ApiModelProperty(value = "登陆人作战单元编码", required = false)
    private String loginBuCode;

    private  String defaultOrder;
}
