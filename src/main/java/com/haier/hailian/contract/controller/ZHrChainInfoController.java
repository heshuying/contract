package com.haier.hailian.contract.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    @ApiOperation(value = "获取链群最小单元详细信息")
    public R selectOne(Integer id) {
        try {
            return R.ok().put("data", this.zHrChainInfoService.queryByNodeId(id));
        } catch (Exception e) {
            return R.error("系统异常，请稍后尝试！");
        }
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("getInfo")
    @ApiOperation(value = "获取链群信息")
    public R getInfo(Integer id) {
        try {
            return R.ok().put("data", this.zHrChainInfoService.queryAllById(id));
        } catch (Exception e) {
            return R.error("系统异常，请稍后尝试！");
        }
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
            /**
             * by liuyq  2020年1月3日 10:11:28  增加当前登录人校验 
             */
            Subject subject = SecurityUtils.getSubject();
            //获取当前用户
            SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
            // 必须是当前登录人链群
            zHrChainInfo.setDeleted(0);
            zHrChainInfo.setMasterCode(sysUser.getEmpSn());
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


//            if (StringUtils.isBlank(zHrChainInfoDto.getFixedPosition())){
//                return R.error("链群定位未输入，请输入！");
//            }
            if (res.get("code").equals(0)){

                if(StringUtils.isEmpty(zHrChainInfoDto.getChainCode())){
                    return R.error("请选择需要注册的链群!");
                }

                // 校验比例
                if(!"1".equals(zHrChainInfoDto.getIsModel())){
                    BigDecimal count = new BigDecimal(0);
                    for (XwType3Info xwType3Info:zHrChainInfoDto.getSaveXwType3().getXwType3List()){
                        count = BigDecimal.valueOf(Double.parseDouble(xwType3Info.getSharePercent())).add(count);
                    }
                    if (count.intValue()>100){
                        return R.error("分享比例不能大于100%");
                    }
                    if (count.intValue() == 0){
                        return R.error("分享比例不能为0！");
                    }
                }

                ZHrChainInfoDto z = zHrChainInfoService.saveChainInfo(zHrChainInfoDto);
                if ("E".equals(z.getCode())){
                    return R.error("MDM系统返回信息:" + z.getMsg());
                }
                if (z==null){
                    return R.error("保存出错了，请稍后重试！");
                }
                return R.ok().put("data", z);
            }
            return res;
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.saveChainInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getMinbuList"})
    @ApiOperation(value = "查询最小单元")
    public R getMinbuList(@RequestBody String ptCode) {
        try {
            List list = zHrChainInfoService.getMinbuList(ptCode);
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getMinbuList,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }
    @PostMapping(value = {"/updateChainInfo"})
    @ApiOperation(value = "更新链群信息")
    public R updateChainInfo(@RequestBody @Validated @ApiParam(value = "更新链群和目标", required = true) ZNodeTargetPercentInfo zNodeTargetPercentInfo) {
        try {
            int z = zHrChainInfoService.updateChainInfo(zNodeTargetPercentInfo);
            if (z==0){
                return R.error("更新出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateChainInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }
    @PostMapping(value = {"/updateBatchChainInfo"})
    @ApiOperation(value = "批量更新节点分享比例")
    public R updateBatchChainInfo(@RequestBody @Validated @ApiParam(value = "更新链群和目标", required = true) List<ZNodeTargetPercentInfo> zNodeTargetPercentInfo) {
        try {
                int z = zHrChainInfoService.updateBatch(zNodeTargetPercentInfo);
                if (z==0){
                    return R.error("更新出错了，请稍后重试！");
                }
                return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateBatchChainInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @PostMapping(value = {"/delChainInfo"})
    @ApiOperation(value = "删除链群信息")
    public R delChainInfo(@RequestBody @Validated @ApiParam(value = "删除链群和目标", required = true) ZNodeTargetPercentInfo zNodeTargetPercentInfo) {
        try {
            int z = zHrChainInfoService.deleteChainInfo(zNodeTargetPercentInfo.getId());
            if (z==0){
                return R.error("删除出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.delChainInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @GetMapping("getDepVCode")
    @ApiOperation(value = "获取用户的部门编码（现在已改成获取最小作战单元）")
    public String getDepVCode(String userCode) {
        try {
            return this.zHrChainInfoService.getDepVCode(userCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = {"/updateChain"})
    @ApiOperation(value = "更新链群信息主要信息")
    public R updateChain(@RequestBody @Validated @ApiParam(value = "更新链群和目标", required = true) ZHrChainInfoDto zHrChainInfoDto) {
        try {
            ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
            zHrChainInfo.setFixedPosition(zHrChainInfoDto.getFixedPosition());
            zHrChainInfo.setId(zHrChainInfoDto.getId());
            zHrChainInfo.setZzfxRate(zHrChainInfoDto.getZzfxRate());
            zHrChainInfo.setTyShareRate(zHrChainInfoDto.getTyShareRate());
            zHrChainInfo.setCdShareRate(zHrChainInfoDto.getCdShareRate());
            zHrChainInfo.setGrabFlag(zHrChainInfoDto.getGrabFlag());
            ZHrChainInfo z = zHrChainInfoService.update(zHrChainInfo);
            if (z==null){
                return R.error("更新出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateChainInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @PostMapping(value = {"/searchChainList"})
    @ApiOperation(value = "查询用户举单和抢单的链群")
    public R searchChainList() {
        try {
            Subject subject = SecurityUtils.getSubject();
            SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
            List<ZHrChainInfo> list = zHrChainInfoService.searchChainListByUser(sysUser.getEmpSn());
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.searchChainList,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getOtherMinbuList"})
    @ApiOperation(value = "查询未选中最小单元")
    public R getOtherMinbuList(@RequestBody String chainCode) {
        try {
            List list = zHrChainInfoService.getOtherMinbuList(chainCode);
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getOtherMinbuList,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/saveNewMinbu"})
    @ApiOperation(value = "保存新增最小作战单元")
    public R saveNewMinbu(@RequestBody List<ZNodeTargetPercentInfo> zNodeTargetPercentInfos) {
        try {
            int z = zHrChainInfoService.saveNewMinbu(zNodeTargetPercentInfos);
            if (z==0){
                return R.error("保存出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.saveNewMinbu,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @PostMapping(value = {"/saveModel"})
    @ApiOperation(value = "链群设置新增模块")
    public R saveModel(@RequestBody ZHrChainInfoDto zHrChainInfoDto) {
        try {
            int z = zHrChainInfoService.saveModel(zHrChainInfoDto);
            if (z==0){
                return R.error("保存出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.saveModel,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/updateModelInfo"})
    @ApiOperation(value = "更新子链群(删除链群deleted字段前端传1)")
    public R updateModelInfo(@RequestBody @Validated @ApiParam(value = "更新子链群", required = true) ZHrChainInfo zHrChainInfo) {
        try {
            int z = zHrChainInfoService.updateModelInfo(zHrChainInfo);
            if (z==0){
                return R.error("修改出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateModelInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getChildChainOtherMinbuList"})
    @ApiOperation(value = "查询子链群未选中最小单元")
    public R getChildChainOtherMinbuList(@RequestBody ChainRepairInfo chainRepairInfo) {
        try {
            List list = zHrChainInfoService.getChildChainOtherMinbuList(chainRepairInfo);
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getChildChainOtherMinbuList,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @GetMapping(value = {"/updateAllGroupId"})
    @ApiOperation(value = "更新所有链群groupId")
    public R updateAllGroupId() {
        try {
            int num = zHrChainInfoService.updateAllGroupId();
            return R.ok().put("data",num);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateAllGroupId,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }



    @GetMapping(value = {"/updateChainTYInfo"})
    @ApiOperation(value = "插入某些链群体验节点数据")
    public R updateChainTYInfo() {
        try {
            int num = zHrChainInfoService.updateChainTYInfo();
            return R.ok().put("data",num);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateChainTYInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @GetMapping(value = {"/updateTargetNodesXwType3Code"})
    @ApiOperation(value = "更新node表XwType3字段")
    public R updateTargetNodesXwType3Code() {
        try {
            int num = zHrChainInfoService.updateTargetNodesXwType3Code();
            return R.ok().put("data",num);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateTargetNodesXwType3Code,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getOdsXwType3List"})
    @ApiOperation(value = "查询最小作战单元类型  xwType3")
    public R getOdsXwType3List() {
        try {
            List list = zHrChainInfoService.getOdsXwType3List();
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getOdsXwType3List,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/getOtherOdsXwType3List"})
    @ApiOperation(value = "查询未选中最小作战单元类型  xwType3(主链群子链群通用)")
    public R getOtherOdsXwType3List(@RequestBody String chainCode) {
        try {
            List list = zHrChainInfoService.getOtherOdsXwType3List(chainCode);
            if(list ==null){
                R.error("登陆异常请重新尝试！");
            }
            return R.ok().put("data", list);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getOtherOdsXwType3List,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/saveNewXwType3"})
    @ApiOperation(value = "保存新增XwType3")
    public R saveNewXwType3(@RequestBody SaveXwType3 saveXwType3) {
        try {
            List<String> nodes = zHrChainInfoService.saveXwType3(saveXwType3);
            if (nodes.size()==0){
                return R.error("所选资源类型未匹配到最小作战单元，请到hr系统维护最小作战单元！");
            }
            return R.ok().put("data", nodes);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.saveNewXwType3,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/delXwType3Nodes"})
    @ApiOperation(value = "删除某链群下某些小微资源类型节点")
    public R delXwType3Nodes(@RequestBody @Validated @ApiParam(value = "删除某链群下某些小微资源类型节点", required = true) ZNodeTargetPercentInfo zNodeTargetPercentInfo) {
        try {
            int z = zHrChainInfoService.delXwType3Nodes(zNodeTargetPercentInfo);
            if (z==0){
                return R.error("删除出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.delXwType3Nodes,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/updateBatchXwType3Nodes"})
    @ApiOperation(value = "批量更新小微类型下节点分享比例")
    public R updateBatchXwType3Nodes(@RequestBody @Validated @ApiParam(value = "更新链群和目标", required = true) SaveXwType3 saveXwType3) {
        try {
            int z = zHrChainInfoService.updateBatchXwType3Nodes(saveXwType3);
            if (z==0){
                return R.error("更新出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.updateBatchXwType3Nodes,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }


    @PostMapping(value = {"/syncMinbuListByXwType3"})
    @ApiOperation(value = "根据XwType3同步ods_minbu最新最小作战单元")
    public R syncMinbuListByXwType3(@RequestBody @Validated @ApiParam(value = "同步最新最小作战单元", required = true) List<SaveXwType3> saveXwType3List) {
        try {
            int z = 0;
            for(SaveXwType3 saveXwType3 : saveXwType3List){
                 z = zHrChainInfoService.syncMinbuListByXwType3(saveXwType3) + z;
            }
            if (z==0){
                return R.error("同步出错了，请稍后重试！");
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.syncMinbuListByXwType3,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }



    @GetMapping(value = {"/getChainExtInfo"})
    @ApiOperation(value = "获取链群额外信息")
    public R getChainExtInfo() {
        try {
            Map<String , Object> map = zHrChainInfoService.getChainExtInfo();
            return R.ok().put("data",map);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getChainExtInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }



    @GetMapping(value = {"/getChainNameList"})
    @ApiOperation(value = "获取MDM链群列表信息")
    public R getChainNameList() {
        try {
            List<Map> map = zHrChainInfoService.getChainNameList();
            return R.ok().put("data",map);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getChainExtInfo,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }





}