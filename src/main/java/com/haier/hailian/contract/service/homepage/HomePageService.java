package com.haier.hailian.contract.service.homepage;

import com.haier.hailian.contract.dto.ContractsShareSpaceInfo;
import com.haier.hailian.contract.dto.homepage.*;

import java.util.List;
import java.util.Map;


public interface HomePageService {

    /**
     * 获取已抢入合约列表查询接口
     * @param contractListsDto
     * @return
     */
    List<ContractListRes> getContractList(ContractListsDto contractListsDto);

    /**
     * 获取链群详情
     * @param chainGroupInfoDto
     * @return
     */
    Map<String,Object> getChainGroupInfo(ChainGroupInfoDto chainGroupInfoDto);

    /**
     * 根据链群编码获取数据
     * @param dataInfo
     * @return
     */
    Map<String,Object> getContractData(DataInfo dataInfo);

    /**
     * 获取链群组织数据
     * @return
     */
    List<ChainDataInfo> getChainData();

    /**
     * @param contractsShareSpaceInfos
     * @return
     */
    int updateContractsShareSpace(List<ContractsShareSpaceInfo> contractsShareSpaceInfos);
}
