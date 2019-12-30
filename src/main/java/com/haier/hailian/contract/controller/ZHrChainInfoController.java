package com.haier.hailian.contract.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ValidateChainNameDTO;
import com.haier.hailian.contract.dto.ZHrChainInfoDto;
import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ZHrChainInfo)表控制层
 *
 * @author makejava
 * @since 2019-12-17 14:52:20
 */
@RestController
@RequestMapping("zHrChainInfo")
@Slf4j
@Api(value = "链群注册", tags = {"构建链群"})
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
    @ApiOperation(value = "获取链群详细信息")
    public ZHrChainInfo selectOne(Integer id) {
        return this.zHrChainInfoService.queryById(id);
    }


    /**
     * 获取所有链群名称列表(后期修改上链)
     *
//     * @param zHrChainInfoDto
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "获取所有链群名称列表")
    public R getAll() {
        try {
            ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
//            System.out.println(zHrChainInfoDto.getChainName());
            List<ZHrChainInfo> list = this.zHrChainInfoService.queryAll(zHrChainInfo);
            return R.ok().put("data", list);
        } catch (Exception e) {
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/validateChainName"})
    @ApiOperation(value = "校验链群名称")
    public R validateChainName(@RequestBody @Validated @ApiParam(value = "验证", required = true) ValidateChainNameDTO validateChainNameDTO) {
        try {
            return zHrChainInfoService.validateChainName(validateChainNameDTO);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.validateChainName,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/searchUsers"})
    @ApiOperation(value = "查询链群架构人员")
    public R searchUsers(@RequestBody @Validated @ApiParam(value = "查询人员", required = true) String keyWords) {
        try {
            JsonParser parse = new JsonParser();  //创建json解析器
            JsonObject json = (JsonObject) parse.parse(keyWords);  //创建jsonObject对象
            String result = json.get("keyWords").getAsString();
            List<SysNodeEhr> list = zHrChainInfoService.searchUsersByKeyWords(result);
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.searchUsers,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getNodeTarget"})
    @ApiOperation(value = "查询人员目标")
    public R getNodeTarget(@RequestBody @Validated @ApiParam(value = "目标查询,以逗号分割", required = true) String nodeCodeStr) {
        try {
            JsonParser parse = new JsonParser();  //创建json解析器
            JsonObject json = (JsonObject) parse.parse(nodeCodeStr);  //创建jsonObject对象
            String result = json.get("nodeCodeStr").getAsString();
            List<TargetBasic> list = zHrChainInfoService.getNodeTargetList(result);
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getNodeTarget,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/saveChainInfo"})
    @ApiOperation(value = "保存链群信息")
    public R saveChainInfo(@RequestBody @Validated @ApiParam(value = "保存链群和目标", required = true) ZHrChainInfoDto zHrChainInfoDto) {
        try {
            //1.校验一下名字是否重复
            R res = zHrChainInfoService.validateChainName(new ValidateChainNameDTO(zHrChainInfoDto.getChainName()));
            if (res.get("code").equals(0)){
                ZHrChainInfoDto z = zHrChainInfoService.saveChainInfo(zHrChainInfoDto);
                if (z==null){
                    return R.error("保存出错了，请稍后重试！");
                }
                return R.ok().put("data", z);
            }
            return res;
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getNodeTarget,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getMinbuList"})
    @ApiOperation(value = "查询最小单元")
    public R getMinbuList() {
        try {
            List list = zHrChainInfoService.getMinbuList();
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getNodeTarget,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }
}