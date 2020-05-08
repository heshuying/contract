package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
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

    @PostMapping(value = {"/selectProductTarget"})
    @ApiOperation(value = "事中显差查询爆款举单、抢单目标")
    public R selectProductTarget(@RequestBody EventMiddleDTO dto) {

        List<ContractProductDTO> list = eventMiddleService.selectProductTarget(dto);
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/selectChainTargetTrend"})
    @ApiOperation(value = "事中显差查询链群目标趋势")
    public R selectChainTargetTrend(@RequestBody EventMiddleDTO dto) {

        List<EventMiddleTrendDTO> list = eventMiddleService.selectChainTargetTrend(dto);
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/selectChainShare"})
    @ApiOperation(value = "事中显差查询链群增值分享")
    public R selectChainShare(@RequestBody EventMiddleDTO dto) {

        EventMiddleChainShareDTO shareDTO = eventMiddleService.selectChainShare(dto);
        return R.ok().put("data",shareDTO);
    }

    @PostMapping(value = {"/selectChainShareTrend"})
    @ApiOperation(value = "事中显差查询链群增值分享趋势")
    public R selectChainShareTrend(@RequestBody EventMiddleDTO dto) {

        List<EventMiddleTrendDTO> list = eventMiddleService.selectChainShareTrend(dto);
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/selectCdShare"})
    @ApiOperation(value = "事中显差查询创单链群增值分享")
    public R selectCdShare(@RequestBody EventMiddleDTO dto) {

        List<EventMiddleCdDTO> list = eventMiddleService.selectCdShare(dto);
        return R.ok().put("data",list);
    }



}
