package com.haier.hailian.contract.service.homepage;

import com.haier.hailian.contract.dto.homepage.ChainGroupInfoDto;
import com.haier.hailian.contract.dto.homepage.ContractListsDto;
import java.util.List;
import java.util.Map;


public interface HomePageService {

    /**
     * 获取已抢入合约列表查询接口
     * @param contractListsDto
     * @return
     */
    List<Object> getContractList(ContractListsDto contractListsDto);

    /**
     * 获取链群详情
     * @param chainGroupInfoDto
     * @return
     */
    Map<String,Object> getChainGroupInfo(ChainGroupInfoDto chainGroupInfoDto);
}
