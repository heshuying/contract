package com.haier.hailian.contract.dto.homepage;

import lombok.Data;

import java.util.Date;

@Data
public class ChainGroupInfoDto {

    /**
     * 链群编码
     */
    private String chainCode;

    private Date startTime;

    private Date endTime;

}
