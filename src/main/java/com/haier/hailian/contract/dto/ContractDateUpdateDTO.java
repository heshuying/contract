package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * Created by 01431594 on 2020/6/9.
 */
@Data
public class ContractDateUpdateDTO {

    private Integer id;

    /**
     * 抢入截止时间
     */
    private String joinTime;
    //链群主复核分享比例截止时间
    private String checkTime;

}
