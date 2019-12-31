package com.haier.hailian.contract.dto.grab;

import lombok.Data;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/29.
 */
@Data
public class TyGrabListQueryDto {
    private String startDate;
    private String endDate;
    private List<String> chainCodes;
    private String empSn;

}
