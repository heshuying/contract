package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.ContractViewDataCD;
import com.haier.hailian.contract.dto.ContractViewResultDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.R;
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
        return R.ok().put("data",resultList);
    }

    @GetMapping(value = {"/getContractInfoTY/{contractId}"})
    @ApiOperation(value = "合约体验数据查询")
    public R getContractInfoTY(@PathVariable String contractId) {
        Map<String, List<ZContractsFactor>> resultMap = contractViewService.getContractViewDataTY(contractId);
        return R.ok().put("data",resultMap);
    }

}
