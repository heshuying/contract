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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

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
        result.setBottomList(bottomList);
        List<ZContractsFactor> e2eList = factorDao.selectFactorForView(contractId, "03");
        result.setE2eList(e2eList);
        List<ZContractsFactor> grabList = factorDao.selectFactorForView(contractId, "02");
        result.setGrabList(grabList);
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
    public List<ContractViewDataTYResultDTO> getContractViewDataTY(String contractId, String orderType){
        List<ContractViewDataTYResultDTO> resultList = new ArrayList<>();
        Map<String,ContractViewDataTYResultDTO> tempMap = new HashMap<>();

//        List<TargetTitleTYDTO> titleList = contractsDao.selectContractsTitleForTY(contractId);
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
                        if(StringUtils.isNotBlank(factor.getFactorValue())){
                            targetConfigDTO.setTargetValue(new BigDecimal(factor.getFactorValue()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        }
                        targetConfigDTO.setTargetType(factor.getFactorType());
                        targetConfigList.add(targetConfigDTO);
                    }

                }

                if(targetConfigList == null || targetConfigList.isEmpty()){
                    resultDTO.setDiff(0);
                }
                int diff = 0;
                for(int i=0; i<targetConfigList.size(); i=i+2){
                    if(i >= targetConfigList.size()-1){
                        break;
                    }
                    if(new BigDecimal(targetConfigList.get(i+1).getTargetValue()).compareTo(new BigDecimal(targetConfigList.get(i).getTargetValue())) < 0){
                        diff++;
                    }
                }
                resultDTO.setDiff(diff);
                resultList.add(resultDTO);
            }
            if("desc".equals(orderType)){
                Collections.sort(resultList, new Comparator<ContractViewDataTYResultDTO>() {
                    @Override
                    public int compare(ContractViewDataTYResultDTO o1, ContractViewDataTYResultDTO o2) {
                        return o2.getDiff() - o1.getDiff();
                    }
                });
            }else{
                Collections.sort(resultList, new Comparator<ContractViewDataTYResultDTO>() {
                    @Override
                    public int compare(ContractViewDataTYResultDTO o1, ContractViewDataTYResultDTO o2) {
                        return o1.getDiff() - o2.getDiff();
                    }
                });
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
    public Collection<ContractViewDataCDResponseDTO> getContractViewDataCD(String contractId){
        List<ContractViewDataCDResponseDTO> resultList = new ArrayList<>();
        List<ContractViewDataCD> list = contractsDao.selectContractsViewForCD(contractId);
        Map<String, ContractViewDataCDResponseDTO> resultMap = new HashMap<String, ContractViewDataCDResponseDTO>();

        if(list != null && !list.isEmpty()){
            for(ContractViewDataCD item : list){
                ContractViewDataCDResponseDTO data = new ContractViewDataCDResponseDTO();
                data = resultMap.get(item.getNodeCode());
                if(data == null){
                    data = new ContractViewDataCDResponseDTO();
                    BeanUtils.copyProperties(item, data);
                    resultMap.put(item.getNodeCode(), data);
                }

                TargetViewDTO targetViewDTO = new TargetViewDTO();
                BeanUtils.copyProperties(item, targetViewDTO);
                data.getTargetList().add(targetViewDTO);
            }
        }

        return resultMap.values();
    }

    @Override
    public String getContractSize(String contractId) {
        Integer fact = contractsDao.selectCount(new QueryWrapper<ZContracts>()
                .eq("parent_id" , contractId)
                .eq("contract_type" , "30")
                .eq("status", "1"));
        Integer target = contractsDao.getContractSize(contractId);
        return fact + "/" + target;
    }


    @Override
    public Integer getContractSize2(String contractId) {
        Integer target = contractsDao.getContractSize2(contractId);
        return target;
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
            case "5" : return "已撤销";
            case "6" : return "已删除";
            default: return "";
        }
    }
}
