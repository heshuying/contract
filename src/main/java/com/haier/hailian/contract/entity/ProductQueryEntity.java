package com.haier.hailian.contract.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by 19012964 on 2020/3/17.
 */
@Data
public class ProductQueryEntity {
    /**
     * 举单合约ID
     */
    private Integer contractId;

    /**
     * 年度数量
     */
    private int qtyYear;

    /**
     * 月度数量
     */
    private int qtyMonth;
}
