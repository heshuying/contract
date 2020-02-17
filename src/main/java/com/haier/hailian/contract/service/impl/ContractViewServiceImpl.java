package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ContractViewService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import com.haier.hailian.contract.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
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
    @Autowired
    ZContractsProductDao contractsProductDao;
    private static final ExcelUtil.CellHeadField[] Serial_Header = {
            new ExcelUtil.CellHeadField("系列", "serial"),
            new ExcelUtil.CellHeadField("年计划", "yearPlan"),
            new ExcelUtil.CellHeadField("年累", "yearSales"),
            new ExcelUtil.CellHeadField("月计划", "monthPlan"),
            new ExcelUtil.CellHeadField("月累", "monthSales")

    };
    private static final ExcelUtil.CellHeadField[] Ty_Export_Header = {
            new ExcelUtil.CellHeadField("链群编码", "chainCode"),
            new ExcelUtil.CellHeadField("链群名称", "chainName"),
            new ExcelUtil.CellHeadField("合约编码", "parentId"),
            new ExcelUtil.CellHeadField("合约开始时间", "startDate"),
            new ExcelUtil.CellHeadField("合约结束时间", "endDate"),
            new ExcelUtil.CellHeadField("抢单编码", "id"),
            new ExcelUtil.CellHeadField("抢单组织编码", "orgCode"),
            new ExcelUtil.CellHeadField("抢单组织名称", "orgName"),
            new ExcelUtil.CellHeadField("抢单人工号", "createCode"),
            new ExcelUtil.CellHeadField("抢单人", "createName"),
            new ExcelUtil.CellHeadField("底线收入", "bottomInc"),
            new ExcelUtil.CellHeadField("底线高端占比", "bottomHigh"),
            new ExcelUtil.CellHeadField("抢单收入", "grabInc"),
            new ExcelUtil.CellHeadField("抢单高端占比", "grabHigh"),

    };
    private static final ExcelUtil.CellHeadField[] Cd_Export_Header = {
            new ExcelUtil.CellHeadField("链群编码", "chainCode"),
            new ExcelUtil.CellHeadField("链群名称", "chainName"),
            new ExcelUtil.CellHeadField("合约编码", "parentId"),
            new ExcelUtil.CellHeadField("合约开始时间", "startDate"),
            new ExcelUtil.CellHeadField("合约结束时间", "endDate"),
            new ExcelUtil.CellHeadField("抢单编码", "id"),
            new ExcelUtil.CellHeadField("抢单组织编码", "orgCode"),
            new ExcelUtil.CellHeadField("抢单组织名称", "orgName"),
            new ExcelUtil.CellHeadField("抢单人工号", "createCode"),
            new ExcelUtil.CellHeadField("抢单人", "createName"),
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
            if("incomeDesc".equals(requestBean.getOrderStr())){
                paraMap.put("factorCode", Constant.FactorCode.Incom.getValue());
            }else if("highDesc".equals(requestBean.getOrderStr())){
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
    public List<ContractSerialDto> staticSerial(Integer contractId) {
        //计划数据
        List<ZContractsProduct> products=contractsProductDao.selectList(
                new QueryWrapper<ZContractsProduct>()
                .eq("contract_id",contractId)
        );
        List<ContractSerialDto> list=new ArrayList<>();
        if(products!=null&&products.size()>0) {
            ZContracts contracts = contractsDao.selectById(contractId);
            if (contracts == null) {
                throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
            }
            int year = DateFormatUtil.getYearOfDate(contracts.getStartDate());
            int mounth = DateFormatUtil.getMonthOfDate(contracts.getStartDate());
            ZContractsProduct queryEntity = new ZContractsProduct();
            queryEntity.setContractId(contractId);
            queryEntity.setQtyYear(year);
            //年度数据
            List<ZContractsProduct> yearSales = contractsProductDao.calContractProduct(queryEntity);

            //月度数据
            queryEntity.setQtyMonth(mounth);
            List<ZContractsProduct> monthSales = contractsProductDao.calContractProduct(queryEntity);

            for (ZContractsProduct zcp:products) {

                ContractSerialDto dto=new ContractSerialDto();
                dto.setSerial(zcp.getProductSeries());
                dto.setYearPlan(zcp.getQtyYear());
                dto.setMonthPlan(zcp.getQtyMonth());
                ZContractsProduct yearSale=yearSales.stream()
                        .filter(m->zcp.getProductSeries().equals(m.getProductSeries()))
                        .findAny().orElse(null);
                ZContractsProduct monthSale=monthSales.stream()
                        .filter(m->zcp.getProductSeries().equals(m.getProductSeries()))
                        .findAny().orElse(null);
                dto.setYearSales(yearSale==null?0:yearSale.getQtyYear());
                dto.setMonthSales(monthSale==null?0:monthSale.getQtyYear());
                list.add(dto);
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
            ExcelUtil.buildSheet(workbook, "体验抢单数据", tyExports, Ty_Export_Header);

            //创单
            List<ContractExportEntity> cdExports=monthChainGroupOrderDao.cdExport(contractId);
            List<ZReservePlanDetail> list=planDetailDao.getByContract(contractId);
            for (ContractExportEntity curr:cdExports
                 ) {
                List<ZReservePlanDetail> currPlan=list.stream().filter(
                        m->curr.getId().equals(
                        m.getParentId())
                ).collect(Collectors.toList());
                String content="";
                if(currPlan!=null&&currPlan.size()>0){

                    for (ZReservePlanDetail plan: currPlan
                         ) {
                        content+=plan.getContent();
                    }
                }
                curr.setContent("");

            }
            ExcelUtil.buildSheet(workbook, "创单抢单数据", cdExports, Cd_Export_Header);

            //爆款
            List<ContractSerialDto> product = this.staticSerial(contractId);
            ExcelUtil.buildSheet(workbook, "爆款数据", product, Serial_Header);
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
