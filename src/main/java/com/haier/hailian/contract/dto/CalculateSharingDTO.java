package com.haier.hailian.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class CalculateSharingDTO {

    private String chainCode;
    private List<ChainGroupTargetDTO2> targetList;
}
