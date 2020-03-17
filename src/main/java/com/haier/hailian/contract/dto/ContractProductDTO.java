package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2020/1/8.
 */
@Data
public class ContractProductDTO {

    /**
     * 产品系列
     */
    private String productSeries;

    /**
     * 场景
     */
    private String sceneName;

    private List<ProductTargetDTO> targetList;


}
