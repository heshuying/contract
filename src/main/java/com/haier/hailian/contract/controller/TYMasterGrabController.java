package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.service.GrabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Api(value = "体验链群主抢单", tags = {"体验链群主抢单"})
@RestController
@RequestMapping(value = "/tyGrab")
public class TYMasterGrabController {
    @Autowired
    private GrabService grabService;

    @PostMapping(value = {"/chain/list"})
    @ApiOperation(value = "体验链群主抢单列表")
    public R chainInfo() {
        List<TyMasterGrabChainInfoDto> list=grabService.queryChainList();
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/chain/info"})
    @ApiOperation(value = "体验链群主抢单链群信息")
    public R chainInfo(@RequestBody TyMasterGrabQueryDto queryDTO) {
        TyMasterGrabChainInfoDto chainInfoDto=grabService.queryChainInfo(queryDTO);
        return R.ok().put("data",chainInfoDto);
    }

    @PostMapping(value = {"/mesh/info"})
    @ApiOperation(value = "体验链群主抢单网格明细信息")
    public R meshInfo(@RequestBody TyMasterGrabQueryDto queryDTO) {
        List<MeshGrabInfoDto> list=grabService.queryMeshGrabDetail(queryDTO);
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/grab"})
    @ApiOperation(value = "体验链群主抢单")
    public R grab(@RequestBody @Validated MessGambSubmitDto dto) {
        return grabService.doGrab(dto);
    }


}
