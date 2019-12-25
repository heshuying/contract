package com.haier.hailian.contract.service.homepage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.grab.PlanInfoDto;
import com.haier.hailian.contract.dto.homepage.*;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.entity.ZReservePlan;
import com.haier.hailian.contract.service.homepage.HomePageService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class HomePageImpl implements HomePageService {


    @Autowired
    private ZGrabContractsDao zGrabContractsDao;
    @Autowired
    private XXwActualDao xXwActualDao;
    @Autowired
    private ZContractsDao zContractsDao;
    @Autowired
    private ZContractsFactorDao zContractsFactorDao;
    @Autowired
    private ZHrChainInfoDao zHrChainInfoDao;
    @Autowired
    private ZReservePlanDao zReservePlanDao;

    /**
     * 获取已抢入合约列表查询接口
     * @param contractListsDto
     * @return
     */
    @Override
    public List<ContractListRes> getContractList(ContractListsDto contractListsDto) {

        // 获取当前登录人小微
        //String xWcode = sysEmployeeEhrDao.selectXwCode(contractListsDto.getEmpSN());


        // 1. z_grab_contracts 获取抢单信息  parent_id 合约id
        // 2. 通过合约id 去 z_gambling_contracts 获取链群编码  chain_code
        // 3. 通过chain_code 去 z_hr_chain_info 获取 链群 和 小微信息
        // 4. 通过合约id 去 z_gambling_contracts_product_index 获取对应月份预计
        // 5. 实际 - 通过x_chain_info表中的 xwcode ，去  x_xw_actual计算实际

        //contractListsDto.setEmpSN(xWcode);

        List<ContractListRes> list = zGrabContractsDao.getContractList(contractListsDto);
        list.add(new ContractListRes());
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
        // 链群等级  现在规则没出来  卡住了
        map.put("chainGroupRank","暂时不知道咋算~");
        // 链群的增值分享    实际利润 - 举单利润（链群）
        // 链群实际利润
        BigDecimal chainFact = xXwActualDao.getChainFact(chainGroupInfoDto);
        // 链群目标利润 TODO
        BigDecimal chainEstimate;

        map.put("chainShare","");
        // 节点、人的增值分享   实际利润 - 举单利润（*百分比  个人）
        map.put("nodeShare","");
        // 吸引力预计
        map.put("estimate","");
        // 吸引力实际
        map.put("fact","");
        return map;
    }


    /**
     * 外部获取数据接口
     * @param dataInfo
     * @return
     */
    @Override
    public Map<String, Object> getContractData(DataInfo dataInfo) {
        Map<String, Object> map = new HashMap<>();

        // 转换时间
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = DateFormatUtil.stringToDate(dataInfo.getMonth() , "yyyy-MM");
        String startTime = format.format(date);
        Date endDate = DateFormatUtil.addDateMonths(date , 1);
        String endTime = format.format(endDate);

        // 链群组织信息
        ZHrChainInfo chainInfo = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>().eq("chain_code" , dataInfo.getChainCode()));

        map.put("chainCode" , chainInfo.getChainCode());
        map.put("chainName" , chainInfo.getChainName());
        map.put("xwCode" , chainInfo.getXwCode());
        map.put("xwName" , chainInfo.getXwName());
        map.put("masterCode" , chainInfo.getMasterCode());
        map.put("masterName" , chainInfo.getMasterName());
        map.put("chainPtCode" , chainInfo.getChainPtCode());

        // 合约信息
        QueryWrapper<ZContracts> query = new QueryWrapper<ZContracts>();
        query.eq("chain_code"  , dataInfo.getChainCode())
                .eq("contract_type" , "10")
                .gt("create_time" , startTime)
                .lt("create_time" , endTime);
        if(dataInfo.getStatus() != null && !"".equals(dataInfo.getStatus())){
            query.eq("status" , dataInfo.getStatus());
        }
        if(!"".equals(dataInfo.getContractId())){
            query.eq("id" , dataInfo.getContractId());
        }
        ZContracts zContracts = zContractsDao.selectOne(query);

        map.put("contractId" , zContracts.getId());
        map.put("contractName" , zContracts.getContractName());
        map.put("startDate" , zContracts.getStartDate());
        map.put("endDate" , zContracts.getEndDate());
        map.put("createTime" , zContracts.getCreateTime());
        map.put("regionCode" , zContracts.getRegionCode());
        map.put("status" , zContracts.getStatus());

        map.put("shareSpace" , zContracts.getShareSpace()); // 增值空间

        // 获取ZContractsFactor部分信息
        getContractsFactor(zContracts , map);

        // 获取抢单信息
        List<ZContracts> grabContractsList = zContractsDao.selectList(new QueryWrapper<ZContracts>()
                .eq("parent_id" , zContracts.getId()));

        // 抢单信息dto
        List<GrabInfo2Outside> grabList = new ArrayList<>();
        for(ZContracts contracts : grabContractsList){
            // 抢单信息
            GrabInfo2Outside grabInfo2Outside = new GrabInfo2Outside();
            BeanUtils.copyProperties(contracts , grabInfo2Outside);
            // 抢单factor信息
            List<ZContractsFactor> factorList = zContractsFactorDao.selectList(new QueryWrapper<ZContractsFactor>()
                    .eq("contract_id" , contracts.getId()));
            for(ZContractsFactor factor : factorList){
                if(factor.getFactorType().equals(Constant.FactorType.Grab.getValue())){
                    grabInfo2Outside.setGrabTargetIncom(factor.getFactorValue());
                    grabInfo2Outside.setGrabTargetUnit(factor.getFactorUnit());
                }
                if(factor.getFactorType().equals(Constant.FactorType.Bottom.getValue())){
                    grabInfo2Outside.setGrabTargetBottom(factor.getFactorValue());
                }
            }
            grabList.add(grabInfo2Outside);
            // 预案信息
            PlanInfoDto planInfoDto = zReservePlanDao.selectPlanInfo(String.valueOf(contracts.getId())).get(0);
            grabInfo2Outside.setGrabTargetPlan(planInfoDto.getContent());
            // 并联预案信息


        }
        map.put("grabInfo" , grabList);

        return map;
    }


    /**
     * 获取 ZContractsFactor部分信息
     * @return
     */
    public Map<String , Object> getContractsFactor(ZContracts zContracts , Map<String , Object> map){
        /**
         * 底线目标
         */
        List<ZContractsFactor> bottomFactorList = zContractsFactorDao.selectList(
                new QueryWrapper<ZContractsFactor>()
                        .eq("contract_id" , zContracts.getId())
                        .eq("factor_type" , Constant.FactorType.Bottom.getValue()));

        for (ZContractsFactor exp : bottomFactorList){
            if(exp.getFactorCode().equals(Constant.FactorCode.Incom.getValue())){
                map.put("bottomTargetIncome" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Lre.getValue())){
                map.put("bottomTargetProfit" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Mll.getValue())){
                map.put("bottomTargetRate" , exp.getFactorValue());
            }
        }

        /**
         * e2e目标
         */
        List<ZContractsFactor> e2eFactorList = zContractsFactorDao.selectList(
                new QueryWrapper<ZContractsFactor>()
                        .eq("contract_id" , zContracts.getId())
                        .eq("factor_type" , Constant.FactorType.E2E.getValue()));

        for (ZContractsFactor exp : e2eFactorList){
            if(exp.getFactorCode().equals(Constant.FactorCode.Incom.getValue())){
                map.put("e2eTargetIncome" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Lre.getValue())){
                map.put("e2eTargetProfit" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Mll.getValue())){
                map.put("e2eTargetRate" , exp.getFactorValue());
            }
        }

        /**
         * 抢单目标
         */
        List<ZContractsFactor> grabFactorList = zContractsFactorDao.selectList(
                new QueryWrapper<ZContractsFactor>()
                        .eq("contract_id" , zContracts.getId())
                        .eq("factor_type" , Constant.FactorType.Grab.getValue()));

        for (ZContractsFactor exp : grabFactorList){
            if(exp.getFactorCode().equals(Constant.FactorCode.Incom.getValue())){
                map.put("grabTargetIncome" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Lre.getValue())){
                map.put("grabTargetProfit" , exp.getFactorValue());
            }
            if(exp.getFactorCode().equals(Constant.FactorCode.Mll.getValue())){
                map.put("grabTargetRate" , exp.getFactorValue());
            }
        }

        return map;
    }





}
