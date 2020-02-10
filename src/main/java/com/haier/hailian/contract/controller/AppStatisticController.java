package com.haier.hailian.contract.controller;


import com.haier.hailian.contract.dto.AppStatDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.entity.AppStatistic;
import com.haier.hailian.contract.service.AppStatisticService;
import com.haier.hailian.contract.util.Constant;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 19012964
 * @since 2020-02-10
 */
@RestController
@RequestMapping("")
public class AppStatisticController {
    @Autowired
    private AppStatisticService appStatisticService;

    @PostMapping(value = "/appStatistic")
    @ApiOperation(value = "PV统计")
    public R appStatistic(@RequestBody AppStatDto dto) {
        if(dto==null){
            throw new RException(Constant.MSG_VALIDFAIL,Constant.CODE_VALIDFAIL);
        }
        AppStatistic appStatistic=new AppStatistic();
        appStatistic.setCreateTime(new Date());
        appStatistic.setEmpSn(dto.getEmpSn());
        appStatistic.setPage(dto.getPage());
        appStatistic.setSource(dto.getSource());
        appStatisticService.save(appStatistic);
        return R.ok();
    }

}

