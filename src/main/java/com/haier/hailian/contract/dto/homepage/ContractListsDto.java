package com.haier.hailian.contract.dto.homepage;

import lombok.Data;

import java.util.Date;

@Data
public class ContractListsDto {

    private String empSN;

    private String empName;

    private String role;

    private Date startTime;

    private Date endTime;

}
