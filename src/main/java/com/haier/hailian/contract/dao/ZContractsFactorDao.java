package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.TargetTrend;
import com.haier.hailian.contract.entity.ZContractsFactor;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
public interface ZContractsFactorDao extends BaseMapper<ZContractsFactor> {
    @Select("SELECT * FROM z_contracts_factor cf WHERE cf.`contract_id` = #{contractId} AND (cf.`region_code` IS NULL OR cf.`region_code` = '') AND cf.`factor_type` = #{factorType};")
    List<ZContractsFactor> selectFactorForView(String contractId, String factorType);

    @Select("SELECT * FROM z_contracts_factor cf WHERE cf.`contract_id` = #{contractId} AND cf.`region_code` IS NOT NULL AND cf.`factor_type` = #{factorType};")
    List<ZContractsFactor> selectFactorForViewTY(String contractId, String factorType);

    List<ZContractsFactor> getFactorByContractId(Integer contractId);
    /**
     * 根据举单ID查询链群目标
     * @param contractId
     * @return
     */
    List<ChainGroupTargetDTO> selectChainFactorByContractId(Integer contractId);

    List<FactorGrabResDTO> getFactorGrabList(Map<String, Object> map);

    int selectTyQualified(Map<String, Object> map);

    /**
     * 根据链群编码和月份查询链群的举单目标
     * @param dto
     * @return
     */
    List<ZContractsFactor> selectChainGamblingTarget(EventMiddleDTO dto);

    List<Integer> selectCdReach(Map<String, Object> map);

    List<EventMiddleTYDTO> selectTyTarget(int contractId);

    List<EventMiddleCdDTO> selectCdTarget(int contractId);

    List<ZContractsFactor> selectGrabInfo(Map<String, Object> map);

    List<ZContractsFactor> selectTYGrabInfo(Map<String, Object> map);

    TargetTrend selectChainTargetTrend(int contractId);
}
