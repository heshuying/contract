package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.TargetTrend;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ContractViewService;
import com.haier.hailian.contract.service.EventMiddleService;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 01431594 on 2020/4/10.
 */
@Service
public class EventMiddleServiceImpl implements EventMiddleService{

    @Autowired
    private ZContractsFactorDao contractsFactorDao;
    @Autowired
    private ZContractsDao contractsDao;
    @Autowired
    private ContractViewService contractViewService;

    @Override
    public List<ZContractsFactor> selectChainTarget(EventMiddleDTO dto) {
//        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMM");
//        String date = dto.getDate();
//        String month = "";
//        try {
//            month = sf2.format(sf1.parse(date));
//        } catch (ParseException e) {
//            throw new RException("日期类型不正确", Constant.CODE_VALIDFAIL);
//        }
//        dto.setDate(month);
        return contractsFactorDao.selectChainGamblingTarget(dto);

    }

    @Override
    public List<EventMiddleTYDTO> selectTyTarget(EventMiddleDTO dto) {
        List<EventMiddleTYDTO> resultList = new ArrayList<>();
        resultList = contractsFactorDao.selectTyTarget(dto.getContractId());
        return resultList;
    }

    @Override
    public List<EventMiddleCdDTO> selectCdTarget(EventMiddleDTO dto) {

        List<EventMiddleCdDTO> resultList = contractsFactorDao.selectCdTarget(dto.getContractId());
        return resultList;
    }

    @Override
    public List<ContractProductDTO> selectProductTarget(EventMiddleDTO dto) {
        List<ContractProductDTO> list = contractViewService.staticSerial(dto.getContractId());
        return list;
    }

    @Override
    public List<EventMiddleTrendDTO> selectChainTargetTrend(EventMiddleDTO dto) {
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        TargetTrend targetTrend = contractsFactorDao.selectChainTargetTrend(dto.getContractId());
        Date startDate = targetTrend.getStartDate();
        List<EventMiddleTrendDTO> list = new ArrayList<>();
        EventMiddleTrendDTO trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue1());
        trendDTO.setMonth(targetTrend.getStartDate1()==null? sf.format(DateFormatUtil.addDateMonths(startDate,-3)):sf.format(targetTrend.getStartDate1()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue2());
        trendDTO.setMonth(targetTrend.getStartDate2()==null?sf.format(DateFormatUtil.addDateMonths(startDate,-2)):sf.format(targetTrend.getStartDate2()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue3());
        trendDTO.setMonth(targetTrend.getStartDate3()==null?sf.format(DateFormatUtil.addDateMonths(startDate,-1)):sf.format(targetTrend.getStartDate3()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue());
        trendDTO.setMonth(targetTrend.getStartDate()==null?"":sf.format(targetTrend.getStartDate()));
        list.add(trendDTO);
        return list;
    }

    @Override
    public EventMiddleChainShareDTO selectChainShare(EventMiddleDTO dto) {
        ZContracts contracts = contractsDao.selectById(dto.getContractId());
        EventMiddleChainShareDTO chainShareDTO = new EventMiddleChainShareDTO();
        chainShareDTO.setPlan(contracts.getShareMoney()+"");
        return chainShareDTO;

    }

    @Override
    public List<EventMiddleTrendDTO> selectChainShareTrend(EventMiddleDTO dto) {
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        TargetTrend targetTrend = contractsDao.selectChainShareTrend(dto.getContractId());
        Date startDate = targetTrend.getStartDate();
        List<EventMiddleTrendDTO> list = new ArrayList<>();
        EventMiddleTrendDTO trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue1());
        trendDTO.setMonth(targetTrend.getStartDate1()==null? sf.format(DateFormatUtil.addDateMonths(startDate,-3)):sf.format(targetTrend.getStartDate1()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue2());
        trendDTO.setMonth(targetTrend.getStartDate2()==null?sf.format(DateFormatUtil.addDateMonths(startDate,-2)):sf.format(targetTrend.getStartDate2()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue3());
        trendDTO.setMonth(targetTrend.getStartDate3()==null?sf.format(DateFormatUtil.addDateMonths(startDate,-1)):sf.format(targetTrend.getStartDate3()));
        list.add(trendDTO);
        trendDTO = new EventMiddleTrendDTO();
        trendDTO.setTarget(targetTrend.getFactorValue());
        trendDTO.setMonth(targetTrend.getStartDate()==null?"":sf.format(targetTrend.getStartDate()));
        list.add(trendDTO);
        return list;
    }

    @Override
    public List<EventMiddleCdDTO> selectCdShare(EventMiddleDTO dto) {
        List<EventMiddleCdDTO> resultList = contractsFactorDao.selectCdTarget(dto.getContractId());
        return resultList;
    }

}
