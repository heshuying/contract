package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.ContractViewDataCD;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
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
     * 查询体验 抢单时，列表对象，剔除已抢入的合约
     * @param queryDto
     * @return
     */
    List<ZContracts>queryTyGrabList(TyGrabListQueryDto queryDto);

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

    /**
     * 合约详情查看（创单）
     * @param contractId
     * @return
     */
    List<ContractViewDataCD> selectContractsViewForCD(String contractId);

    /**
     * 查询已抢入合约列表
     * @param queryDTO
     * @return
     */
    List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO);

    /**
     * 查询待抢入合约列表
     * @param queryDTO
     * @return
     */
    List<ZContracts> selectToGrabContract(QueryContractListDTO queryDTO);
}
