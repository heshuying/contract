package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.service.CDGrabService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = {"/info"})
    @ApiOperation(value = "创单节点抢单页面信息接口")
    public R queryInfo(@RequestBody CDGrabInfoRequestDto requestDto) {
        CDGrabInfoResponseDto data= cdGrabService.queryCDGrabInfo(requestDto);
        return R.ok().put("data",data);
    }
}
