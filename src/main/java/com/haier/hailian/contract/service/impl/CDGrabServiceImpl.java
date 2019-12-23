package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.grab.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Override
    public CDGrabInfoResponseDto queryCDGrabInfo(CDGrabInfoRequestDto requestDto){
        CDGrabInfoResponseDto responseDto = new CDGrabInfoResponseDto();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        String xwCode = currentUser.getXwCode();
        String ptCode = currentUser.getPtcode();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_TIME_PATTERN));
            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_TIME_PATTERN));
            List<ZHrChainInfo> chainInfos = chainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
            if(chainInfos != null && !chainInfos.isEmpty()){
                responseDto.setChainName(chainInfos.get(0).getChainName());
            }
        }

        /*// 分享比例查询
        List<ZSharePercent> resultList = sharePercentDao.selectList(new QueryWrapper<ZSharePercent>().eq("xw_code", xwCode).eq("period_code", requestDto.getYearMonth()));
        if(resultList!=null && !resultList.isEmpty()){
            responseDto.setSharePercent(resultList.get(0).getPercent());
        }

        // 目标底线查询
        List<TargetBasic> targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("target_pt_code", ptCode).like("role_code", xwCode)
                .eq("targe_year", requestDto.getYear()).eq("target_month", requestDto.getMonth()));
        if(targetList != null && !targetList.isEmpty()){
            responseDto.setTargetShareMoney(targetList.get(0).getTargetBottomLine());
        }*/

        List<ZNodeTargetPercentInfo> targetPercentInfos = targetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("xw_code", xwCode));
        if(targetPercentInfos != null && !targetPercentInfos.isEmpty()){
            responseDto.setSharePercent(targetPercentInfos.get(0).getSharePercent());
            responseDto.setTargetShareMoney(targetPercentInfos.get(0).getTargetLine());
        }


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
            List<ZHrChainInfo> chainInfos = chainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
            if(chainInfos != null && !chainInfos.isEmpty()){
                responseDto.setChainName(chainInfos.get(0).getChainName());
            }

            List<ZContractsFactor> factorList = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId()).eq("factor_code", Constant.FactorCode.Incom.getValue()));
            if(factorList != null && !factorList.isEmpty()){
                for(ZContractsFactor factor : factorList){
                    if(factor.getFactorType().equalsIgnoreCase(Constant.FactorType.Bottom.getValue())){
                        responseDto.setChainGoal(factor.getFactorValue());
                    }else if(factor.getFactorType().equalsIgnoreCase(Constant.FactorType.Grab.getValue())){
                        responseDto.setChainGrabGoal(factor.getFactorValue());
                    }
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
        contracts.setParentId(requestDto.getContractId());
        contracts.setJoinTime(new Date());
        contracts.setShareSpace(new BigDecimal(requestDto.getTargetShareMoney()));
        contracts.setSharePercent(requestDto.getSharePercent());
        contracts.setContractType("30"); //创客合约
        contracts.setCreateCode(currentUser.getEmpsn());
        contracts.setCreateName(currentUser.getEmpname());
        contracts.setCreateTime(new Date());
        contracts.setXiaoweiCode(currentUser.getXwCode());

        contractsDao.insert(contracts);
        Integer contractsId = contracts.getId();

        // 链群目标保存
        ZContractsFactor contractsFactor = new ZContractsFactor();
        contractsFactor.setContractId(contractsId);
        contractsFactor.setFactorCode(Constant.FactorCode.Incom.getValue());
        contractsFactor.setFactorName(Constant.FactorCode.Incom.getName());
        contractsFactor.setFactorValue(requestDto.getChainGoal());
        contractsFactor.setFactorType(Constant.FactorType.Bottom.getValue());
        contractsFactor.setFactorUnit("元");
        factorDao.insert(contractsFactor);

        // 抢单目标保存
        ZContractsFactor contractsFactor2 = new ZContractsFactor();
        contractsFactor2.setContractId(contractsId);
        contractsFactor2.setFactorCode(Constant.FactorCode.Incom.getValue());
        contractsFactor2.setFactorName(Constant.FactorCode.Incom.getName());
        contractsFactor2.setFactorValue(requestDto.getChainGrabGoal());
        contractsFactor2.setFactorType(Constant.FactorType.Grab.getValue());
        contractsFactor2.setFactorUnit("元");
        factorDao.insert(contractsFactor2);

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
}
