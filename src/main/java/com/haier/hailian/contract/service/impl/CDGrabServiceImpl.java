package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

        // 分享比例查询
        List<ZSharePercent> resultList = sharePercentDao.selectList(new QueryWrapper<ZSharePercent>().eq("xw_code", xwCode).eq("period_code", requestDto.getYearMonth()));
        if(resultList!=null && !resultList.isEmpty()){
            responseDto.setSharePercent(resultList.get(0).getPercent());
        }

        // 目标底线查询
        List<TargetBasic> targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("target_pt_code", ptCode).like("role_code", xwCode)
                .eq("targe_year", requestDto.getYear()).eq("target_month", requestDto.getMonth()));
        if(targetList != null && !targetList.isEmpty()){
            responseDto.setTargetShareMoney(targetList.get(0).getTargetBottomLine());
        }

        // 达成目标预计分享酬 todo

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
        contractsFactor.setFactorCode(Constant.FactorCode.Incom.getName());
        contractsFactor.setFactorName(Constant.FactorCode.Incom.getValue());
        contractsFactor.setFactorValue(requestDto.getChainGoal());
        contractsFactor.setFactorType(Constant.FactorType.Bottom.getValue());
        contractsFactor.setFactorUnit("元");
        factorDao.insert(contractsFactor);

        // 抢单目标保存
        ZContractsFactor contractsFactor2 = new ZContractsFactor();
        contractsFactor2.setContractId(contractsId);
        contractsFactor2.setFactorCode(Constant.FactorCode.Incom.getName());
        contractsFactor2.setFactorName(Constant.FactorCode.Incom.getValue());
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
