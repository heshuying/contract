package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.FactorGrabResDTO;
import com.haier.hailian.contract.dto.TargetReachSaveReqDTO;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TargetReachServiceImpl implements com.haier.hailian.contract.service.TargetReachService {

    @Autowired
    ZContractsFactorDao factorDao;
    @Autowired
    ZContractsFactorService factorService;

    @Override
    public List<FactorGrabResDTO> getFactorGrabList(String contractId){
        Map<String,Object> map = new HashMap<>();
        map.put("contractId", contractId);
        return factorDao.getFactorGrabList(map);
    }

    @Override
    public void saveTargetActual(List<TargetReachSaveReqDTO> reqBean){
        if(reqBean == null || reqBean.isEmpty()){
            return;
        }

        List<ZContractsFactor> factors = new ArrayList<>();
        for(TargetReachSaveReqDTO item : reqBean){
            ZContractsFactor factor = new ZContractsFactor();
            factor.setId(Integer.parseInt(item.getFactId()));
            factor.setFactorValueActual(item.getTargetActual());
            factors.add(factor);
        }
        factorService.updateBatchById(factors);
    }

}
