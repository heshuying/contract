package com.haier.hailian.contract.controller.homepage;


import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;
import com.haier.hailian.contract.service.homepage.ExpectAndActualDiffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyq 2020年4月10日 15:18:43
 */
@Api(value = "预实差相关接口", tags = {"预实差相关接口"})
@RestController
@Slf4j
public class ExpectAndActualDiffController {

    @Autowired
    private ExpectAndActualDiffService expectAndActualDiffService;



    @PostMapping(value = {"/getChainGrabNum"})
    @ApiOperation(value = "链群应实抢入数量及达标对比")
    public R getChainGrabNum(@RequestBody ExpectAndActualDiffDto expectAndActualDiffDto) {
        try{
            Map<String , Object> res = expectAndActualDiffService.getChainGrabInfo(expectAndActualDiffDto);
            return R.ok().put("data",res);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("获取：" + e.getMessage());
        }
    }



    @PostMapping(value = {"/getGrabInfo"})
    @ApiOperation(value = "获取节点抢单详情")
    public R getGrabInfo(@RequestBody ExpectAndActualDiffDto expectAndActualDiffDto) {
        try{
            Map<String , Object> res = expectAndActualDiffService.getGrabInfo(expectAndActualDiffDto);
            return R.ok().put("data",res);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("获取：" + e.getMessage());
        }
    }



    @PostMapping(value = {"/getTYGrabInfo"})
    @ApiOperation(value = "获取体验节点抢单详情")
    public R getTYGrabInfo(@RequestBody ExpectAndActualDiffDto expectAndActualDiffDto) {
        try{
            Map<String , Object> res = expectAndActualDiffService.getTYGrabInfo(expectAndActualDiffDto);
            return R.ok().put("data",res);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("获取：" + e.getMessage());
        }
    }



    @PostMapping(value = {"/grabStarMap"})
    @ApiOperation(value = "抢入星图")
    public R grabStarMap(@RequestBody ExpectAndActualDiffDto expectAndActualDiffDto) {
        try{
            List<Map<String, Object>> res = expectAndActualDiffService.grabStarMap(expectAndActualDiffDto);
            return R.ok().put("data",res);
        }catch (Exception e){
            e.printStackTrace();
            return R.error("获取：" + e.getMessage());
        }
    }


}
