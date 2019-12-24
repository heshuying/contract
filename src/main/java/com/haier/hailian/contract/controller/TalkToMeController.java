package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.ZReservePlanDto;
import com.haier.hailian.contract.entity.ZReservePlanDetail;
import com.haier.hailian.contract.service.SysNodeEhrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 表控制层
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
@RestController
@RequestMapping("talk")
@Api(value = "并联协同预案", tags = {"交流群"})
public class TalkToMeController {
    /**
     * 服务对象
     */
    @Resource
    private SysNodeEhrService sysNodeEhrService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("getTalkCount")
    @ApiOperation(value = "获取协同预案的条数")
    public int selectOne(@RequestBody @ApiParam(value = "合约的ID", required = true) int id) {
        return 34;
    }


    @GetMapping("getTalkList")
    @ApiOperation(value = "获取并联协同预案列表")
    public List<ZReservePlanDto> getTalkList(@RequestBody @ApiParam(value = "合约的ID", required = true) int id) {
        List<ZReservePlanDto> list = new ArrayList<>();
        int a =1;
        for (int i = 0; i < 10; i++) {
            int p = i + 1;
            ZReservePlanDto zReservePlanDto = new ZReservePlanDto();
            zReservePlanDto.setId(p);
            zReservePlanDto.setOrderType("ABC");
            zReservePlanDto.setParentId(id);
            zReservePlanDto.setTitle("我就是问问你们第"+i+"个问题？");
            List<ZReservePlanDetail> zReservePlanDetails = new ArrayList<>();
            for (int j=0;j<5;j++){
                ZReservePlanDetail zReservePlanDetail = new ZReservePlanDetail();
                zReservePlanDetail.setContent("我告诉你答案"+a);
                zReservePlanDetail.setParentId(p);
                zReservePlanDetail.setId(a);
                zReservePlanDetails.add(zReservePlanDetail);
                a++;
            }
            zReservePlanDto.setDetails(zReservePlanDetails);
            list.add(zReservePlanDto);
        }
        return list;
    }

}