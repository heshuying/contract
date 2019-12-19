package com.haier.hailian.contract.target.controller;


import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.target.dto.QueryBottomDTO;
import com.haier.hailian.contract.target.service.TargetBasicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
@RestController
@RequestMapping("/targetBasic")
@Api(value = "目标底线接口", tags = "目标底线接口")
public class TargetBasicController {

    @Autowired
    private TargetBasicService targetBasicService;

    @PostMapping(value = {"/selectBottom"})
    @ApiOperation(value = "链群主举单查询目标底线")
    public R selectBottom(@RequestBody QueryBottomDTO dto) {
        List<TargetBasic> list = targetBasicService.selectBottom(dto);
        return R.ok().put("data",list);
    }

}

