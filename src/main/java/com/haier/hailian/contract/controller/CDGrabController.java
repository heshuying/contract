package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.service.IncrementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

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
            data = cdGrabService.queryCDGrabInfo(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("创单节点抢单页面查询发生异常：" + e.getMessage());
        }
        return R.ok().put("data",data);
    }

    @PostMapping(value = {"/save"})
    @ApiOperation(value = "创单节点抢单页面数据保存接口")
    public R saveGrab(@RequestBody CDGrabInfoSaveRequestDto requestDto) {
        try {
            cdGrabService.saveCDGrab(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("创单节点抢单保存发生异常：" + e.getMessage());
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

}
