package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.IncrementService;
import com.haier.hailian.contract.util.Constant;
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
        BigDecimal bottom = new BigDecimal(zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Bottom.getValue())).getFactorValue());

        BigDecimal e2e = new BigDecimal(zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.E2E.getValue())).getFactorValue());

        BigDecimal grab = new BigDecimal(zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Grab.getValue())).getFactorValue());


        BigDecimal money = new BigDecimal("0");


        // 抢单利润大于  e2e 利润才有 第二阶梯  50%
        if(grab.compareTo(e2e)>0){
            money = grab.subtract(e2e).multiply(new BigDecimal("0.5"));
        }
        // 第一阶段 20%
        money = money.add(e2e.subtract(bottom).multiply(new BigDecimal("0.2")));

        // 分享比例5%
        money = money.multiply(new BigDecimal(requestDto.getSharePercent()));

        return money;
    }
}
