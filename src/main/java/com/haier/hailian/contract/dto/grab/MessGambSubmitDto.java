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
    private String xwCode;
    private Integer contractId;
    //当前操作人登陆选择的组织编码
    private String loginXwcode;
    private String loginXwName;
    private String orgType;
    //当前操作市场小微编码
    private String orgCode;
    private String orgName;
    private String year;
    private String month;
    private String yearMonth;
}
