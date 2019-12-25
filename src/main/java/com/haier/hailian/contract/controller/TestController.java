package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.entity.SysEmpChain;
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.SysEmpChainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 19012964 on 2019/12/23.
 */
@Api(value = "测试或者跑批", tags = {"测试或者跑批"})
@RestController(value = "/test")
public class TestController {
    @Autowired
    SysEmpChainService empChainService;
    @Autowired
    ChainCommonService chainCommonService;

    @GetMapping(value = {"/userChains"})
    @ApiOperation(value = "跑批产生用户chainCode")
    public R createChain() {
        empChainService.batchCreateChainForEmp();
        return R.ok();
    }

    @GetMapping(value = {"/buildContractChain/{contractId}"})
    @ApiOperation(value = "跑批产生用户chainCode")
    public R createChain(@PathVariable Integer contractId) {
        chainCommonService.buildContractChain(contractId);
        return R.ok();
    }
}
