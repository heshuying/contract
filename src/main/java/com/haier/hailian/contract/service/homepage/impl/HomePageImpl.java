package com.haier.hailian.contract.service.homepage.impl;

import com.haier.hailian.contract.dao.SysEmployeeEhrDao;
import com.haier.hailian.contract.dao.XXwActualDao;
import com.haier.hailian.contract.dao.ZGrabContractsDao;
import com.haier.hailian.contract.dto.homepage.ChainGroupInfoDto;
import com.haier.hailian.contract.dto.homepage.ContractListRes;
import com.haier.hailian.contract.dto.homepage.ContractListsDto;
import com.haier.hailian.contract.service.homepage.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HomePageImpl implements HomePageService {


    @Autowired
    private ZGrabContractsDao zGrabContractsDao;
    @Autowired
    private SysEmployeeEhrDao sysEmployeeEhrDao;
    @Autowired
    private XXwActualDao xXwActualDao;

    /**
     * 获取已抢入合约列表查询接口
     * @param contractListsDto
     * @return
     */
    @Override
    public List<ContractListRes> getContractList(ContractListsDto contractListsDto) {
        List<ContractListRes> list = new ArrayList<>();
        try{

            // 获取当前登录人小微
            //String xWcode = sysEmployeeEhrDao.selectXwCode(contractListsDto.getEmpSN());


            // 1. z_grab_contracts 获取抢单信息  parent_id 合约id
            // 2. 通过合约id 去 z_gambling_contracts 获取链群编码  chain_code
            // 3. 通过chain_code 去 z_hr_chain_info获取链群信息
            // 4. 通过前端月份时间 + 通过chain_code（链群编码） 去 z_target_basic获取对应月份预计
            // 5. 实际 - 现在卡住了（大数据现在需要给我们数据）

            //contractListsDto.setEmpSN(xWcode);

            list = zGrabContractsDao.getContractList(contractListsDto);
            list.add(new ContractListRes());
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
            // 链群等级  现在规则没出来  卡住了
            map.put("chainGroupRank","暂时不知道咋算~");
            // 链群的增值分享   实际 - 没有  卡住了  实际利润 - 举单利润（链群）
            // 链群实际利润
            BigDecimal chainFact = xXwActualDao.getChainFact(chainGroupInfoDto);
            // 链群目标利润 TODO
            BigDecimal chainEstimate;

            map.put("chainShare","");
            // 节点、人的增值分享  实际 - 没有  卡住了  实际利润 - 举单利润（个人）
            map.put("nodeShare","");
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
