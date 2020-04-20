package com.haier.hailian.contract.service.homepage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.VJdxpDao;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZNodeTargetPercentInfoDao;
import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;
import com.haier.hailian.contract.entity.VJdxp;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import com.haier.hailian.contract.service.homepage.ExpectAndActualDiffService;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExpectAndActualDiffServiceImpl implements ExpectAndActualDiffService {

    @Autowired
    private ZNodeTargetPercentInfoDao zNodeTargetPercentInfoDao;
    @Autowired
    private ZContractsDao zContractsDao;
    @Autowired
    private ZContractsFactorDao zContractsFactorDao;
    @Autowired
    private VJdxpDao vJdxpDao;


    @Override
    public Map<String, Object> getChainGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto) {

        // 转换时间
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = DateFormatUtil.stringToDate(expectAndActualDiffDto.getMonth() , "yyyy-MM");
        String startTime = format.format(date);
        Date endDate = DateFormatUtil.addDateMonths(date , 1);
        String endTime = format.format(endDate);

        Map<String, Object> res = new HashMap<>();
        int tyExpectNum = zNodeTargetPercentInfoDao.selectCount(
                new QueryWrapper<ZNodeTargetPercentInfo>()
                        .eq("lq_code" , expectAndActualDiffDto.getChainCode())
                        .isNull("share_percent")
        );
        // 体验应抢
        res.put("tyExpectNum" , tyExpectNum);

        int tyActualNum = zContractsDao.selectCount(
                new QueryWrapper<ZContracts>()
                        .eq("chain_code" , expectAndActualDiffDto.getChainCode())
                        .eq("contract_type" , "20")
                        .in("status" , new String[]{"1" , "8"})
                        .gt("end_date" , startTime)
                        .lt("end_date" , endTime)
        );
        // 体验实际
        res.put("tyActualNum" , tyActualNum);

        Map<String , Object> exp = new HashMap<>();
        exp.put("startTime" , startTime);
        exp.put("endTime" , endTime);
        exp.put("chainCode" , expectAndActualDiffDto.getChainCode());
        int tyQualifiedNum = zContractsFactorDao.selectTyQualified(exp);

        // 体验达标
        res.put("tyQualifiedNum" , tyQualifiedNum);
        // 体验未达标
        res.put("tyNotQualifiedNum" , tyActualNum-tyQualifiedNum);

        int cdActualNum = zContractsDao.selectCount(
                new QueryWrapper<ZContracts>()
                        .eq("chain_code" , expectAndActualDiffDto.getChainCode())
                        .eq("contract_type" , "30")
                        .in("status" , new String[]{"1" , "8"})
                        .gt("end_date" , startTime)
                        .lt("end_date" , endTime)
        );
        // 创单实际抢入
        res.put("cdActualNum" , cdActualNum);
        // 创单抢入达标
        res.put("cdQualifiedNum" , cdActualNum);
        // 创单抢入未达标
        res.put("cdNotQualifiedNum" , cdActualNum-cdActualNum);

        // TODO 体验暂时没有实际数
        res.put("tyReachNum" , 0);
        res.put("tyNotReachNum" , 0);
        // TODO 创单是否达成(按照实际达成全部满足计算)
        // TODO 创单链群增值分享(按照实际达成全部满足计算)
        List<Integer> cdReachList = zContractsFactorDao.selectCdReach(exp);
        if(cdReachList.size()>0 && cdReachList!=null){
            // 创单达成
            res.put("cdReachNum" , cdReachList.size());
            // 创单增值分享数量
            res.put("cdShareNum" , cdReachList.size());
            // 创单未达成
            res.put("cdNotReachNum" , cdActualNum-cdReachList.size());
            // 创单无增值分享数量
            res.put("cdNotShareNum" , cdActualNum-cdReachList.size());
        }else{
            // 创单达成
            res.put("cdReachNum" , 0);
            // 创单增值分享数量
            res.put("cdShareNum" , 0);
            // 创单未达成
            res.put("cdNotReachNum" , cdActualNum);
            // 创单无增值分享数量
            res.put("cdNotShareNum" , cdActualNum);
        }

        return res;
    }


    @Override
    public Map<String, Object> getGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto) {

        Map<String, Object> res = new HashMap<>();

        // 转换时间
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = DateFormatUtil.stringToDate(expectAndActualDiffDto.getMonth() , "yyyy-MM");
        String startTime = format.format(date);
        Date endDate = DateFormatUtil.addDateMonths(date , 1);
        String endTime = format.format(endDate);

        Map<String , Object> exp = new HashMap<>();
        exp.put("startTime" , startTime);
        exp.put("endTime" , endTime);
        exp.put("chainCode" , expectAndActualDiffDto.getChainCode());
        exp.put("orgCode" , expectAndActualDiffDto.getOrgCode());
        exp.put("contractId" ,expectAndActualDiffDto.getContractId());
        List<ZContractsFactor> list = zContractsFactorDao.selectGrabInfo(exp);
        // 抢单目标
        res.put("grabInfo" , list);
        // 增值分享目标
        if(list != null && list.size()>0){
            ZContracts zContracts = zContractsDao.selectOne(new QueryWrapper<ZContracts>()
                    .eq("id" , list.get(0).getContractId())
            );

            res.put("shareTarget" , zContracts.getShareSpace());
        }else{
            res.put("shareTarget" , 0);
        }

        // TODO 节点增值分享实际
        // 评价
        String newMonth = expectAndActualDiffDto.getMonth().replace("-" , "").replace(" ","");
        expectAndActualDiffDto.setMonth(newMonth);
        VJdxp vJdxp = vJdxpDao.getOrgStar(expectAndActualDiffDto);
        if(vJdxp != null){
            res.put("star" , vJdxp.getPjM());
        }else {
            res.put("star" , "");
        }

        return res;
    }

    @Override
    public Map<String, Object> getTYGrabInfo(ExpectAndActualDiffDto expectAndActualDiffDto) {
        Map<String, Object> res = new HashMap<>();

        Map<String , Object> exp = new HashMap<>();
        exp.put("chainCode" , expectAndActualDiffDto.getChainCode());
        exp.put("orgCode" , expectAndActualDiffDto.getOrgCode());
        exp.put("contractId" ,expectAndActualDiffDto.getContractId());
        List<ZContractsFactor> list = zContractsFactorDao.selectTYGrabInfo(exp);
        // 体验举单目标
        if (list != null && list.size()>0){
            res.put("targetIncome" , list.get(0).getFactorValue());
        }else{
            res.put("targetIncome" , 0);
        }
        res.put("actualIncome" , 0);
        res.put("targetIncrease" , 0);
        res.put("actualIncrease" , 0);


        res.put("targetShareMoney" , 0);
        res.put("actualShareMoney" , 0);
        res.put("attract" , 1);

        return res;
    }

    @Override
    public Map<String, Object> grabStarMap(ExpectAndActualDiffDto expectAndActualDiffDto) {
        return null;
    }


}
