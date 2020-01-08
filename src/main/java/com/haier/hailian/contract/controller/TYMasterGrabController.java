package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.service.GrabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public R chainInfo(@RequestBody TyGrabListQueryDto queryDto) {
        List<TyMasterGrabChainInfoDto> list=grabService.queryChainList(queryDto);
        return R.ok().put("data",list);
    }

    @GetMapping(value = {"/chain/my/{contractId}"})
    @ApiOperation(value = "体检链群主查看抢入之后的详情信息")
    public R chainInfo(@PathVariable(value = "contractId") Integer contractId) {
        TyMasterGrabChainInfoDto info=grabService.queryChainInfo(contractId);
        List<MeshGrabInfoDto>  list=grabService.queryMeshGrabDetail(contractId);
        Map<String, Object> data=new HashedMap();
        data.put("info", info);
        data.put("mesh", list);
        return R.ok().put("data",data);
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
