package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.XiaoweiEhr;
import lombok.Data;

import java.util.List;

/**
 * Created by 01431594 on 2019/12/28.
 */
@Data
public class MarketReturnDTO {
    private List<XiaoweiEhr> market;
    private List<TargetBasic> targetRequired;
    private List<TargetBasic> target;

}
