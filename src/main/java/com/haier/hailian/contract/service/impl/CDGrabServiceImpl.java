package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.CDGrabService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    ZGrabContractsDao grabContractsDao;

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
        ZGrabContracts grabContracts = new ZGrabContracts();
        grabContracts.setParentId(requestDto.getContractId());
        grabContracts.setShareRatio(requestDto.getSharePercent());
        grabContractsDao.insert(grabContracts);
        Integer grabId = grabContracts.getId();

        ZReservePlan plan = new ZReservePlan();
        plan.setParentId(grabId);
        plan.setTitle(requestDto.getPlanTitle());
        reservePlanDao.insert(plan);
        Integer planId = plan.getId();

        ZReservePlanDetail planDetail = new ZReservePlanDetail();
        planDetail.setParentId(planId);
        planDetail.setContent(requestDto.getPlanContent());
        reservePlanDetailDao.insert(planDetail);
    }
}
