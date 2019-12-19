package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.service.SysNodeEhrService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysNodeEhr)表控制层
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
@RestController
@RequestMapping("sysNodeEhr")
public class SysNodeEhrController {
    /**
     * 服务对象
     */
    @Resource
    private SysNodeEhrService sysNodeEhrService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public SysNodeEhr selectOne(Integer id) {
        return this.sysNodeEhrService.queryById(id);
    }

}