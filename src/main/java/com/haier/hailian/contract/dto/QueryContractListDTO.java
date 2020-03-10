package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * Created by 01431594 on 2019/12/20.
 */
@Data
public class QueryContractListDTO {

    private String chainCode;

    private String contractName;

    private String startDate;

    private String endDate;

    private String status;

    private String userCode;

    private String[] chainCodeList;

    private String littleXwCode;

    private String nextMonth;

    private Integer parentId;

    private Double startShare;

    private Double endShare;

    private String paixu;//1.分享额升序  2.分享额降序 3.合约开始升序 4 合约开始降序

}
