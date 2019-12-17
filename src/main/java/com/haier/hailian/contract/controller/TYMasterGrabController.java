package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.service.GrabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Api(value = "体验链群主抢单", tags = {"体验链群主抢单"})
@RestController(value = "/tyMasterGrab")
public class TYMasterGrabController {
    @Autowired
    private GrabService grabService;

    @PostMapping(value = {"/chain/info"})
    @ApiOperation(value = "体验链群主抢单链群信息")
    public R meshDetail(@RequestBody @Validated MeshStatisticQueryDto queryDTO) {
        TyMasterGrabChainInfoDto chainInfoDto=grabService.queryChainInfo(queryDTO);
        return R.ok().put("data",chainInfoDto);
    }

    @PostMapping(value = {"/mesh/info"})
    @ApiOperation(value = "体验链群主抢单网格明细信息")
    public R meshStatistic(@RequestBody @Validated MeshStatisticQueryDto queryDTO) {
        MeshSummaryDto meshSummaryDto=grabService.queryMeshGrabDetail(queryDTO);
        return R.ok().put("data",meshSummaryDto);
    }
}
