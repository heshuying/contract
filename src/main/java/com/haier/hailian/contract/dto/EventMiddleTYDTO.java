package com.haier.hailian.contract.dto;

import lombok.Data;

/**
 * Created by 01431594 on 2020/4/14.
 */
@Data
public class EventMiddleTYDTO {

    private String littleXwName;//最小作战单元名字
    private String xwName;//小微名字
    private String userName;//负责人名字
    private String jdIncome;//举单收入
    private String qdIncome;//抢单收入
    private String jdHigh;//举单高端占比
    private String qdHigh;//抢单高端占比
    private String pjM;//五星评价

}
