package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ContractViewService;
import com.haier.hailian.contract.service.IncrementService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import com.haier.hailian.contract.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    ZContractsService contractsService;
    @Autowired
    ZHrChainInfoDao zHrChainInfoDao;
    @Autowired
    ZContractsProductDao contractsProductDao;
    @Autowired
    ZNodeTargetPercentInfoDao zNodeTargetPercentInfoDao;
    @Autowired
    IncrementService incrementService;
    @Autowired
    ZContractsXwType3Dao xwType3Dao;

    private static final ExcelUtil.CellHeadField[] Serial_Header = {
            new ExcelUtil.CellHeadField("场景", "sceneName"),
            new ExcelUtil.CellHeadField("系列", "productSeries"),
            new ExcelUtil.CellHeadField("维度", "targetName"),
            new ExcelUtil.CellHeadField("单位", "targetUnit"),
            new ExcelUtil.CellHeadField("年计划", "qtyYear"),
            new ExcelUtil.CellHeadField("年累", "actualYear"),
            new ExcelUtil.CellHeadField("月计划", "qtyMonth"),
            new ExcelUtil.CellHeadField("月累", "actualMonth")

    };
    private static ExcelUtil.CellHeadField[] Ty_Export_Header = {
            new ExcelUtil.CellHeadField("链群编码", "chainCode"),
            new ExcelUtil.CellHeadField("链群名称", "chainName"),
            new ExcelUtil.CellHeadField("合约编码", "parentId"),
            new ExcelUtil.CellHeadField("合约开始时间", "startDate"),
            new ExcelUtil.CellHeadField("合约结束时间", "endDate"),
            new ExcelUtil.CellHeadField("抢单编码", "id"),
            new ExcelUtil.CellHeadField("小微编码", "xwCode"),
            new ExcelUtil.CellHeadField("小微名称", "xwName"),
            new ExcelUtil.CellHeadField("抢单组织编码", "orgCode"),
            new ExcelUtil.CellHeadField("抢单组织名称", "orgName"),
            new ExcelUtil.CellHeadField("抢单人工号", "createCode"),
            new ExcelUtil.CellHeadField("抢单人", "createName"),
            new ExcelUtil.CellHeadField("抢单时间", "createTime"),

    };
    private static final ExcelUtil.CellHeadField[] Cd_Export_Header = {
            new ExcelUtil.CellHeadField("链群编码", "chainCode"),
            new ExcelUtil.CellHeadField("链群名称", "chainName"),
            new ExcelUtil.CellHeadField("合约编码", "parentId"),
            new ExcelUtil.CellHeadField("合约开始时间", "startDate"),
            new ExcelUtil.CellHeadField("合约结束时间", "endDate"),
            new ExcelUtil.CellHeadField("抢单编码", "id"),
            new ExcelUtil.CellHeadField("小微编码", "xwCode"),
            new ExcelUtil.CellHeadField("小微名称", "xwName"),
            new ExcelUtil.CellHeadField("抢单组织编码", "orgCode"),
            new ExcelUtil.CellHeadField("抢单组织名称", "orgName"),
            new ExcelUtil.CellHeadField("抢单人工号", "createCode"),
            new ExcelUtil.CellHeadField("抢单人", "createName"),
            new ExcelUtil.CellHeadField("抢单时间", "createTime"),

            new ExcelUtil.CellHeadField("抢单", "factorName"),
            new ExcelUtil.CellHeadField("值", "factorValue"),
            new ExcelUtil.CellHeadField("预案", "content"),

    };
    @Autowired
    private HttpServletRequest request;

    @Override
    public ContractViewResultDTO getContractViewData(String contractId){
        ContractViewResultDTO result = new ContractViewResultDTO();

        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts != null){
            result.setStartDate(DateFormatUtil.format(contracts.getStartDate(),DateFormatUtil.DATE_PATTERN));
            result.setEndDate(DateFormatUtil.format(contracts.getEndDate(),DateFormatUtil.DATE_PATTERN));
            result.setShareSpace(contracts.getShareSpace());
            result.setShareMoney(contracts.getShareMoney());
            result.setStatus(contracts.getStatus());
            result.setStatusName(getStatusName(contracts.getStatus()));
            result.setJoinTime(DateFormatUtil.format(contracts.getJoinTime(),DateFormatUtil.DATE_TIME_PATTERN));
            result.setCheckTime(DateFormatUtil.format(contracts.getCheckTime(),DateFormatUtil.DATE_TIME_PATTERN));
            if(null != contracts.getEndDate() && contracts.getEndDate().after(new Date())){
                result.setStatus5("1");
            }else {
                result.setStatus5("0");
            }
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
        List<SubContractInfo> subList = getSubContractList(contractId);
        result.setSubContractList(subList);
        return result;
    }

    /**
     * 获取自合约列表
     * @param contractId
     * @return
     */
    public List<SubContractInfo> getSubContractList(String contractId){
        List<SubContractInfo> subList = new ArrayList<SubContractInfo>();
        List<ZContracts> contracts = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id", contractId).eq("contract_type", "10"));
        if(contracts == null || contracts.isEmpty()){
            return new ArrayList<>();
        }

        for(ZContracts contract : contracts){
            String subId = String.valueOf(contract.getId());
            SubContractInfo subContract = new SubContractInfo();
            subContract.setContractId(subId);

            ZHrChainInfo chain = zHrChainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contract.getChainCode()));
            if(chain != null){
                subContract.setChainName(chain.getChainName());
            }

            // 计算体验个数
            Integer grabSize = selectContractsViewForTYCount(subId);
            Integer size = getContractSize2(subId);
            subContract.setCountTY(grabSize + "/" + size);

            // 计算爆款个数
            List<ContractProductDTO> list = this.staticSerial(contract.getId());

            if (list == null || list.isEmpty()){
                subContract.setCountBK("0");
            }else{
                subContract.setCountBK(String.valueOf(list.size()));
            }

            // 计算创单个数
            int countGrabed = 0;
            List<CDGrabType3> type3List = this.queryCDGrabDataXWType3(subId, "");
            if(type3List != null && !type3List.isEmpty()){
                for(CDGrabType3 item : type3List){
                    List<CDGrabDataDTO> grabList = this.queryGrabListXWType3(subId, item.getXwType3Code(), null);
                    if(grabList != null && !grabList.isEmpty()){
                        item.setGrabCount(String.valueOf(grabList.size()));
                        countGrabed++;
                    }else{
                        item.setGrabCount("0");
                    }
                }
            }
            subContract.setCountCD(countGrabed+"/"+type3List.size());

            subList.add(subContract);
        }
        return subList;
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
//        if(StringUtils.isNotBlank(requestBean.getOrderStr())){
//            if("incomeDesc".equals(requestBean.getOrderStr())){
//                paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
//            }else if("highDesc".equals(requestBean.getOrderStr())){
//                paraMap.put("factorCode", Constant.FactorCode.HighPercent.getValue());
//            }
//        }else {
//            paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
//        }

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
                if(new BigDecimal(item.getQdList().get(0).getTargetValue()).compareTo(new BigDecimal(item.getJdList().get(0).getTargetValue())) < 0){
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
                if(new BigDecimal(item.getQdList().get(0).getTargetValue()).compareTo(new BigDecimal(item.getE2eList().get(0).getTargetValue())) < 0){
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

                BigDecimal rateImcome = ((((new BigDecimal(item.getQdList().get(0).getTargetValue()).subtract(new BigDecimal(item.getJdList().get(0).getTargetValue()))).divide(new BigDecimal(item.getJdList().get(0).getTargetValue()), BigDecimal.ROUND_HALF_UP)))
                        .add((new BigDecimal(item.getQdList().get(0).getTargetValue()).subtract(new BigDecimal(item.getE2eList().get(0).getTargetValue()))).divide(new BigDecimal(item.getE2eList().get(0).getTargetValue()), BigDecimal.ROUND_HALF_UP))).divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP);

                BigDecimal rateHigh = (((new BigDecimal(item.getQdList().get(1).getTargetValue()).subtract(new BigDecimal(item.getJdList().get(1).getTargetValue()))).divide(new BigDecimal(item.getJdList().get(1).getTargetValue()), BigDecimal.ROUND_HALF_UP)));
                // e2e的高端占比有0的情况作为分母会报错
//                        .add((new BigDecimal(item.getQdList().get(1).getTargetValue()).subtract(new BigDecimal(item.getE2eList().get(1).getTargetValue()))).divide(new BigDecimal(item.getE2eList().get(1).getTargetValue()), BigDecimal.ROUND_HALF_UP))).divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP);
                grabRate = (rateImcome.add(rateHigh)).divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP);
                item.setGrabRate(grabRate);
            }

            Collections.sort((List<ContractViewDataTYResponseNewDTO>)resultMap.get("data"), new Comparator<ContractViewDataTYResponseNewDTO>() {
                @Override
                public int compare(ContractViewDataTYResponseNewDTO o1, ContractViewDataTYResponseNewDTO o2) {
                    if(Integer.parseInt(o2.getIsGrab()) > Integer.parseInt(o1.getIsGrab())){
                        return 1;
                    }else if(Integer.parseInt(o2.getIsGrab()) < Integer.parseInt(o1.getIsGrab())){
                        return -1;
                    }else if(Integer.parseInt(o2.getIsGrab()) == Integer.parseInt(o1.getIsGrab())){
                        return o2.getGrabRate().subtract(o1.getGrabRate()).compareTo(BigDecimal.ZERO);
                    }
                    return o2.getGrabRate().subtract(o1.getGrabRate()).compareTo(BigDecimal.ZERO);
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
    public Collection<ContractViewDataCDResponseDTO> getContractViewDataCD(String contractId, String xwName){
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("contractId", contractId);
        if(StringUtils.isNotBlank(xwName)){
            paraMap.put("xwName", xwName);
        }
        List<ContractViewDataCDResponseDTO> resultList = new ArrayList<>();
        List<ContractViewDataCD> list = contractsDao.selectContractsViewForCD(paraMap);
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

    /**
     * 查询创单抢单type3列表
     * @param contractId
     * @return
     */
    @Override
    public List<CDGrabType3> queryCDGrabDataXWType3(String contractId, String keyword){
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("contractId", contractId);
        paraMap.put("keyword", keyword);

        return zNodeTargetPercentInfoDao.queryCDGrabDataXWType3(paraMap);
    }

    @Override
    public List<CDGrabDataDTO> queryGrabListXWType3(String contractId, String type3Code, String grabId){
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("contractId", contractId);
        paraMap.put("type3Code", type3Code);
        paraMap.put("grabId", grabId);

        return contractsDao.getCDGrabResultType3List(paraMap);
    }

    @Override
    public List<String> queryContractsForUpdate(){
        return contractsDao.getContractsForCountUpdate(new HashMap<>());
    }

    @Override
    public void insertXWType3Count(String contractId, CDGrabType3 cdGrabType3){
        ZContractsXwType3 type3 = new ZContractsXwType3();
        type3.setContractId(Integer.parseInt(contractId));
        type3.setXwType3(cdGrabType3.getXwType3Name());
        type3.setXwType3Code(cdGrabType3.getXwType3Code());
        type3.setNodeNumber(Integer.parseInt(cdGrabType3.getGrabCount()));
        xwType3Dao.insert(type3);
    }

    @Override
    @Transactional
    public void updateCDSharePercent(String contractId, String sharePercent){
        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts == null){
            return;
        }

        // 不需要同步更新
//        List<ZNodeTargetPercentInfo> nodes = zNodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("lq_code", contracts.getChainCode()).eq("node_code", contracts.getOrgCode()));
//        if(nodes != null && !nodes.isEmpty()){
//            for(ZNodeTargetPercentInfo node : nodes){
//                node.setSharePercent(sharePercent);
//                zNodeTargetPercentInfoDao.updateById(node);
//            }
//        }

        // 重新计算分享筹并更新复核状态
        CDGrabInfoSaveRequestDto dto = new CDGrabInfoSaveRequestDto();
        dto.setContractId(contracts.getParentId());
        dto.setSharePercent(sharePercent);
        BigDecimal shareSpace = incrementService.incrementMoneyShareModify(dto);
        if(shareSpace != null){
            contracts.setShareSpace(shareSpace);
        }
        contracts.setSharePercent(sharePercent);
        contracts.setIsChecked("1");
        contractsDao.updateById(contracts);

        // 更新主合约复核状态
        this.updateMasterContracts(contracts);

    }

    @Override
    public void updateCDShareSpace(String contractId){
        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts == null){
            return;
        }

        List<ZContracts> list = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id", contractId).eq("contract_type","30").in("status","1","8"));

        // 不需要同步更新
//        List<ZNodeTargetPercentInfo> nodes = zNodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("lq_code", contracts.getChainCode()).eq("node_code", contracts.getOrgCode()));
//        if(nodes != null && !nodes.isEmpty()){
//            for(ZNodeTargetPercentInfo node : nodes){
//                node.setSharePercent(sharePercent);
//                zNodeTargetPercentInfoDao.updateById(node);
//            }
//        }

        // 重新计算分享筹并更新复核状态
        for(ZContracts c : list){
            CDGrabInfoSaveRequestDto dto = new CDGrabInfoSaveRequestDto();
            dto.setContractId(c.getParentId());
            dto.setSharePercent(c.getSharePercent());
            BigDecimal shareSpace = incrementService.incrementMoneyShareModify(dto);
            if(shareSpace != null){
                c.setShareSpace(shareSpace);
            }
        }
        contractsService.updateBatchById(list);

    }

    /**
     * 更新主合约分享比例
     * @param contracts
     */
    public void updateMasterContracts(ZContracts contracts){
        if(contracts == null ||  contracts.getParentId() ==0){
            return;
        }
        ZContracts parentContract = contractsDao.selectById(contracts.getParentId());
        if(parentContract == null || "1".equals(parentContract.getIsChecked())){
            return;
        }
        parentContract.setIsChecked("1");
        contractsDao.updateById(parentContract);

        if(parentContract.getParentId() == 0){
            return;
        }

        parentContract = contractsDao.selectById(parentContract.getParentId());
        if(parentContract == null || "1".equals(parentContract.getIsChecked())){
            return;
        }
        parentContract.setIsChecked("1");
        contractsDao.updateById(parentContract);
    }

    @Override
    public void updateSharePercentChainMaster(String contractId){
        // 判断链群主是否复核过
        ZContracts contracts = contractsDao.selectById(contractId);
        // 如果已经复核过则不再重新计算
        if(contracts == null || "1".equals(contracts.getIsChecked())){
            return;
        }

        // 重新计算的逻辑
        // 1、查询子合约列表
        List<ZContracts> subContracts =  contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id", contractId).eq("contract_type", "10"));
        if(subContracts != null && !subContracts.isEmpty()){
            // 有子合约
            for(ZContracts zc : subContracts){
                // 1、按资源类型分组
                List<CDGrabType3> type3List =  queryCDGrabDataXWType3(String.valueOf(zc.getId()), "");
                // 2、循环获取资源类型下面的抢单列表
                if(type3List != null && !type3List.isEmpty()) {
                    for (CDGrabType3 item : type3List) {
                        BigDecimal percent = BigDecimal.ZERO;
                        List<ZContracts> list = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id", String.valueOf(zc.getId()))
                                .in("status", "1", "8").eq("contract_type", 30).like("org_type", "|" + item.getXwType3Code() + "|"));
                        if(list == null || list.isEmpty()){
                            continue;
                        }
                        percent = new BigDecimal(item.getSharePercent()).divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
                        for(ZContracts c : list){
                            c.setSharePercent(percent.toString());
                            c.setIsChecked("1");
                            CDGrabInfoSaveRequestDto dto = new CDGrabInfoSaveRequestDto();
                            dto.setContractId(c.getParentId());
                            dto.setSharePercent(percent.toString());
                            BigDecimal shareSpace = incrementService.incrementMoneyShareModify(dto);
                            if(shareSpace != null){
                                c.setShareSpace(shareSpace);
                            }
                        }
                        contractsService.updateBatchById(list);
                    }
                }
                // 3、更新主合约复核状态
                ZContracts c = new ZContracts();
                c.setId(zc.getId());
                c.setIsChecked("1");
                contractsDao.updateById(c);
            }
        }else {
            // 没有子合约
            // 1、按资源类型分组
            List<CDGrabType3> type3List =  queryCDGrabDataXWType3(contractId, "");
            // 2、循环获取资源类型下面的抢单列表
            if(type3List != null && !type3List.isEmpty()) {
                for (CDGrabType3 item : type3List) {
                    BigDecimal percent = BigDecimal.ZERO;
                    List<ZContracts> list = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id", contractId)
                            .in("status", "1","8").eq("contract_type", 30).like("org_type", "|" + item.getXwType3Code() + "|"));
                    if(list == null || list.isEmpty()){
                        continue;
                    }
                    percent = new BigDecimal(item.getSharePercent()).divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);
                    for(ZContracts c : list){
                        c.setSharePercent(percent.toString());
                        c.setIsChecked("1");
                        CDGrabInfoSaveRequestDto dto = new CDGrabInfoSaveRequestDto();
                        dto.setContractId(c.getParentId());
                        dto.setSharePercent(percent.toString());
                        BigDecimal shareSpace = incrementService.incrementMoneyShareModify(dto);
                        if(shareSpace != null){
                            c.setShareSpace(shareSpace);
                        }
                    }
                    contractsService.updateBatchById(list);
                }
            }

        }

        // 更新主合约复核状态
        ZContracts c = new ZContracts();
        c.setId(Integer.parseInt(contractId));
        c.setIsChecked("1");
        contractsDao.updateById(c);
    }

    /**
     * 检索需要更新分享比例的举单合约列表
     * @return
     */
    @Override
    public List<ZContracts> getContractForUpdateSPercent(){
        List<ZContracts> list = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("contract_type", 10).eq("parent_id", "0")
                .eq("is_checked", "0").lt("check_time", new Date()));
        if(list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public String getContractSize(String contractId) {
        Integer fact = contractsDao.selectCount(new QueryWrapper<ZContracts>()
                .eq("parent_id" , contractId)
                .eq("contract_type" , "30")
                .in("status", "1", "8"));
        Integer target = contractsDao.getContractSize(contractId);
        return fact + "/" + target;
    }

    @Override
    public List<ContractProductDTO> staticSerial(Integer contractId) {
        //计划数据
        List<ContractProductDTO> list=new ArrayList<>();

        List<ZContractsProduct> contractsProducts=contractsProductDao.selectList(
                new QueryWrapper<ZContractsProduct>().eq("contract_id",contractId));
        List<ZContractsProduct> serials=contractsProductDao.distinctSerialAndScene(contractId);

        if(serials!=null&&serials.size()>0) {
            ZContracts contracts = contractsDao.selectById(contractId);
            if (contracts == null) {
                throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
            }
            int year = DateFormatUtil.getYearOfDate(contracts.getStartDate());
            int mounth = DateFormatUtil.getMonthOfDate(contracts.getStartDate());
            ProductQueryEntity queryEntity = new ProductQueryEntity();
            queryEntity.setContractId(contractId);
            queryEntity.setQtyYear(year);
            //年度数据
            List<ZContractsProduct> yearSales = contractsProductDao.calContractProduct(queryEntity);

            //月度数据
            queryEntity.setQtyMonth(mounth);
            List<ZContractsProduct> monthSales = contractsProductDao.calContractProduct(queryEntity);

            for (ZContractsProduct serial:serials) {
                ZContractsProduct yearSale = yearSales.stream()
                        .filter(m -> serial.getProductSeries().equals(m.getProductSeries())
                        &&serial.getSceneName().equals(m.getSceneName()))
                        .findAny().orElse(null);
                ZContractsProduct monthSale = monthSales.stream()
                        .filter(m -> serial.getProductSeries().equals(m.getProductSeries())
                                &&serial.getSceneName().equals(m.getSceneName()))
                        .findAny().orElse(null);

                ContractProductDTO contractProductDTO = new ContractProductDTO();
                contractProductDTO.setProductSeries(serial.getProductSeries());
                contractProductDTO.setSceneName(serial.getSceneName());
                List<ZContractsProduct> currentSerials = contractsProducts.stream()
                        .filter(m -> serial.getProductSeries().equals(m.getProductSeries())
                        && serial.getSceneName().equals(m.getSceneName())).collect(Collectors.toList());
                List<ProductTargetDTO> targetDTOs = new ArrayList<>();
                for (ZContractsProduct serialPlan : currentSerials) {
                    ProductTargetDTO targetDTO = new ProductTargetDTO();
                    BeanUtils.copyProperties(serialPlan, targetDTO);
                    if (serialPlan.getTargetCode().equals("B00001")) {
                        //收入
                        targetDTO.setActualYear(yearSale == null ? BigDecimal.ZERO : yearSale.getQtyMonth().divide(
                                new BigDecimal("10000"),2, RoundingMode.HALF_UP
                        ));
                        targetDTO.setActualMonth(monthSale == null ? BigDecimal.ZERO : monthSale.getQtyMonth().divide(
                                new BigDecimal("10000"),2, RoundingMode.HALF_UP
                        ));
                    } else if (serialPlan.getTargetCode().equals("B00002")) {
                        targetDTO.setActualYear(yearSale == null ? BigDecimal.ZERO : yearSale.getQtyYear());
                        targetDTO.setActualMonth(monthSale == null ? BigDecimal.ZERO : monthSale.getQtyYear());
                    }
                    targetDTOs.add(targetDTO);
                }

                contractProductDTO.setTargetList(targetDTOs);
                list.add(contractProductDTO);

            }
        }
        return  list;
    }
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private MonthChainGroupOrderDao monthChainGroupOrderDao;

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
    @Autowired
    private ZReservePlanDetailDao planDetailDao;

    @Override
    public void exportContract(Integer contractId) {

        try {
            Workbook workbook = new HSSFWorkbook();
            //市场
            List<ContractExportEntity> tyExports=monthChainGroupOrderDao.tyExport(contractId);
            List<ZContractsFactor> factors=factorDao.getFactorByContractId(contractId);
            List<ExcelUtil.CellHeadField> headFields = new ArrayList<>();
            headFields.addAll(Arrays.asList(Ty_Export_Header));

            //获取动态列
            List<ZContractsFactor> anyone=factors.stream().filter(m->tyExports.get(0).getId().equals(
                    m.getContractId().toString()
            )).collect(Collectors.toList());
            for(ZContractsFactor fac:anyone){
                headFields.add(new ExcelUtil.CellHeadField(
                        Constant.FactorType.Bottom.getValue().equals(fac.getFactorType()) ?
                                "举单"+fac.getFactorName():"抢单"+fac.getFactorName(),fac.getFactorType()+fac.getFactorCode()) );
            }
            ExcelUtil.CellHeadField[] constHeader= (ExcelUtil.CellHeadField[]) headFields.toArray(
                    new ExcelUtil.CellHeadField[headFields.size()]
            );

            //动态设置值
            List<Map<String,Object>> tyExportData=new ArrayList<>();
            for(ContractExportEntity entity:tyExports){
                Map<String,Object> map= JSONObject.parseObject(JSON.toJSONString(entity));
                List<ZContractsFactor> curr=factors.stream().filter(m->entity.getId().equals(
                        m.getContractId().toString()
                )).collect(Collectors.toList());
                for(ZContractsFactor fac:curr){
                    map.put(fac.getFactorType()+fac.getFactorCode(),fac.getFactorValue());
                }
                tyExportData.add(map);
            }
            //获取动态
            ExcelUtil.buildSheet(workbook, "体验抢单数据", tyExportData, constHeader);

            //创单
            List<ContractExportEntity> cdExports=monthChainGroupOrderDao.cdExport(contractId);
            List<ZReservePlanDetail> list=planDetailDao.getByContract(contractId);
            for (ContractExportEntity curr:cdExports
                 ) {
                List<ZReservePlanDetail> currPlan=list.stream().filter(
                        m->curr.getId().equals(m.getParentId().toString())
                ).collect(Collectors.toList());
                String content="";
                if(currPlan!=null&&currPlan.size()>0){
                    int index=1;
                    for (ZReservePlanDetail plan: currPlan
                         ) {
                        content+="预案"+index+"  标题："+plan.getCost()+"， 内容："+plan.getContent()+"。 ";
                        index++;
                    }
                }
                curr.setContent(content);

            }
            ExcelUtil.buildSheet(workbook, "创单抢单数据", cdExports, Cd_Export_Header);

            //爆款
            List<ContractProductDTO> products = this.staticSerial(contractId);
            List<ProductTargetDTO> details=new ArrayList<>();
            for (ContractProductDTO dto:products
                 ) {
                details.addAll(dto.getTargetList());
            }
            ExcelUtil.buildSheet(workbook, "爆款数据", details, Serial_Header);
            ByteArrayOutputStream bot = new ByteArrayOutputStream();
            workbook.write(bot);
            ExcelUtil.export(request, response, workbook, "合约抢单信息.xls");
        }catch (Exception ex){

        }
    }

    @Override
    public Integer getContractSize2(String contractId) {
        Integer target = contractsDao.getContractSize2(contractId);
        return target;
    }
}
