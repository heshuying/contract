package com.haier.hailian.contract.dto;

import lombok.Data;

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
     * 年度数量
     */
    private Integer qtyYear;

    /**
     * 月度数量
     */
    private Integer qtyMonth;

}
