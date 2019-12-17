package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ValidateChainNameDTO;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * (ZHrChainInfo)表控制层
 *
 * @author makejava
 * @since 2019-12-17 14:52:20
 */
@RestController
@RequestMapping("zHrChainInfo")
public class ZHrChainInfoController {
    /**
     * 服务对象
     */
    @Resource
    private ZHrChainInfoService zHrChainInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public ZHrChainInfo selectOne(Integer id) {
        return this.zHrChainInfoService.queryById(id);
    }


    @PostMapping(value = {"/validateChainName"})
    @ApiOperation(value = "校验链群名称")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token",dataType = "String")
    public R validateChainName(@RequestBody @Validated @ApiParam(value = "验证", required = true) ValidateChainNameDTO validateChainNameDTO) throws IOException {
        return  zHrChainInfoService.validateChainName(validateChainNameDTO);
    }
}