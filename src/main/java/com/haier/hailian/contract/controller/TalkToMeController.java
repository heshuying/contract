package com.haier.hailian.contract.controller;

import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.ZReservePlanTeamworkDto;
import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.entity.ZWaringPeriodConfig;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import com.haier.hailian.contract.service.ZWaringPeriodConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 表控制层
 *
 * @author makejava
 * @since 2019-12-18 14:24:06
 */
@RestController
@RequestMapping("talk")
@Api(value = "并联协同预案", tags = {"交流群"})
@Slf4j
public class TalkToMeController {
    /**
     * 服务对象
     */
    @Resource
    private ZReservePlanTeamworkService zReservePlanTeamworkService;
    @Resource
    private ZWaringPeriodConfigService zWaringPeriodConfigService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("getTalkCount")
    @ApiOperation(value = "获取协同预案的条数")
    public R getTalkCount(@RequestBody  @Validated @ApiParam(value = "合约的ID", required = true) int id) {
        try {
            int count = zReservePlanTeamworkService.queryCountByParentId(id);
            return R.ok().put("data", count);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getTalkCount,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @PostMapping("getTalkList")
    @ApiOperation(value = "获取并联协同预案列表")
    public R getTalkList(@RequestBody  @Validated @ApiParam(value = "合约的ID", required = true) int id) {
        try {
            ZReservePlanTeamwork zReservePlanTeamwork = new ZReservePlanTeamwork();
            zReservePlanTeamwork.setParentId(id);
            List<ZReservePlanTeamworkDto> zReservePlanTeamworkList = zReservePlanTeamworkService.queryAllByKey(zReservePlanTeamwork);
            return R.ok().put("data", zReservePlanTeamworkList);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getTalkList,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

    @PostMapping(value = {"/savePlan"})
    @ApiOperation(value = "保存协同预案")
    public R savePlan(@RequestBody @Validated @ApiParam(value = "保存协同预案", required = true) ZReservePlanTeamworkDto zReservePlanTeamworkDto) {
        try {
            if (StringUtils.isBlank(zReservePlanTeamworkDto.getExecuter())){
             return R.error("执行人不能为空！");
            }
            String z = zReservePlanTeamworkService.saveAllInfo(zReservePlanTeamworkDto);
            if (z == null){
                return R.error("操作人不在链群中！");
            }
            if (!"保存成功".equals(z)){
                return R.error(z);
            }
            return R.ok().put("data", z);
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.savePlan,", e);
            return R.error("操作出问题了，请联系管理员！");
        }
    }

    @PostMapping(value = {"/getA"})
    @ApiOperation(value = "测试URL")
    //166
    public R getA() {
        try {
            zWaringPeriodConfigService.jdWarning();
            zWaringPeriodConfigService.qdWarning();
            return R.ok();
        } catch (Exception e) {
            log.error("错误发生在ZHrChainInfoController.getA,", e);
            return R.error("系统异常，请稍后尝试！");
        }
    }

}
