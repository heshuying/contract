package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.service.ContractViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
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
        Collection<ContractViewDataCDResponseDTO> resultList = contractViewService.getContractViewDataCD(contractId, null);
        String rate = contractViewService.getContractSize(contractId);
        return R.ok().put("data",resultList).put("grabPercent", rate);
    }

    @PostMapping(value = {"/getType3List"})
    @ApiOperation(value = "合约创单数据查询")
    public R getType3List(@RequestBody Map<String,String> paraMap) {
        if(paraMap.get("contractId") == null){
            return R.error("请求参数错误，有为空的字段");
        }
        int countGrabed = 0;
        int countTotal = 0;
        List<CDGrabType3> type3List = contractViewService.queryCDGrabDataXWType3(paraMap.get("contractId"));
        if(type3List == null || type3List.isEmpty()){

        }

        return R.ok().put("data", type3List);
    }

    @PostMapping(value = {"/getType3GrabList"})
    @ApiOperation(value = "合约创单数据查询")
    public R getType3GrabList(@RequestBody Map<String,String> paraMap) {
        if(paraMap.get("contractId") == null || paraMap.get("type3Code") == null){
            return R.error("请求参数错误，有为空的字段");
        }
        List<CDGrabDataDTO> list = contractViewService.queryGrabListXWType3(paraMap.get("contractId"), paraMap.get("type3Code"));

        return R.ok().put("data", list);
    }

    @GetMapping(value = {"/getContractInfoTY/{contractId}/{orderType}"})
    @ApiOperation(value = "合约体验数据查询")
    public R getContractInfoTY(@PathVariable String contractId, @PathVariable String orderType) {
        List<TargetTitleTYDTO> resultTitle = contractViewService.getTargetTitleList(contractId);
        List<ContractViewDataTYResultDTO> result = contractViewService.getContractViewDataTY(contractId, orderType);
        Integer size = contractViewService.getContractSize2(contractId);
        return R.ok().put("data",result).put("title", resultTitle).put("grabPercent", result.size() + "/" + size);
    }

    @PostMapping(value = {"/getContractInfoTYNew"})
    @ApiOperation(value = "合约体验数据查询new")
    public R getContractInfoTYNew(@RequestBody ContractViewRequestNewDTO requestBean) {
        if(requestBean == null || StringUtils.isBlank(requestBean.getContractId())){
            return R.error("请求参数错误，合约id为空");
        }
        Map<String, Object> result = contractViewService.getContractViewDataTYNew(requestBean);
        Integer grabSize = contractViewService.selectContractsViewForTYCount(requestBean.getContractId());
        Integer size = contractViewService.getContractSize2(requestBean.getContractId());
        return R.ok().put("data",result.get("data")).put("lessthanJDCount", result.get("lessthanJDCount")).put("lessthanE2ECount", result.get("lessthanE2ECount"))
                .put("grabedCount", result.get("grabedCount")).put("notGrabCount", result.get("notGrabCount")).put("grabPercent", grabSize + "/" + size);
    }


    @GetMapping(value = {"/serials/{contractId}"})
    @ApiOperation(value = "合约爆款数据统计")
    public R getContractInfo(@PathVariable Integer contractId) {
         List<ContractSerialDto> list = contractViewService.staticSerial(contractId);
        return R.ok().put("data",list);
    }

    @GetMapping(value = "/exportContract",headers="Accept=application/octet-stream")
    @ApiOperation(value = "导出合约抢单数据")
    public void exportContract(@RequestParam Integer contractId) throws IOException {
        contractViewService.exportContract(contractId);
    }

}
