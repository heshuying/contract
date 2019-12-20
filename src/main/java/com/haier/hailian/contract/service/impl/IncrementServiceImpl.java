package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZSharePercent;
import com.haier.hailian.contract.service.IncrementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
public class IncrementServiceImpl implements IncrementService {

    @Autowired
    private ZContractsFactorDao zContractsFactorDao;

    /**
     * 计算增值分享金额
     * @param requestDto
     * @return
     */
    @Override
    public BigDecimal incrementMoney(CDGrabInfoSaveRequestDto requestDto) {
        zContractsFactorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id" , requestDto.getContractId()));
        return null;
    }
}
