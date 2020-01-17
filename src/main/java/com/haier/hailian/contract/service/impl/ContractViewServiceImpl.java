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
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
            if("desc".equalsIgnoreCase(orderType)){
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
    public Map<String, Object> getContractViewDataTYNew(ContractViewRequestNewDTO requestBean){
        Map<String, Object> resultMap = new HashMap<>();
        List<ContractViewDataTYResponseNewDTO> resultList = new ArrayList<>();
        List<ContractViewDataTYResponseNewDTO> filterListJD = new ArrayList<>();
        List<ContractViewDataTYResponseNewDTO> filterListE2E = new ArrayList<>();
        List<ContractViewDataTYResponseNewDTO> filterListGrabed = new ArrayList<>();
        List<ContractViewDataTYResponseNewDTO> filterListNotGrab = new ArrayList<>();
        resultMap.put("data", resultList);
        resultMap.put("lessthanJDCount", "0");
        resultMap.put("lessthanE2ECount", "0");
        resultMap.put("filterListGrabed", "0");
        resultMap.put("filterListNotGrab", "0");

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("contractId", requestBean.getContractId());
        paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
//        if(StringUtils.isNotBlank(requestBean.getFilterStr())){
//           if("grabed".equals(requestBean.getFilterStr())){
//                paraMap.put("isgrab", "yes");
//            }else if("notGrab".equals(requestBean.getFilterStr())){
//                paraMap.put("isgrab", "no");
//            }
//        }

        // 排序
        if(StringUtils.isNotBlank(requestBean.getOrderStr())){
            if("incomeDesc".equals(requestBean.getFilterStr())){
                paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
            }else if("highDesc".equals(requestBean.getFilterStr())){
                paraMap.put("factorCode", Constant.FactorCode.HighPercent.getValue());
            }
        }else {
            paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
        }

        // 按名称检索
        if(StringUtils.isNotBlank(requestBean.getXwName())){
            paraMap.put("xwName", requestBean.getXwName());
        }

        resultList = contractsDao.selectContractsViewForTYNew(paraMap);
        resultMap.put("data", resultList);
        if(resultList == null || resultList.isEmpty()){
            return resultMap;
        }

        // 过滤已抢入
        filterListGrabed = resultList.stream().filter(f-> "1".equals(f.getIsGrab())).collect(Collectors.toList());
        resultMap.put("grabedCount", filterListGrabed.size());

        // 过滤未抢入
        filterListNotGrab = resultList.stream().filter(f-> "0".equals(f.getIsGrab())).collect(Collectors.toList());
        resultMap.put("notGrabCount", filterListNotGrab.size());

        // 过滤抢单低于举单
        for(ContractViewDataTYResponseNewDTO item : resultList){
            if(item.getQdList() == null || item.getQdList().isEmpty() || item.getQdList().size() != 2){
                continue;
            }

            if(item.getJdList() != null && !item.getJdList().isEmpty() && item.getJdList().size() == 2){
                if(new BigDecimal(item.getQdList().get(0).getTargetValue()).compareTo(new BigDecimal(item.getJdList().get(0).getTargetValue())) < 0 || new BigDecimal(item.getQdList().get(1).getTargetValue()).compareTo(new BigDecimal(item.getJdList().get(1).getTargetValue())) < 0){
                    filterListJD.add(item);
                }
            }
        }
        resultMap.put("lessthanJDCount", filterListJD.size());

        // 过滤抢单低于E2E
        for(ContractViewDataTYResponseNewDTO item : resultList){
            if(item.getQdList() == null || item.getQdList().isEmpty() || item.getQdList().size() != 2){
                continue;
            }

            if(item.getE2eList() != null && !item.getE2eList().isEmpty()){
                if(new BigDecimal(item.getQdList().get(0).getTargetValue()).compareTo(new BigDecimal(item.getE2eList().get(0).getTargetValue())) < 0 || new BigDecimal(item.getQdList().get(1).getTargetValue()).compareTo(new BigDecimal(item.getE2eList().get(1).getTargetValue())) < 0){
                    filterListE2E.add(item);
                }
            }
        }
        resultMap.put("lessthanE2ECount", filterListE2E.size());

        if(StringUtils.isNotBlank(requestBean.getFilterStr()) && "grabed".equals(requestBean.getFilterStr())){
            resultMap.put("data", filterListGrabed);
        }else if(StringUtils.isNotBlank(requestBean.getFilterStr()) && "notGrab".equals(requestBean.getFilterStr())){
            resultMap.put("data", filterListNotGrab);
        }else if(StringUtils.isNotBlank(requestBean.getFilterStr()) && "lessthanJD".equals(requestBean.getFilterStr())){
            resultMap.put("data", filterListJD);
        }else if(StringUtils.isNotBlank(requestBean.getFilterStr()) && "lessthanE2E".equals(requestBean.getFilterStr())){
            resultMap.put("data", filterListE2E);
        }else{
            resultMap.put("data", resultList);
        }

        // 计算抢单完成率
        if(StringUtils.isNotBlank(requestBean.getOrderStr()) && "grabRateDesc".equals(requestBean.getOrderStr())){
            BigDecimal grabRate = BigDecimal.ZERO;
            for(ContractViewDataTYResponseNewDTO item : (List<ContractViewDataTYResponseNewDTO>)resultMap.get("data")){
                if(item.getQdList() == null || item.getQdList().isEmpty() || item.getQdList().size() != 2){
                    continue;
                }
                if(item.getJdList() == null || item.getJdList().isEmpty() || item.getJdList().size() != 2){
                    continue;
                }
                if(item.getE2eList() == null || item.getE2eList().isEmpty() || item.getE2eList().size() != 2){
                    continue;
                }

                BigDecimal rateImcome = ((((new BigDecimal(item.getQdList().get(0).getTargetValue()).subtract(new BigDecimal(item.getJdList().get(0).getTargetValue()))).divide(new BigDecimal(item.getJdList().get(0).getTargetValue()))))
                        .add((new BigDecimal(item.getQdList().get(0).getTargetValue()).subtract(new BigDecimal(item.getE2eList().get(0).getTargetValue()))).divide(new BigDecimal(item.getE2eList().get(0).getTargetValue())))).divide(new BigDecimal("2"));

                BigDecimal rateHigh = ((((new BigDecimal(item.getQdList().get(1).getTargetValue()).subtract(new BigDecimal(item.getJdList().get(1).getTargetValue()))).divide(new BigDecimal(item.getJdList().get(1).getTargetValue()))))
                        .add((new BigDecimal(item.getQdList().get(1).getTargetValue()).subtract(new BigDecimal(item.getE2eList().get(1).getTargetValue()))).divide(new BigDecimal(item.getE2eList().get(1).getTargetValue())))).divide(new BigDecimal("2"));
                grabRate = (rateImcome.add(rateHigh)).divide(new BigDecimal("2"));
                item.setGrabRate(grabRate);
            }

            Collections.sort((List<ContractViewDataTYResponseNewDTO>)resultMap.get("data"), new Comparator<ContractViewDataTYResponseNewDTO>() {
                @Override
                public int compare(ContractViewDataTYResponseNewDTO o1, ContractViewDataTYResponseNewDTO o2) {
                    return o1.getGrabRate().subtract(o2.getGrabRate()).compareTo(BigDecimal.ZERO);
                }
            });
        }


        return resultMap;
    }

    @Override
    public int selectContractsViewForTYCount(String contractId){
        return contractsDao.selectContractsViewForTYCount(contractId);
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
