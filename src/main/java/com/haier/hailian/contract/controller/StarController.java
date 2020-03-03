package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.service.VJdxpService;
import com.haier.hailian.contract.util.DateFormatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author 19033323
 */
@Slf4j
@Api(value = "星级评价", tags = {"星级评价"})
@RestController
@RequestMapping(value = {"/star"})
public class StarController {
    @Autowired
    VJdxpService vJdxpService;

    @PostMapping(value = {"/list"})
    @ApiOperation(value = "评分列表")
    public R list(@RequestBody Map<String,Object> requestMap) {
        List<StarDTO> data= new ArrayList<>();
        try {
            data = vJdxpService.getStarList(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",data).put("count", data.size());
    }

    @PostMapping(value = {"/getScore"})
    @ApiOperation(value = "获取当月评分")
    public R getScore(@RequestBody Map<String,Object> requestMap) {
        String score = "";
        if(requestMap == null){
            requestMap = new HashMap<>();
        }
        List<StarDTO> data= new ArrayList<>();
        try {
            requestMap.put("startDate", DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YM) + "01");
            requestMap.put("endDate", DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YM) + "31");
            data = vJdxpService.getStarList(requestMap);
            if(data != null && !data.isEmpty()){
                score = data.get(0).getPjM();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",score);
    }
}

