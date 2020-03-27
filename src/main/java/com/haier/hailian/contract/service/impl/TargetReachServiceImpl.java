package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.FactorGrabResDTO;
import com.haier.hailian.contract.dto.QueryContractListDTO;
import com.haier.hailian.contract.dto.TargetListResDTO;
import com.haier.hailian.contract.dto.TargetReachSaveReqDTO;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TargetReachServiceImpl implements com.haier.hailian.contract.service.TargetReachService {

    @Autowired
    ZContractsFactorDao factorDao;
    @Autowired
    ZContractsFactorService factorService;
    @Autowired
    ZContractsDao contractsDao;

    @Override
    public List<ZContracts> selectContractListForTarget(QueryContractListDTO queryDTO) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        queryDTO.setParentId(0);
        List<ZContracts> list = contractsDao.selectContractListForTarget(queryDTO);
        //查询还没有抢入、抢入未截止的合约
        String contractIds = contractsDao.selectContractToUpdate()+",";
        if(null != list && list.size() > 0){
            for(ZContracts zContracts:list){
                int id = zContracts.getId();
                if(contractIds.indexOf(id+",")<0){
                    zContracts.setStatus2("0");
                }else {
                    zContracts.setStatus2("1");
                }
                if(null != zContracts.getCheckTime() && zContracts.getCheckTime().after(new Date())){
                    zContracts.setStatus4("1");
                }else {
                    zContracts.setStatus4("0");
                }
            }
        }
        return list;
    }

    @Override
    public List<TargetListResDTO> getFactorGrabList(String contractId){
        Map<String,Object> map = new HashMap<>();
        map.put("contractId", contractId);
        List<TargetListResDTO> resultList = new ArrayList<>();

        List<FactorGrabResDTO> list = factorDao.getFactorGrabList(map);
        for(FactorGrabResDTO item : list){
            TargetListResDTO target = new TargetListResDTO();
            BeanUtils.copyProperties(item, target);
            if(!resultList.contains(target)){
                target.getGrabList().add(item);
                resultList.add(target);
            }else{
                target = resultList.get(resultList.indexOf(target));
                target.getGrabList().add(item);
            }

        }
        return resultList;
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
