package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.homepage.ContractListRes;
import com.haier.hailian.contract.dto.homepage.ContractListsDto;
import com.haier.hailian.contract.entity.ZGrabContracts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19033715
 * @since 2019-12-18
 */
public interface ZGrabContractsDao extends BaseMapper<ZGrabContracts> {

    /**
     * 获取已抢入合约列表
     * @return
     */
    List<ContractListRes> getContractList(ContractListsDto contractListsDto);

}
