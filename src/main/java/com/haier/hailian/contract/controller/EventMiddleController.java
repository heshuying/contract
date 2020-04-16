package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.EventMiddleCdDTO;
import com.haier.hailian.contract.dto.EventMiddleDTO;
import com.haier.hailian.contract.dto.EventMiddleTYDTO;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.EventMiddleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 01431594 on 2020/4/10.
 */
@RestController
@RequestMapping("/eventMiddle")
@Api(value = "事中显差相关接口", tags = "事中显差相关接口")
public class EventMiddleController {

    @Autowired
    private EventMiddleService eventMiddleService;

    @PostMapping(value = {"/selectChainTarget"})
    @ApiOperation(value = "事中显差查询链群目标")
    public R selectChainTarget(@RequestBody EventMiddleDTO dto) {

         List<ZContractsFactor> list = eventMiddleService.selectChainTarget(dto);
         return R.ok().put("data",list);
    }

    @PostMapping(value = {"/selectTyTarget"})
    @ApiOperation(value = "事中显差查询体验链群抢单目标")
    public R selectTyTarget(@RequestBody EventMiddleDTO dto) {

        List<EventMiddleTYDTO> list = eventMiddleService.selectTyTarget(dto);
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/selectCdTarget"})
    @ApiOperation(value = "事中显差查询创单链群抢单目标")
    public R selectCdTarget(@RequestBody EventMiddleDTO dto) {

        List<EventMiddleCdDTO> list = eventMiddleService.selectCdTarget(dto);
        return R.ok().put("data",list);
    }


}
