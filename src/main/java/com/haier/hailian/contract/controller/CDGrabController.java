package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.*;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.service.IncrementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 19033323
 */
@Slf4j
@Api(value = "创单节点抢单", tags = {"创单节点抢单"})
@RestController
@RequestMapping(value = {"/cdGrab"})
public class CDGrabController {

    @Autowired
    CDGrabService cdGrabService;
    @Autowired
    private IncrementService incrementService;

    @PostMapping(value = {"/info"})
    @ApiOperation(value = "创单节点抢单页面信息接口")
    public R queryInfo(@RequestBody CDGrabInfoRequestDto requestDto) {
        CDGrabInfoResponseDto data= null;
        try {
            if(StringUtils.isBlank(requestDto.getYearMonth()) && StringUtils.isNotBlank(requestDto.getYear()) && StringUtils.isNotBlank(requestDto.getMonth())){
                requestDto.setYearMonth(requestDto.getYear()+requestDto.getMonth());
            }
            data = cdGrabService.queryCDGrabInfo(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",data);
    }

    @PostMapping(value = {"/save"})
    @ApiOperation(value = "创单节点抢单页面数据保存接口")
    public R saveGrab(@RequestBody CDGrabInfoSaveRequestDto requestDto) {
        if(requestDto.getTargetList() == null || requestDto.getTargetList().isEmpty()){
            return R.error("请求参数错误，有为空的字段");
        }
//        for(CDGrabTargetDto target : requestDto.getTargetList()){
//            if(target.getTargetTo().equals("1") && target.getChainGrabGoal().compareTo(target.getChainGoal()) < 0){
//                return R.error("抢单目标需要大于底线目标");
//            }else if(target.getTargetTo().equals("0") && target.getChainGrabGoal().compareTo(target.getChainGoal()) > 0){
//                return R.error("抢单目标需要小于底线目标");  
//            }
//        }

        try {
            if("1".equals(requestDto.getIsUpdate())){
                cdGrabService.updateCDGrab(requestDto);
            }else {
                cdGrabService.saveCDGrab(requestDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data","");
    }

    @PostMapping(value = {"/incrementMoney"})
    @ApiOperation(value = "增值分享金额")
    public R getIncrement(@RequestBody CDGrabInfoSaveRequestDto requestDto) {
        try {
            BigDecimal incrementMoney = incrementService.incrementMoney(requestDto);
            return R.ok().put("data",incrementMoney);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("获取：" + e.getMessage());
        }
    }

    @PostMapping(value = {"/grabview"})
    @ApiOperation(value = "创单节点抢单页面查看接口")
    public R grabView(@RequestBody CDGrabInfoRequestDto requestDto) {
        CDGrabViewResponseDto data= null;
        try {
            data = cdGrabService.queryCDGrabView(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",data);
    }

    @PostMapping(value = {"/cancel"})
    @ApiOperation(value = "创单节点抢单页撤销接口")
    public R cancel(@RequestBody CDGrabInfoRequestDto requestDto) {
        if(requestDto == null || requestDto.getContractId() == null){
            return R.error("请求参数错误，有为空的字段");
        }

        try {
            cdGrabService.updateCancelGrab(String.valueOf(requestDto.getContractId()));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

        return R.ok().put("data","");
    }

    @PostMapping(value = {"/kickoff"})
    @ApiOperation(value = "创单节点抢单页踢出接口")
    public R kickoff(@RequestBody CDGrabInfoRequestDto requestDto) {
        if(requestDto == null || requestDto.getContractId() == null){
            return R.error("请求参数错误，有为空的字段");
        }

        try {
            cdGrabService.updateKickOff(String.valueOf(requestDto.getContractId()));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

        return R.ok().put("data","");
    }

    @PostMapping(value = {"/grabHistoryView"})
    @ApiOperation(value = "创单节点抢单页面查看接口")
    public R grabHistoryView(@RequestBody CDGrabInfoRequestDto requestDto) {
        List<CDGrabHistoryResponseDto> data= null;
        try {
            data = cdGrabService.queryCDGrabHistoryView(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",data);
    }

}
