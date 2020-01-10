package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.dto.grab.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.CDGrabService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import com.haier.hailian.contract.util.IHaierUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
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
        TOdsMinbu currentUser = sysUser.getMinbu();
        String xwCode = currentUser.getXwCode();
        String littleXWCode = sysUser.getMinbu().getLittleXwCode();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_PATTERN));
            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_PATTERN));
            responseDto.setChainName(contracts.getContractName());

            List<ZHrChainInfo> chainInfos = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
            if(chainInfos != null && !chainInfos.isEmpty()){
                responseDto.setMasterCode(chainInfos.get(0).getMasterCode());
                responseDto.setMasterName(chainInfos.get(0).getMasterName());
                responseDto.setXwCode(chainInfos.get(0).getXwCode());
                responseDto.setXwName(chainInfos.get(0).getXwName());
            }

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

//        List<String> chainCodeList = new ArrayList<>();
//        List<ZHrChainInfo> chainList = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("xw_code", xwCode));
//        if(chainList != null && !chainList.isEmpty()){
//            for(ZHrChainInfo chainInfo : chainList){
//                chainCodeList.add(chainInfo.getChainCode());
//            }
//        }

        List<String> yearMonthList = getYearMonth(String.valueOf(requestDto.getContractId()));
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("nodeCode", littleXWCode);
        paraMap.put("ptCode", currentUser.getPtCode());
        paraMap.put("yearMonthList", yearMonthList);
        paraMap.put("chainCode", contracts.getChainCode());
        List<CDGrabTargetEntity> targetList = targetPercentInfoDao.queryCDGrabTargetNew(paraMap);

        // 目标底线查询
//        List<TargetBasic> targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("target_pt_code", currentUser.getPtcode()).like("role_code", xwCode)
//                /*.eq("targe_year", requestDto.getYear()).eq("target_month", requestDto.getMonth())*/.in("chain_code", chainCodeList));
        if(targetList != null && !targetList.isEmpty()){
            for (CDGrabTargetEntity targetInfo : targetList){
                CDGrabTargetDto target = new CDGrabTargetDto();
                target.setTargetName(targetInfo.getTargetName());
                target.setTargetCode(targetInfo.getTargetCode());
//                target.setChainGoal(new BigDecimal(targetInfo.getTargetBottomLine()));
                target.setTargetUnit(targetInfo.getTargetUnit());
//                target.setTargetTo(targetInfo.getTargetTo());
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
        TOdsMinbu currentUser = sysUser.getMinbu();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
            responseDto.setSharePercent(contracts.getSharePercent());
            responseDto.setTargetShareMoney(contracts.getShareSpace().toString());
            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_PATTERN));
            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_PATTERN));
            responseDto.setChainName(contracts.getContractName());

            List<ZHrChainInfo> chainInfos = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
            if(chainInfos != null && !chainInfos.isEmpty()){
                responseDto.setMasterCode(chainInfos.get(0).getMasterCode());
                responseDto.setMasterName(chainInfos.get(0).getMasterName());
                responseDto.setXwCode(chainInfos.get(0).getXwCode());
                responseDto.setXwName(chainInfos.get(0).getXwName());
            }
//            List<ZHrChainInfo> chainInfos = chainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
//            if(chainInfos != null && !chainInfos.isEmpty()){
//                responseDto.setChainName(chainInfos.get(0).getChainName());
//            }

            List<ZContractsFactor> factorList = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId()));
            if(factorList != null && !factorList.isEmpty()){
                Map<String, List<ZContractsFactor>> tempMap = new HashMap<>();
                for(ZContractsFactor factor : factorList){
                    if(StringUtils.isNotBlank(factor.getFactorValue())){
                        factor.setFactorValue(new BigDecimal(factor.getFactorValue()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    }
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
                    target.setTargetTo(factors.get(0).getFactorDirecton());
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

            List<PlanInfoDto> planInfoList = reservePlanDao.selectPlanInfoGroup(String.valueOf(contracts.getId()));
            if(planInfoList != null && !planInfoList.isEmpty()){
                for(PlanInfoDto planInfo : planInfoList){
                    ReservePlanResultDTO reservePlanDTO = new ReservePlanResultDTO();
                    BeanUtils.copyProperties(planInfo, reservePlanDTO);
                    reservePlanDTO.setCreateUserCode(planInfo.getCreateUserCode());
                    reservePlanDTO.setCreateUserName(planInfo.getCreateUserName());
                    reservePlanDTO.setStartTime(DateFormatUtil.format(planInfo.getStartTime(),DateFormatUtil.DATE_PATTERN));
                    reservePlanDTO.setEndTime(DateFormatUtil.format(planInfo.getEndTime(),DateFormatUtil.DATE_PATTERN));
                    reservePlanDTO.setExecuter(planInfo.getExecuter());
                    reservePlanDTO.setIsImportant(planInfo.getIsImportant());
                    reservePlanDTO.setRemindTime(planInfo.getRemindTime());
                    reservePlanDTO.setRemindType(planInfo.getRemindType());
                    List<PlanInfoDto> planInfoDetails = reservePlanDao.selectPlanInfoSub(String.valueOf(contracts.getId()), planInfo.getOrderType());
                    if(planInfoDetails != null){
                        for(PlanInfoDto planDetail : planInfoDetails){
                            ReservePlanDetailDTO reservePlanDetailDTO = new ReservePlanDetailDTO();
                            reservePlanDetailDTO.setTitle(planDetail.getTitle());
                            reservePlanDetailDTO.setContent(planDetail.getContent());
                            reservePlanDTO.getPlanDetail().add(reservePlanDetailDTO);
                        }
                    }
                    responseDto.getPlanList().add(reservePlanDTO);
                }
            }
        }

        return responseDto;
    }

    /**
     * 查询抢单历史记录
     * @param requestDto
     * @return
     */
    @Override
    public List<CDGrabHistoryResponseDto> queryCDGrabHistoryView(CDGrabInfoRequestDto requestDto){
        List<CDGrabHistoryResponseDto> resultList = new ArrayList<>();

        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts != null){
//            responseDto.setSharePercent(contracts.getSharePercent());
//            responseDto.setTargetShareMoney(contracts.getShareSpace().toString());
//            responseDto.setStartTime(DateFormatUtil.format(contracts.getStartDate(), DateFormatUtil.DATE_PATTERN));
//            responseDto.setEndTime(DateFormatUtil.format(contracts.getEndDate(), DateFormatUtil.DATE_PATTERN));
//            responseDto.setChainName(contracts.getContractName());
            List<ZContracts> contractList = contractsDao.selectList(
                    new QueryWrapper<ZContracts>().eq("parent_id", contracts.getParentId())
                            .eq("xiaowei_code", contracts.getXiaoweiCode())
                            .eq("create_code", contracts.getCreateCode())
                            .eq("org_code", contracts.getOrgCode())
                            .eq("contract_type", "30")
                            .ne("status", "5")
                            .ne("status", "3")
                            .orderByAsc("id"));
            if(contractList == null){
                return resultList;
            }
            for(ZContracts item : contractList){
                CDGrabHistoryResponseDto responseDto = new CDGrabHistoryResponseDto();
                responseDto.setGrabId(String.valueOf(item.getId()));
                responseDto.setStatus(item.getStatus());
                responseDto.setCreateTime(DateFormatUtil.format(item.getCreateTime(), DateFormatUtil.DATE_TIME_PATTERN));
                List<ZContractsFactor> factorList = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id", item.getId()));
                if(factorList != null && !factorList.isEmpty()){
                    Map<String, List<ZContractsFactor>> tempMap = new HashMap<>();
                    for(ZContractsFactor factor : factorList){
                        if(StringUtils.isNotBlank(factor.getFactorValue())){
                            factor.setFactorValue(new BigDecimal(factor.getFactorValue()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                        }
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
                        target.setTargetTo(factors.get(0).getFactorDirecton());
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

                List<PlanInfoDto> planInfoList = reservePlanDao.selectPlanInfoGroup(String.valueOf(item.getId()));
                if(planInfoList != null && !planInfoList.isEmpty()){
                    for(PlanInfoDto planInfo : planInfoList){
                        ReservePlanResultDTO reservePlanDTO = new ReservePlanResultDTO();
                        BeanUtils.copyProperties(planInfo, reservePlanDTO);
                        reservePlanDTO.setCreateUserCode(planInfo.getCreateUserCode());
                        reservePlanDTO.setCreateUserName(planInfo.getCreateUserName());
                        reservePlanDTO.setStartTime(DateFormatUtil.format(planInfo.getStartTime(),DateFormatUtil.DATE_PATTERN));
                        reservePlanDTO.setEndTime(DateFormatUtil.format(planInfo.getEndTime(),DateFormatUtil.DATE_PATTERN));
                        reservePlanDTO.setExecuter(planInfo.getExecuter());
                        reservePlanDTO.setIsImportant(planInfo.getIsImportant());
                        reservePlanDTO.setRemindTime(planInfo.getRemindTime());
                        reservePlanDTO.setRemindType(planInfo.getRemindType());
                        List<PlanInfoDto> planInfoDetails = reservePlanDao.selectPlanInfoSub(String.valueOf(item.getId()), planInfo.getOrderType());
                        if(planInfoDetails != null){
                            for(PlanInfoDto planDetail : planInfoDetails){
                                ReservePlanDetailDTO reservePlanDetailDTO = new ReservePlanDetailDTO();
                                reservePlanDetailDTO.setTitle(planDetail.getTitle());
                                reservePlanDetailDTO.setContent(planDetail.getContent());
                                reservePlanDTO.getPlanDetail().add(reservePlanDetailDTO);
                            }
                        }
                        responseDto.getPlanList().add(reservePlanDTO);
                    }
                }

                resultList.add(responseDto);
            }
        }

        return resultList;
    }

    @Override
    @Transactional
    public void saveCDGrab(CDGrabInfoSaveRequestDto requestDto){
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if(currentUser==null||StringUtils.isBlank(currentUser.getXwType5Code())||
                !currentUser.getXwType5Code().equals("1")){
            throw new RException(Constant.MSG_NO_MINBU,Constant.CODE_NO_MINBU);
        }
        ZContracts contracts = new ZContracts();
        contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }

        //根据小微code 和合约判断是否已抢单
        List<ZContracts> contractList=contractsDao.selectList(new QueryWrapper<ZContracts>()
                .eq("parent_id",requestDto.getContractId())
                .eq("create_code",sysUser.getEmpSn())
                .eq("contract_type", "30")
                .notIn("status", "3", "5", "6"));
        if(contractList!=null && contractList.size()>0){
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
        contracts.setStatus("1");
        contracts.setShareSpace(new BigDecimal(requestDto.getTargetShareMoney()));
        contracts.setSharePercent(requestDto.getSharePercent());
        contracts.setContractType("30"); //创客合约
        contracts.setCreateCode(sysUser.getEmpSn());
        contracts.setCreateName(sysUser.getEmpName());
        contracts.setCreateTime(new Date());
        contracts.setRegionCode(regionCode);

        contracts.setOrgCode(sysUser.getMinbu().getLittleXwCode());
        contracts.setOrgName(sysUser.getMinbu().getLittleXwName());
        contracts.setOrgType(sysUser.getMinbu().getXwType3Code());
        contracts.setXiaoweiCode(sysUser.getMinbu().getXwCode());

        contractsDao.insert(contracts);
        Integer contractsId = contracts.getId();

        if(requestDto.getTargetList() != null){
            for(CDGrabTargetDto targetDto : requestDto.getTargetList()){
                if(targetDto.getChainGrabGoal() != null){
                    // 抢单目标保存
                    ZContractsFactor contractsFactor2 = new ZContractsFactor();
                    contractsFactor2.setContractId(contractsId);
                    contractsFactor2.setFactorCode(targetDto.getTargetCode());
                    contractsFactor2.setFactorName(targetDto.getTargetName());
                    contractsFactor2.setFactorValue(targetDto.getChainGrabGoal().toString());
                    contractsFactor2.setFactorType(Constant.FactorType.Grab.getValue());
                    contractsFactor2.setFactorUnit(targetDto.getTargetUnit());
                    contractsFactor2.setFactorDirecton(targetDto.getTargetTo());
                    factorDao.insert(contractsFactor2);
                }
                // 链群目标保存
                //            ZContractsFactor contractsFactor = new ZContractsFactor();
                //            contractsFactor.setContractId(contractsId);
                //            contractsFactor.setFactorCode(targetDto.getTargetCode());
                //            contractsFactor.setFactorName(targetDto.getTargetName());
                //            contractsFactor.setFactorValue(targetDto.getChainGoal().toString());
                //            contractsFactor.setFactorType(Constant.FactorType.Bottom.getValue());
                //            contractsFactor.setFactorUnit(targetDto.getTargetUnit());
                //            contractsFactor.setFactorDirecton(targetDto.getTargetTo());
                //            factorDao.insert(contractsFactor);

            }
        }

        if(requestDto.getPlanInfo() != null && !requestDto.getPlanInfo().isEmpty()){
            int index = 1;
            for(ReservePlanRequestDTO planInfo : requestDto.getPlanInfo()){
                ZReservePlan plan = new ZReservePlan();
                ZReservePlanDetail planDetail = new ZReservePlanDetail();
                BeanUtils.copyProperties(planInfo, plan);
                plan.setParentId(contractsId);
                plan.setCreateUserCode(sysUser.getEmpSn());
                plan.setCreateUserName(sysUser.getEmpName());
                plan.setCreateUserTime(new Date());
//                plan.setSenduser(planInfo.getSenduser());
                plan.setExecuter(sysUser.getEmpSn());
                plan.setOrderType(String.valueOf(index));
                for(ReservePlanDetailDTO detail : planInfo.getPlanDetail()){
                    plan.setTitle(detail.getTitle());
                    planDetail.setContent(detail.getContent());
                    reservePlanDao.insert(plan);
                    planDetail.setParentId(plan.getId());
                    reservePlanDetailDao.insert(planDetail);

                    // 调用ihaier的接口进行任务创建
                    /*IhaierTask ihaierTask = new IhaierTask();
                    if(StringUtils.isNotBlank(sysUser.getEmpSn())){
                        String executor = IHaierUtil.getUserOpenId(new String[]{sysUser.getEmpSn()});
                        ihaierTask.setExecutors(executor.split(","));
                    }
                    ihaierTask.setExecutors(new String[]{sysUser.getEmpSn()});
                    if(!StringUtils.isEmpty(planInfo.getTeamworker())){
                        String ccs = IHaierUtil.getUserOpenId(planInfo.getTeamworker().split(","));
                        ihaierTask.setCcs(ccs.split(","));
                    }
                    String oid = IHaierUtil.getUserOpenId(planInfo.getCreateUserCode().split(","));
                    ihaierTask.setOpenId(oid);
                    ihaierTask.setContent(detail.getContent());
                    ihaierTask.setEndDate(planInfo.getEndTime().getTime());
                    ihaierTask.setImportant(planInfo.getIsImportant());
                    ihaierTask.setNoticeTime(15);
                    ihaierTask.setChannel("690");
                    //                ihaierTask.setCreateChannel(“”);
                    ihaierTask.setTimingNoticeTime(planInfo.getRemindTime());
                    ihaierTask.setCallBackUrl("http://jhzx.haier.net/api/v1/callBack");
                    String taskId = IHaierUtil.getTaskId(new Gson().toJson(ihaierTask));
                    plan.setTaskCode(taskId);
                    //更新taskID
                    reservePlanDao.updateById(plan);*/
                }
                index++;
            }

        }

        //加入群组
        ZHrChainInfo chainInfo=chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code", contracts.getChainCode()));
        if(chainInfo!=null&&StringUtils.isNoneBlank(chainInfo.getGroupId())) {
            String groupId = chainInfo.getGroupId();
            String[] users = new String[]{sysUser.getEmpSn()};
            IHaierUtil.joinGroup(groupId, users);
        }
    }

    @Override
    public void test(String contractId) {
        List<ZReservePlan> planInfo = reservePlanDao.selectList(new QueryWrapper<ZReservePlan>()
                .eq("parent_id", contractId));
        for (ZReservePlan detail : planInfo) {
            // 调用ihaier的接口进行任务创建
            IhaierTask ihaierTask = new IhaierTask();
            String executor = IHaierUtil.getUserOpenId(detail.getExecuter().split(","));
            ihaierTask.setExecutors(executor.split(","));
            if (!StringUtils.isEmpty(detail.getTeamworker())) {
                String ccs = IHaierUtil.getUserOpenId(detail.getTeamworker().split(","));
                ihaierTask.setCcs(ccs.split(","));
            }
            String oid = IHaierUtil.getUserOpenId(detail.getCreateUserCode().split(","));
            ihaierTask.setOpenId(oid);
            ZReservePlanDetail zReservePlanDetail = reservePlanDetailDao.selectOne(new QueryWrapper<ZReservePlanDetail>()
                    .eq("parent_id", detail.getId()));
            ihaierTask.setContent(zReservePlanDetail.getContent());
            ihaierTask.setEndDate(detail.getEndTime().getTime());
            ihaierTask.setImportant(detail.getIsImportant());
            ihaierTask.setChannel("690");
            ihaierTask.setNoticeTime(15);
            ihaierTask.setTimingNoticeTime(detail.getRemindTime());
            ihaierTask.setCallBackUrl("http://jhzx.haier.net/api/v1/callBack");
            String taskId = IHaierUtil.getTaskId(new Gson().toJson(ihaierTask));
            PlanInfoDto temp = new PlanInfoDto();
            temp.setTaskCode(taskId);
            temp.setId(detail.getId());
            //更新taskID
            reservePlanDao.updateById(detail);
        }
    }

    @Override
    @Transactional
    public void updateCDGrab(CDGrabInfoSaveRequestDto requestDto){
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu currentUser = sysUser.getMinbu();
        if(currentUser==null||StringUtils.isBlank(currentUser.getXwType5Code())||
                !currentUser.getXwType5Code().equals("1")){
            throw new RException(Constant.MSG_NO_MINBU,Constant.CODE_NO_MINBU);
        }

        ZContracts contracts = contractsDao.selectById(requestDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }

        if(!"1".equals(contracts.getStatus())){
            throw new RException("未抢入成功不可以优化");
        }

        if(contracts.getEndDate() != null && new Date().getTime() > contracts.getEndDate().getTime()){
            throw new RException("已过结束时间不可以优化");
        }

        if(contracts.getJoinTime() != null && new Date().getTime() > contracts.getJoinTime().getTime()){
            throw new RException("已过抢入截止时间不可以优化");
        }

        contracts.setStatus("6"); //设置状态为已删除
        contractsDao.updateById(contracts);

        // 保存新记录
        requestDto.setContractId(contracts.getParentId());
        requestDto.setIsUpdate("0");
        saveCDGrab(requestDto);
    }

    /**
     * 更新为已撤销
     * @param contractId
     * @return
     */
    @Override
    public Integer updateCancelGrab(String contractId){
        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts == null){
            throw new RException("合约不存在，合约id：" + contractId);
        }

        if(!"1".equals(contracts.getStatus())){
            throw new RException("未抢入成功不可以撤销");
        }

        if(contracts.getEndDate() != null && new Date().getTime() > contracts.getEndDate().getTime()){
            throw new RException("已过结束时间不可以撤销");
        }

        contracts.setStatus("5"); // 设置为已撤销
        return contractsDao.updateById(contracts);
    }

    /**
     * 更新为已踢出
     * @param contractId
     * @return
     */
    @Override
    public Integer updateKickOff(String contractId){
        ZContracts contracts = contractsDao.selectById(contractId);
        if(contracts == null){
            throw new RException("合约不存在，合约id：" + contractId);
        }

        if(!"1".equals(contracts.getStatus())){
            throw new RException("未抢入成功不可以踢出");
        }

        if(contracts.getEndDate() != null && new Date().getTime() > contracts.getEndDate().getTime()){
            throw new RException("已过结束时间不可以踢出");
        }

        contracts.setStatus("3"); // 设置为已踢出
        return contractsDao.updateById(contracts);
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