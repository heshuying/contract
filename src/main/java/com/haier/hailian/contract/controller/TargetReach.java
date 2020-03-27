package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.service.TargetReachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 19033323
 * 目标达成
 */
@Slf4j
@RestController
@RequestMapping("/targetReach")
@Api(value = "目标达成", tags = "目标达成")
public class TargetReach {
    @Autowired
    TargetReachService targetReachService;

    @PostMapping(value = {"/contractListForTarget"})
    @ApiOperation(value = "目标达成合约列表")
    public R selectContractListForTarget(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = targetReachService.selectContractListForTarget(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/list"})
    @ApiOperation(value = "目标达成列表")
    public R queryInfo(@RequestBody Map<String,String> reqBean) {
        List<TargetListResDTO> data= new ArrayList<>();
        try {
            if(StringUtils.isBlank(reqBean.get("contractId"))){
                return R.error("请求参数错误");
            }
            data = targetReachService.getFactorGrabList(reqBean.get("contractId"));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",data);
    }

    @PostMapping(value = {"/saveTarget"})
    @ApiOperation(value = "目标达成保存")
    public R saveTarget(@RequestBody List<TargetReachSaveReqDTO> reqBean) {
        if(reqBean == null || reqBean.isEmpty()){
            return R.error("没有要更新的数据");
        }

        try {
            targetReachService.saveTargetActual(reqBean);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("保存失败: " + e.getMessage());
        }
        return R.ok();
    }
}
