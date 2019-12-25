package com.haier.hailian.contract.dto.homepage;

import com.haier.hailian.contract.entity.ZReservePlanTeamworkDetail;
import lombok.Data;
import java.util.List;

@Data
public class PlanTeamWorkInfo {

    private String problemCode;

    private String taskCode;

    private List<ZReservePlanTeamworkDetail> details;

}
