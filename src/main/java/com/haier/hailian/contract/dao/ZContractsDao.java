package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.entity.ZContracts;

import java.util.Date;
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
public interface ZContractsDao extends BaseMapper<ZContracts> {

    List<ZContracts> selectContractList(QueryContractListDTO queryDTO);

    /**
     * 查询体验 抢单时，列表对象，剔除已抢入的合约
     * @param queryDto
     * @return
     */
    List<ZContracts>queryTyGrabList(TyGrabListQueryDto queryDto);

    List<ZContracts>queryTyMyGrabList(TyGrabListQueryDto queryDto);
    /**
     * 查询所有的链群主抢单记录
     * @return
     */
    List<ZContracts> selectAllContracts(String status);


    List<ZContracts> selectAllContractsById(int pId);

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
    ZContracts selectByGID(String groupId,String userCode);

    /**
     * 合约详情查看（创单）
     */
    List<ContractViewDataCD> selectContractsViewForCD(Map<String,Object> paraMap);

    /**
     * 查询已抢入合约列表
     * @param queryDTO
     * @return
     */
    List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO);

    List<ContractViewDataTY> selectContractsViewForTY(String contractId);

    /**
     * 体验抢单标题列表查询
     * @param contractId
     * @return
     */
    List<TargetTitleTYDTO> selectContractsTitleForTY(String contractId);

    /**
     * 体验抢单汇总
     * @param contractId
     * @return
     */
    List<FactorConfigDTO> selectContractsViewForTYSum(String contractId);

    /**
     * 体验抢单列表数据new
     * @return
     */
    List<ContractViewDataTYResponseNewDTO> selectContractsViewForTYNew(Map<String, Object> paraMap);

    /**
     * 查询待抢入合约列表
     * @param queryDTO
     * @return
     */
    List<ZContracts> selectToGrabContract(QueryContractListDTO queryDTO);

    /**
     * 获取创单节点抢入合约
     */
    Integer getContractSize(String contractId);

    /**
     * 获取市场抢入合约
     */
    Integer getContractSize2(String contractId);

    /**
     * 根据合约ID查询合约详情
     * @param contractId
     * @return
     */
    ZContracts selectByContractId(Integer contractId);

    /**
     * 查询未抢入、可优化的合约ID
     * @return
     */
    String selectContractToUpdate();

    /**
     * 查询被踢出的、抢入已截止、在有效期内的合约
     * @param dto
     * @return
     */
    List<ZContracts> selectKickedOutContract(QueryContractListDTO dto);

    int selectContractsViewForTYCount(String contractId);


    ZContracts selectByTime(String startTime ,String endTime,String groupId);

    List<ZContracts> selectHomePageContract(QueryContractListDTO2 queryDTO);


    /**
     * 获取合约的ID
     * @param opTime
     * @return
     */
    ZContracts selectByChainCode(String chainCode,String opTime);
    Date selectGamnlingBeginDate(String chainCode);

    List<CDGrabDataDTO> getCDGrabResultType3List(Map<String,Object> paraMap);

    List<String> getContractsForCountUpdate(Map<String,Object> paraMap);

    List<ZContracts> selectContractListForTarget(QueryContractListDTO queryDTO);

    List<ZContracts> selectAllGrabContract(QueryContractListDTO queryDTO);

    //查询复核截止时间将要截止，需要邮件提醒复核的合约
    List<ZContracts> selectContractsForCheckWarning();
}
