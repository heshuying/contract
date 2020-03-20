package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.ContractXwType3DTO;
import com.haier.hailian.contract.entity.ZContractsXwType3;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 01431594
 * @since 2020-03-19
 */
public interface ZContractsXwType3Dao extends BaseMapper<ZContractsXwType3> {

    List<ContractXwType3DTO> selectXwType3ByContractId(@Param(value = "chainCode") String chainCode, @Param(value = "contractId") Integer contractId);
}
