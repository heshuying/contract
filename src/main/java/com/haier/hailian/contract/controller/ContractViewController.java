package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.service.ContractViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 19033323
 */
@Slf4j
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
        String orderStr = "desc";
        if(paraMap.get("contractId") == null){
            return R.error("请求参数错误，有为空的字段");
        }
        if(!StringUtils.isBlank(paraMap.get("orderStr"))){
            orderStr = paraMap.get("orderStr");
        }

        int countGrabed = 0;
        List<CDGrabType3> type3List = contractViewService.queryCDGrabDataXWType3(paraMap.get("contractId"), paraMap.get("keyword"));
        if(type3List != null && !type3List.isEmpty()){
            for(CDGrabType3 item : type3List){
                List<CDGrabDataDTO> list = contractViewService.queryGrabListXWType3(paraMap.get("contractId"), item.getXwType3Code());
                if(list != null && !list.isEmpty()){
                    item.setGrabCount(String.valueOf(list.size()));
                    countGrabed++;
                }else{
                    item.setGrabCount("0");
                }
            }
        }

        // 排序
        if("asc".equalsIgnoreCase(orderStr)){
            Collections.sort(type3List, new Comparator<CDGrabType3>() {
                @Override
                public int compare(CDGrabType3 o1, CDGrabType3 o2) {
                    if(Integer.parseInt(o2.getGrabCount()) > Integer.parseInt(o1.getGrabCount())){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });
        }else if("desc".equalsIgnoreCase(orderStr)){
            Collections.sort(type3List, new Comparator<CDGrabType3>() {
                @Override
                public int compare(CDGrabType3 o1, CDGrabType3 o2) {
                    if(Integer.parseInt(o2.getGrabCount()) > Integer.parseInt(o1.getGrabCount())){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }

        return R.ok().put("data", type3List).put("grabRate", countGrabed+"/"+type3List.size());
    }

    @PostMapping(value = {"/updateSharePercent"})
    @ApiOperation(value = "更新分享比例")
    public R updateSharePercent(@RequestBody List<Map<String,String>> paramList) {
        if(paramList == null || paramList.isEmpty()){
            return R.error("没有传要修改分享比例的数据");
        }

        try {
            for(Map<String,String> map : paramList){
                contractViewService.updateCDSharePercent(map.get("contractId"), map.get("sharePercent"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改分享比例失败: " + e.getMessage());
            return R.error("修改分享比例失败");
        }
        return R.ok();
    }

    @PostMapping(value = {"/getType3GrabList"})
    @ApiOperation(value = "合约创单数据查询")
    public R getType3GrabList(@RequestBody Map<String,String> paraMap) {
        if(paraMap.get("contractId") == null || paraMap.get("xwType3Code") == null){
            return R.error("请求参数错误，有为空的字段");
        }
        List<CDGrabDataDTO> list = contractViewService.queryGrabListXWType3(paraMap.get("contractId"), paraMap.get("xwType3Code"));

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
         List<ContractProductDTO> list = contractViewService.staticSerial(contractId);
        return R.ok().put("data",list);
    }

    @GetMapping(value = "/exportContract",headers="Accept=application/octet-stream")
    @ApiOperation(value = "导出合约抢单数据")
    public void exportContract(@RequestParam Integer contractId) throws IOException {
        contractViewService.exportContract(contractId);
    }

    /**
     * 批量计算分享筹并保存
     * @param paramBean
     * @return
     */
    @PostMapping(value = {"/updateSharePercentByHand"})
    public R updateSharePercentByHand(@RequestBody Map<String,String> paramBean) {
        try {
            contractViewService.updateCDShareSpace(paramBean.get("contractId"));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("计算更新分享筹异常");
        }
        return R.ok();
    }

}
