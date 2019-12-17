package com.haier.hailian.contract.service.homepage.impl;

import com.haier.hailian.contract.dto.homepage.ChainGroupInfoDto;
import com.haier.hailian.contract.dto.homepage.ContractListsDto;
import com.haier.hailian.contract.service.homepage.HomePageService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HomePageImpl implements HomePageService {


    /**
     * 获取已抢入合约列表查询接口
     * @param contractListsDto
     * @return
     */
    @Override
    public List<Object> getContractList(ContractListsDto contractListsDto) {
        List<Object> list = new ArrayList<>();
        try{
            list = null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取链群详情
     * @param chainGroupInfoDto
     * @return
     */
    @Override
    public Map<String, Object> getChainGroupInfo(ChainGroupInfoDto chainGroupInfoDto) {
        Map<String, Object> map = new HashMap<>();
        try{
            // 链群等级
            map.put("chainGroupRank","");
            // 链群、节点、人的增值分享
            map.put("share","");
            // 吸引力预计
            map.put("estimate","");
            // 吸引力实际
            map.put("fact","");
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
