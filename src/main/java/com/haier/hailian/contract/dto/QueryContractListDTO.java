package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class QueryContractListDTO {

    private String chainCode;

    private String startDate;

    private String endDate;

    private String status;

    private String userCode;

    private String[] chainCodeList;

    private String littleXwCode;

    private String nextMonth;

}
