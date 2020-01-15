package com.haier.hailian.contract.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExportChainUnitInfo {

    private String littleXwCode;

    private String littleXwName;

    private BigDecimal sharePercent;

}
