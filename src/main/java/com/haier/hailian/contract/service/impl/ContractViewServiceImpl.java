package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.ContractViewDataCD;
import com.haier.hailian.contract.dto.ContractViewResultDTO;
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

        return result;
    }

    @Override
    public Map<String, List<ZContractsFactor>> getContractViewDataTY(String contractId){
        Map<String, List<ZContractsFactor>> resultMap = new HashMap<>();
        List<ZContractsFactor> factorList = factorDao.selectFactorForViewTY(contractId, "02");

        if(factorList != null && !factorList.isEmpty()){
            for(ZContractsFactor factor : factorList){
                List<ZContractsFactor> factors = resultMap.get(factor.getRegionName());
                if(factors == null){
                    factors = new ArrayList<>();
                    resultMap.put(factor.getRegionName(), factors);
                }
                factors.add(factor);
            }
        }

        return resultMap;
    }

    @Override
    public List<ContractViewDataCD> getContractViewDataCD(String contractId){
        List<ContractViewDataCD> resultList = contractsDao.selectContractsViewForCD(contractId);
        return resultList;
    }
}
