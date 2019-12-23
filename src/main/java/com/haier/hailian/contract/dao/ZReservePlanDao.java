package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.dto.grab.PlanInfoDto;
import com.haier.hailian.contract.entity.ZReservePlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
public interface ZReservePlanDao extends BaseMapper<ZReservePlan> {
    @Select("SELECT p.`parent_id` as contractId, p.`title`, pd.`content` FROM z_reserve_plan p INNER JOIN z_reserve_plan_detail pd ON pd.parent_id = p.`id` WHERE p.`parent_id` = #{contractId}")
    List<PlanInfoDto> selectPlanInfo(String contractId);
}
