package com.haier.hailian.contract.dto;

import com.haier.hailian.contract.entity.TargetBasic;
import lombok.Data;
import java.util.List;

@Data
public class TargetBasicInfo extends TargetBasic {

    private List<TargetBasic> childTargetBasicList;

}
