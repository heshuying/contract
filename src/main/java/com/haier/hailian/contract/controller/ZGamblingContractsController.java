package com.haier.hailian.contract.controller;


import com.haier.hailian.contract.dto.GamblingContractDTO;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.entity.XiaoweiEhr;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 01431594
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/zGamblingContracts")
@Api(value = "链群主抢单（举单）接口", tags = "链群主抢单（举单）接口")
public class ZGamblingContractsController {

    @Autowired
    private ZGamblingContractsService gamblingContractsService;

    @PostMapping(value = {"/saveGambling"})
    @ApiOperation(value = "链群主抢单（举单）信息保存")
    public R selectBottom(@RequestBody GamblingContractDTO dto) {
        gamblingContractsService.saveGambling(dto);
        return R.ok();
    }

    @PostMapping(value = {"/selectMarket"})
    @ApiOperation(value = "查询42市场小微的名字")
    public R selectMarket() {
        List<XiaoweiEhr> list = gamblingContractsService.selectMarket();
        return R.ok().put("data",list);
    }

}

