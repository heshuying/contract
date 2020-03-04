package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.service.VJdxpService;
import com.haier.hailian.contract.util.DateFormatUtil;
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
        BigDecimal score = BigDecimal.ZERO;
        if(requestMap == null){
            requestMap = new HashMap<>();
        }
        List<StarDTO> data= new ArrayList<>();
        try {
//            requestMap.put("startDate", DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YM) + "01");
//            requestMap.put("endDate", DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YM) + "31");
            data = vJdxpService.getStarList(requestMap);
            if(data != null && !data.isEmpty()){
                for(StarDTO star : data){
                    score = score.add(new BigDecimal(StringUtils.isBlank(star.getPjM())?"0":star.getPjM()));
                }
                score = score.divide(BigDecimal.valueOf(data.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("data",score);
    }
}

