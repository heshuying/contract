package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.ContractViewService;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 19033323
 */
@Service
public class ContractViewServiceImpl implements ContractViewService {
    @Autowired
    ZContractsFactorDao factorDao;
    @Autowired
    ZContractsDao contractsDao;
    @Autowired
    ZHrChainInfoDao zHrChainInfoDao;

    @Override
    public ContractViewResultDTO getContractViewData(String contractId){
        ContractViewResultDTO result = new ContractViewResultDTO();

        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts != null){
            result.setStartDate(DateFormatUtil.format(contracts.getStartDate(),DateFormatUtil.DATE_PATTERN));
            result.setEndDate(DateFormatUtil.format(contracts.getEndDate(),DateFormatUtil.DATE_PATTERN));
            result.setShareSpace(contracts.getShareSpace());
            result.setStatus(contracts.getStatus());
            result.setStatusName(getStatusName(contracts.getStatus()));
        }

        ZHrChainInfo chain = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
        if(chain != null){
            result.setChainName(chain.getChainName());
            result.setChainMasterName(chain.getMasterName());
        }

        List<ZContractsFactor> bottomList = factorDao.selectFactorForView(contractId, "01");
        List<ZContractsFactor> e2eList = factorDao.selectFactorForView(contractId, "03");
        List<ZContractsFactor> grabList = factorDao.selectFactorForView(contractId, "02");
        result.setBottomList(bottomList);
        result.setE2eList(e2eList);
        result.setGrabList(grabList);

        // 体验抢单目标
       /* BigDecimal incom = BigDecimal.ZERO;
        Integer highPercent = 0;
        Integer lowPercent = 0;
        List<ZContractsFactor> grabTYList = new ArrayList<>();
        List<ContractViewDataTY> factorList = contractsDao.selectContractsViewForTY(contractId);
        if(factorList != null && !factorList.isEmpty()){
            for(ContractViewDataTY factor : factorList){
                if(Constant.FactorCode.Incom.getValue().equals(factor.getFactorCode())){
                    incom = incom.add(new BigDecimal(factor.getFactorValue()));
                }else if(Constant.FactorCode.HighPercent.getValue().equals(factor.getFactorCode())){
                    highPercent = highPercent + Integer.parseInt(factor.getFactorValue());
                }else if(Constant.FactorCode.LowPercent.getValue().equals(factor.getFactorCode())){
                    lowPercent = lowPercent + Integer.parseInt(factor.getFactorValue());
                }
            }
            highPercent = highPercent / factorList.size();
            lowPercent = lowPercent / factorList.size();

            ZContractsFactor factor1 = new ZContractsFactor();
            factor1.setFactorName(Constant.FactorCode.Incom.getName());
            factor1.setFactorCode(Constant.FactorCode.Incom.getValue());
            factor1.setFactorValue(incom.toString());
            factor1.setFactorUnit("元");
            grabTYList.add(factor1);

            ZContractsFactor factor2 = new ZContractsFactor();
            factor2.setFactorName(Constant.FactorCode.HighPercent.getName());
            factor2.setFactorCode(Constant.FactorCode.HighPercent.getValue());
            factor2.setFactorValue(String.valueOf(highPercent));
            factor2.setFactorUnit("%");
            grabTYList.add(factor2);

            ZContractsFactor factor3 = new ZContractsFactor();
            factor3.setFactorName(Constant.FactorCode.LowPercent.getName());
            factor3.setFactorCode(Constant.FactorCode.LowPercent.getValue());
            factor3.setFactorValue(String.valueOf(lowPercent));
            factor3.setFactorUnit("%");
            grabTYList.add(factor3);
        }*/

        List<FactorConfigDTO> grabTYList = contractsDao.selectContractsViewForTYSum(contractId);
        result.setGrabTYList(grabTYList);

        return result;
    }

    @Override
    public Map<String, List<ContractViewDataTY>> getContractViewDataTYOld(String contractId){
        Map<String, List<ContractViewDataTY>> resultMap = new HashMap<>();
        List<ContractViewDataTY> factorList = contractsDao.selectContractsViewForTY(contractId);

        if(factorList != null && !factorList.isEmpty()){
            for(ContractViewDataTY factor : factorList){
                List<ContractViewDataTY> factors = resultMap.get(factor.getXwName());
                if(factors == null){
                    factors = new ArrayList<>();
                    resultMap.put(factor.getXwName(), factors);
                }
                factors.add(factor);
            }
        }

        return resultMap;
    }

    @Override
    public List<ContractViewDataTYResultDTO> getContractViewDataTY(String contractId){
        List<ContractViewDataTYResultDTO> resultList = new ArrayList<>();
        Map<String,ContractViewDataTYResultDTO> tempMap = new HashMap<>();

        List<TargetTitleTYDTO> titleList = contractsDao.selectContractsTitleForTY(contractId);
        List<ContractViewDataTY> factorList = contractsDao.selectContractsViewForTY(contractId);

        if(factorList != null && !factorList.isEmpty()){
            for(ContractViewDataTY factor : factorList){
                ContractViewDataTYResultDTO resultDTO = tempMap.get(factor.getXwName());
                if(resultDTO == null){
                    resultDTO = new ContractViewDataTYResultDTO();
                    resultDTO.setXwName(factor.getXwName());
                    tempMap.put(factor.getXwName(), resultDTO);
                }
            }
        }

        if(factorList != null && !factorList.isEmpty()){
            for(String xwName : tempMap.keySet()) {
                ContractViewDataTYResultDTO resultDTO = tempMap.get(xwName);
                List<TargetConfigDTO> targetConfigList = new ArrayList<>();
                for (ContractViewDataTY factor : factorList) {
                    if(xwName.equals(factor.getXwName())){
                        resultDTO.setTargetList(targetConfigList);
                        TargetConfigDTO targetConfigDTO = new TargetConfigDTO();
                        targetConfigDTO.setTargetCode(factor.getFactorCode());
                        targetConfigDTO.setTargetName(factor.getFactorName());
                        targetConfigDTO.setTargetValue(factor.getFactorValue());
                        targetConfigDTO.setTargetType(factor.getFactorType());
                        targetConfigList.add(targetConfigDTO);
                    }

                }
                resultList.add(resultDTO);
            }
        }

        return resultList;
    }

    @Override
    public List<TargetTitleTYDTO> getTargetTitleList(String contractId){
        List<TargetTitleTYDTO> titleList = contractsDao.selectContractsTitleForTY(contractId);
        return titleList;
    }


    @Override
    public List<ContractViewDataCD> getContractViewDataCD(String contractId){
        List<ContractViewDataCD> resultList = contractsDao.selectContractsViewForCD(contractId);
        return resultList;
    }

    /**
     * 获取合约状态名称
     * @param status
     * @return
     */
    private String getStatusName(String status){
        switch (status){
            case "0" : return "抢入中";
            case "1" : return "已生效";
            case "4" : return "已失效";
            default: return "";
        }
    }
}
