package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.entity.TargetBasic;
import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2019/12/28.
 */
@Data
public class MarketReturnDTO {
    private List<TOdsMinbu> market;
    private List<TargetBasic> targetRequired;
    private List<TargetBasic> target;

}
