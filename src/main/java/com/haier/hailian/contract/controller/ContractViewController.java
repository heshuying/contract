package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ContractViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 19033323
 */
@RestController
@RequestMapping("/contractview")
@Api(value = "合约查看页面相关接口", tags = "合约查看页面相关接口")
public class ContractViewController {
    @Autowired
    ContractViewService contractViewService;

    @GetMapping(value = {"/getContractInfo/{contractId}"})
    @ApiOperation(value = "合约基础数据查询")
    public R getContractInfo(@PathVariable String contractId) {
        ContractViewResultDTO resultDTO = contractViewService.getContractViewData(contractId);
        return R.ok().put("data",resultDTO);
    }

    @GetMapping(value = {"/getContractInfoCD/{contractId}"})
    @ApiOperation(value = "合约创单数据查询")
    public R getContractInfoCD(@PathVariable String contractId) {
        List<ContractViewDataCD> resultList = contractViewService.getContractViewDataCD(contractId);
        String rate = contractViewService.getContractSize(contractId);
        return R.ok().put("data",resultList).put("grabPercent", rate);
    }

    @GetMapping(value = {"/getContractInfoTY/{contractId}"})
    @ApiOperation(value = "合约体验数据查询")
    public R getContractInfoTY(@PathVariable String contractId) {
        List<TargetTitleTYDTO> resultTitle = contractViewService.getTargetTitleList(contractId);
        List<ContractViewDataTYResultDTO> result = contractViewService.getContractViewDataTY(contractId);
        Integer size = contractViewService.getContractSize2(contractId);
        return R.ok().put("data",result).put("title", resultTitle).put("grabPercent", result.size() + "/" + size);
    }

}
