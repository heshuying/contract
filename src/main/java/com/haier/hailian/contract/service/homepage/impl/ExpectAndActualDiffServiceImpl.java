package com.haier.hailian.contract.service.homepage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dao.ZNodeTargetPercentInfoDao;
import com.haier.hailian.contract.dto.homepage.ExpectAndActualDiffDto;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import com.haier.hailian.contract.service.homepage.ExpectAndActualDiffService;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class ExpectAndActualDiffServiceImpl implements ExpectAndActualDiffService {

    @Autowired
    private ZNodeTargetPercentInfoDao zNodeTargetPercentInfoDao;
    @Autowired
    private ZContractsDao zContractsDao;
    @Autowired
    private ZContractsFactorDao zContractsFactorDao;


    @Override
    public Map<String, Object> getChainGrabNum(ExpectAndActualDiffDto expectAndActualDiffDto) {

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
        // 创单达标
        res.put("cdQualifiedNum" , cdActualNum);

        return res;
    }
}
