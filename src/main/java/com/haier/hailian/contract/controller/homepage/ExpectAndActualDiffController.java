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


}
