package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.service.ContractViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

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
        Collection<ContractViewDataCDResponseDTO> resultList = contractViewService.getContractViewDataCD(contractId);
        String rate = contractViewService.getContractSize(contractId);
        return R.ok().put("data",resultList).put("grabPercent", rate);
    }

    @GetMapping(value = {"/getContractInfoTY/{contractId}/{orderType}"})
    @ApiOperation(value = "合约体验数据查询")
    public R getContractInfoTY(@PathVariable String contractId, @PathVariable String orderType) {
        List<TargetTitleTYDTO> resultTitle = contractViewService.getTargetTitleList(contractId);
        List<ContractViewDataTYResultDTO> result = contractViewService.getContractViewDataTY(contractId, orderType);
        Integer size = contractViewService.getContractSize2(contractId);
        return R.ok().put("data",result).put("title", resultTitle).put("grabPercent", result.size() + "/" + size);
    }

}
