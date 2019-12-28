package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * Created by 19012964 on 2019/12/28.
 */
@Data
public class FactorDto {
    /**
     * 编码
     */
    private String factorCode;

    /**
     * 名称
     */
    private String factorName;

    /**
     * 值
     */
    private String factorValue;


    /**
     * 单位
     */
    private String factorUnit;

    private Boolean hasInput;

}
