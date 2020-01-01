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
    @Select("SELECT p.`parent_id` AS contractId, p.order_type AS orderType, p.`title`,p.`start_time` AS startTime,p.`end_time` AS endTime,p.`remind_type` AS remindType,p.`remind_time` AS remindTime,p.`is_important` AS isImportant,p.`executer` AS executer,p.`teamworker` AS teamworker,p.`create_user_code` AS createUserCode,p.`create_user_name` AS createUserName,p.`create_user_time` AS createUserTime,p.`task_code` AS taskCode,pd.`content`  FROM z_reserve_plan p INNER JOIN z_reserve_plan_detail pd ON pd.parent_id = p.`id` WHERE p.`parent_id` = #{contractId}")
    List<PlanInfoDto> selectPlanInfo(String contractId);

    @Select("SELECT p.`parent_id` AS contractId, p.order_type AS orderType, p.`title`,p.`start_time` AS startTime,p.`end_time` AS endTime,p.`remind_type` AS remindType,p.`remind_time` AS remindTime,p.`is_important` AS isImportant,p.`executer` AS executer,p.`teamworker` AS teamworker,p.`create_user_code` AS createUserCode,p.`create_user_name` AS createUserName,p.`create_user_time` AS createUserTime,p.`task_code` AS taskCode,pd.`content`  FROM z_reserve_plan p INNER JOIN z_reserve_plan_detail pd ON pd.parent_id = p.`id` WHERE p.`parent_id` = #{contractId} group by order_type")
    List<PlanInfoDto> selectPlanInfoGroup(String contractId);

    @Select("SELECT p.`parent_id` AS contractId, p.order_type AS orderType, p.`title`,p.`start_time` AS startTime,p.`end_time` AS endTime,p.`remind_type` AS remindType,p.`remind_time` AS remindTime,p.`is_important` AS isImportant,p.`executer` AS executer,p.`teamworker` AS teamworker,p.`create_user_code` AS createUserCode,p.`create_user_name` AS createUserName,p.`create_user_time` AS createUserTime,p.`task_code` AS taskCode,pd.`content`  FROM z_reserve_plan p INNER JOIN z_reserve_plan_detail pd ON pd.parent_id = p.`id` WHERE p.`parent_id` = #{contractId} AND order_type = #{orderType}")
    List<PlanInfoDto> selectPlanInfoSub(String contractId, String orderType);
}
