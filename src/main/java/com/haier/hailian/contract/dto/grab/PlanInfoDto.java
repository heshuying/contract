package com.haier.hailian.contract.dto.grab;

import com.haier.hailian.contract.entity.ZReservePlan;
import lombok.Data;

@Data
public class PlanInfoDto extends ZReservePlan {

    public String content;

    public String contractId;
}
