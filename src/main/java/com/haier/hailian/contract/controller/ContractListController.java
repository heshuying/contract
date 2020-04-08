package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO2;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.entity.ZContracts;
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
 * Created by 01431594 on 2019/12/29.
 */
@RestController
@RequestMapping("/contractList")
@Api(value = "合约列表相关接口", tags = "合约列表相关接口")
public class ContractListController {

    @Autowired
    private ZGamblingContractsService gamblingContractsService;

    @PostMapping(value = {"/selectMyStartContract"})
    @ApiOperation(value = "查询我发起的合约列表")
    public R selectMyStartContract(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectMyStartContract(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/selectMyGrabContract"})
    @ApiOperation(value = "查询我抢入的合约列表")
    public R selectMyGrabContract(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectMyGrabContract(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/selectToGrabContract"})
    @ApiOperation(value = "查询待抢入的合约列表")
    public R selectToGrabContract(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectToGrabContract(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/selectHomePageContract"})
    @ApiOperation(value = "首页查询合约列表")
    public R selectHomePageContract(@RequestBody QueryContractListDTO2 queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectHomePageContract(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/selectAllGrabContract"})
    @ApiOperation(value = "事中显差，查询登录人所在最小作战单元抢入的所有合约")
    public R selectAllGrabContract(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectAllGrabContract(queryDTO);
        return R.ok().put("data",contractsList);
    }

}
