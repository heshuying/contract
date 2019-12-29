package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class MarketTargetDTO {

    private String xwCode;

    private String xwName;

    private List<MarketTargetDTO2> targetList;
}
