package com.haier.hailian.contract.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.hailian.contract.dto.LittleXWEmpDTO;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.service.EmployeeCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Value("${card.url}")
    private String cardUrl;

    @Autowired
    EmployeeCardService employeeCardService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = {"/emplist"})
    @ApiOperation(value = "最小单元下人员列表")
    public R emplist(@RequestBody Map<String,Object> paraMap) {
        List<LittleXWEmpDTO> data = employeeCardService.getLittleXWEmp(paraMap);
        return R.ok().put("count", data.size()).put("data",data);
    }

    @PostMapping("/empinfo")
    @ApiOperation(value = "人员详情接口")
    public R empinfo(@RequestBody Map<String,String> paraMap) {
        String empSn = paraMap.get("empSn");
        if(StringUtils.isBlank(empSn)){
            return R.error("请求参数不全");
        }
        cardUrl = cardUrl + empSn;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(cardUrl, String.class);
        JSONObject data = JSON.parseObject(responseEntity.getBody(), JSONObject.class);
        return R.ok().put("data", data);
    }
}
