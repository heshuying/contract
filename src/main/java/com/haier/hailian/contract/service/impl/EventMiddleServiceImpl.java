package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.EventMiddleDTO;
import com.haier.hailian.contract.dto.EventMiddleTYDTO;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.EventMiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

}
