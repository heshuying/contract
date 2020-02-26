package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.LittleXWEmpDTO;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.service.EmployeeCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 19033323
 */
@Slf4j
@Api(value = "人才池相关接口", tags = {"人才池相关接口"})
@RestController
@RequestMapping(value = {"/card"})
public class EmployeeCardController {

    @Autowired
    EmployeeCardService employeeCardService;

    @PostMapping(value = {"/emplist"})
    @ApiOperation(value = "最小单元下人员列表")
    public R emplist(@RequestBody Map<String,Object> paraMap) {
        List<LittleXWEmpDTO> data = employeeCardService.getLittleXWEmp(paraMap);
        return R.ok().put("count", data.size()).put("data",data);
    }
}
