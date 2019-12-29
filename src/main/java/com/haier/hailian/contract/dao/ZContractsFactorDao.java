package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.ChainGroupTargetDTO;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ZContractsFactorDao extends BaseMapper<ZContractsFactor> {
    @Select("SELECT * FROM z_contracts_factor cf WHERE cf.`contract_id` = #{contractId} AND (cf.`region_code` IS NULL OR cf.`region_code` = '') AND cf.`factor_type` = #{factorType};")
    List<ZContractsFactor> selectFactorForView(String contractId, String factorType);

    @Select("SELECT * FROM z_contracts_factor cf WHERE cf.`contract_id` = #{contractId} AND cf.`region_code` IS NOT NULL AND cf.`factor_type` = #{factorType};")
    List<ZContractsFactor> selectFactorForViewTY(String contractId, String factorType);
}
