package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.entity.ZContracts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
public interface ZContractsDao extends BaseMapper<ZContracts> {

    List<ZContracts> selectContractList(QueryContractListDTO queryDTO);

    /**
     * 查询所有的链群主抢单记录
     * @return
     */
    List<ZContracts> selectAllContracts();

    /**
     * 查询所有的抢单人的
     * @param id
     * @return
     */
    List<ZContracts> selectUserList(int id);

    /**
     * 获取合约的ID
     * @param groupId
     * @return
     */
    ZContracts selectByGID(String groupId);
}
