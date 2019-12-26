package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 19033323
 */
@Service
public class CDGrabServiceImpl implements CDGrabService {
    @Autowired
    ZSharePercentDao sharePercentDao;
    @Autowired
    TargetBasicDao targetBasicDao;
    @Autowired
    ZReservePlanDao reservePlanDao;
    @Autowired
    ZReservePlanDetailDao reservePlanDetailDao;
    @Autowired
    ZContractsDao contractsDao;
    @Autowired
    ZContractsFactorDao factorDao;
    @Autowired
    ZHrChainInfoDao chainInfoDao;
    @Autowired
    ZNodeTargetPercentInfoDao targetPercentInfoDao;
    @Autowired
    SysXwRegionDao sysXwRegionDao;
    @Autowired
    ZHrChainInfoDao zHrChainInfoDao;

    @Override
    public CDGrabInfoResponseDto queryCDGrabInfo(CDGrabInfoRequestDto requestDto){
        CDGrabInfoResponseDto responseDto = new CDGrabInfoResponseDto();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        String xwCode = currentUser.getXwCode();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_TIME_PATTERN));
            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_TIME_PATTERN));
            responseDto.setChainName(contracts.getContractName());
//            List<ZHrChainInfo> chainInfos = chainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
//            if(chainInfos != null && !chainInfos.isEmpty()){
//                responseDto.setChainName(chainInfos.get(0).getChainName());
//            }
        }

        /*// 分享比例查询
        List<ZSharePercent> resultList = sharePercentDao.selectList(new QueryWrapper<ZSharePercent>().eq("xw_code", xwCode).eq("period_code", requestDto.getYearMonth()));
        if(resultList!=null && !resultList.isEmpty()){
            responseDto.setSharePercent(resultList.get(0).getPercent());
        }*/

        List<String> chainCodeList = new ArrayList<>();
        List<ZHrChainInfo> chainList = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("xw_code", xwCode));
        if(chainList != null && !chainList.isEmpty()){
            for(ZHrChainInfo chainInfo : chainList){
                chainCodeList.add(chainInfo.getChainCode());
            }
        }

        List<String> yearMonthList = getYearMonth(String.valueOf(requestDto.getContractId()));
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("nodeCode", currentUser.getOrgNum());
        paraMap.put("ptCode", currentUser.getPtcode());
        paraMap.put("yearMonthList", yearMonthList);
        paraMap.put("chainCode", contracts.getChainCode());
        List<CDGrabTargetEntity> targetList = targetPercentInfoDao.queryCDGrabTarget(paraMap);

        // 目标底线查询
//        List<TargetBasic> targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("target_pt_code", currentUser.getPtcode()).like("role_code", xwCode)
//                /*.eq("targe_year", requestDto.getYear()).eq("target_month", requestDto.getMonth())*/.in("chain_code", chainCodeList));
        if(targetList != null && !targetList.isEmpty()){
            for (CDGrabTargetEntity targetInfo : targetList){
                CDGrabTargetDto target = new CDGrabTargetDto();
                target.setTargetName(targetInfo.getTargetName());
                target.setTargetCode(targetInfo.getTargetCode());
                target.setChainGoal(new BigDecimal(targetInfo.getTargetBottomLine()));
                target.setTargetUnit(targetInfo.getTargetUnit());
                responseDto.getTargetList().add(target);
                responseDto.setSharePercent(targetInfo.getSharePercent());
            }
        }

        // 分享比例查询
//        List<ZNodeTargetPercentInfo> targetPercentInfos = targetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("xw_code", xwCode)
//                                                           .in("lq_code", chainCodeList));
//        if(targetPercentInfos != null && !targetPercentInfos.isEmpty()){
//            responseDto.setSharePercent(targetPercentInfos.get(0).getSharePercent());
//        }


        return responseDto;
    }

    @Override
    public CDGrabViewResponseDto queryCDGrabView(CDGrabInfoRequestDto requestDto){
        CDGrabViewResponseDto responseDto = new CDGrabViewResponseDto();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        String xwCode = currentUser.getXwCode();
        String ptCode = currentUser.getPtcode();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
            responseDto.setSharePercent(contracts.getSharePercent());
            responseDto.setTargetShareMoney(contracts.getShareSpace().toString());
            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_TIME_PATTERN));
            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_TIME_PATTERN));
            responseDto.setChainName(contracts.getContractName());
//            List<ZHrChainInfo> chainInfos = chainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
//            if(chainInfos != null && !chainInfos.isEmpty()){
//                responseDto.setChainName(chainInfos.get(0).getChainName());
//            }

            List<ZContractsFactor> factorList = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId()));
            if(factorList != null && !factorList.isEmpty()){
                Map<String, List<ZContractsFactor>> tempMap = new HashMap<>();
                for(ZContractsFactor factor : factorList){
                    List<ZContractsFactor> list = tempMap.get(factor.getFactorCode());
                    if(list == null || list.isEmpty()){
                        list = new ArrayList<>();
                        tempMap.put(factor.getFactorCode(), list);
                    }
                    list.add(factor);
                }

                for(Map.Entry<String, List<ZContractsFactor>> entity : tempMap.entrySet()){
                    List<ZContractsFactor> factors = entity.getValue();
                    CDGrabTargetDto target = new CDGrabTargetDto();
                    target.setTargetName(factors.get(0).getFactorName());
                    target.setTargetCode(factors.get(0).getFactorCode());
                    target.setTargetUnit(factors.get(0).getFactorUnit());
                    for(ZContractsFactor factor : factors){
                        if(Constant.FactorType.Bottom.getValue().equals(factor.getFactorType())){
                            target.setChainGoal(new BigDecimal(factor.getFactorValue()));
                        }else if(Constant.FactorType.Grab.getValue().equals(factor.getFactorType())){
                            target.setChainGrabGoal(new BigDecimal(factor.getFactorValue()));
                        }
                    }
                    responseDto.getTargetList().add(target);
                }
            }

            List<PlanInfoDto> planInfoList = reservePlanDao.selectPlanInfo(String.valueOf(contracts.getId()));
            responseDto.setPlanList(planInfoList);
        }

        return responseDto;
    }

    @Override
    @Transactional
    public void saveCDGrab(CDGrabInfoSaveRequestDto requestDto){
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();

        ZContracts contracts = new ZContracts();
        contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }

        //根据小微code 和合约判断是否已抢单
        List<ZContracts> contractList=contractsDao.selectList(new QueryWrapper<ZContracts>()
                .eq("parent_id",requestDto.getContractId())
                .eq("create_code",currentUser.getEmpsn())
                .eq("contract_type", "30"));
        if(contractList!=null){
            throw new RException("用户已抢单");
        }

        String regionCode = "";
        List<SysXwRegion> xwRegion=sysXwRegionDao.selectList(new QueryWrapper<SysXwRegion>()
                .eq("xw_code", currentUser.getXwCode()));
        if(xwRegion!=null && !xwRegion.isEmpty()){
            regionCode = xwRegion.get(0).getRegionCode();
        }

        contracts.setId(null);
        contracts.setParentId(requestDto.getContractId());
        contracts.setJoinTime(new Date());
        contracts.setStatus("1");
        contracts.setShareSpace(new BigDecimal(requestDto.getTargetShareMoney()));
        contracts.setSharePercent(requestDto.getSharePercent());
        contracts.setContractType("30"); //创客合约
        contracts.setCreateCode(currentUser.getEmpsn());
        contracts.setCreateName(currentUser.getEmpname());
        contracts.setOrgCode(currentUser.getOrgNum());
        contracts.setOrgName(currentUser.getOrgName());
        contracts.setOrgType(currentUser.getOrgType());
        contracts.setCreateTime(new Date());
        contracts.setXiaoweiCode(currentUser.getXwCode());
        contracts.setRegionCode(regionCode);

        contractsDao.insert(contracts);
        Integer contractsId = contracts.getId();

        for(CDGrabTargetDto targetDto : requestDto.getTargetList()){
            // 链群目标保存
            ZContractsFactor contractsFactor = new ZContractsFactor();
            contractsFactor.setContractId(contractsId);
            contractsFactor.setFactorCode(targetDto.getTargetCode());
            contractsFactor.setFactorName(targetDto.getTargetName());
            contractsFactor.setFactorValue(targetDto.getChainGoal().toString());
            contractsFactor.setFactorType(Constant.FactorType.Bottom.getValue());
            contractsFactor.setFactorUnit(targetDto.getTargetUnit());
            factorDao.insert(contractsFactor);

            // 抢单目标保存
            ZContractsFactor contractsFactor2 = new ZContractsFactor();
            contractsFactor2.setContractId(contractsId);
            contractsFactor2.setFactorCode(targetDto.getTargetCode());
            contractsFactor2.setFactorName(targetDto.getTargetName());
            contractsFactor2.setFactorValue(targetDto.getChainGrabGoal().toString());
            contractsFactor2.setFactorType(Constant.FactorType.Grab.getValue());
            contractsFactor2.setFactorUnit(targetDto.getTargetUnit());
            factorDao.insert(contractsFactor2);
        }

        // 保存预案信息
        ZReservePlan plan = new ZReservePlan();
        plan.setParentId(contractsId);
        plan.setTitle(requestDto.getPlanTitle());
        reservePlanDao.insert(plan);
        Integer planId = plan.getId();

        ZReservePlanDetail planDetail = new ZReservePlanDetail();
        planDetail.setParentId(planId);
        planDetail.setContent(requestDto.getPlanContent());
        reservePlanDetailDao.insert(planDetail);
    }

    private List<String> getYearMonth(String contractId){
        String currentYear = String.valueOf(DateFormatUtil.getYearOfDate(new Date()));
        ZContracts contracts=contractsDao.selectById(contractId);
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }
        int mounth = DateFormatUtil.getMonthOfDate(contracts.getStartDate());
        int endMounth=DateFormatUtil.getMonthOfDate(contracts.getEndDate());
        List<String> yearMounths=new ArrayList<>();

        for(int start=mounth;start<=endMounth;start++){
            String strMounth = start < 10 ? "0" + start : String.valueOf(start);
            yearMounths.add(currentYear+strMounth);
        }
        return yearMounths;
    }
}
