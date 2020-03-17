package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.grab.CDGrabInfoSaveRequestDto;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.IncrementService;
import com.haier.hailian.contract.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;


@Service
public class IncrementServiceImpl implements IncrementService {

    @Autowired
    private ZContractsFactorDao zContractsFactorDao;

    @Autowired
    ZHrChainInfoDao zHrChainInfoDao;
    @Autowired
    ZContractsDao zContractsDao;

    /**
     * 计算增值分享金额
     * @param requestDto
     * @return
     */
    @Override
    public BigDecimal incrementMoney(CDGrabInfoSaveRequestDto requestDto) {
        ZContractsFactor fBottom = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Bottom.getValue()));
        ZContractsFactor fE2E = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.E2E.getValue()));
        ZContractsFactor fGrab = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Grab.getValue())
                .isNull("region_code"));
        if(fBottom == null || fE2E == null || fGrab == null){
            return BigDecimal.ZERO;
        }

        BigDecimal bottom = new BigDecimal(fBottom.getFactorValue());
        BigDecimal e2e = new BigDecimal(fE2E.getFactorValue());
        BigDecimal grab = new BigDecimal(fGrab.getFactorValue());


        BigDecimal money = new BigDecimal("0");


        // 抢单利润大于  e2e 利润才有 第二阶梯  50%
        if(grab.compareTo(e2e)>0){
            money = grab.subtract(e2e).multiply(new BigDecimal("0.5"));
        }
        // 第一阶段 20%
        money = money.add(e2e.subtract(bottom).multiply(new BigDecimal("0.2")));

        // 分享比例5%
        money = money.multiply(new BigDecimal(requestDto.getSharePercent())).divide(new BigDecimal("100"));

        // 乘以创单分享比例
        ZContracts contracts = zContractsDao.selectById(requestDto.getContractId());
        if(contracts == null){
            return money;
        }
        List<ZHrChainInfo> chainInfos = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
        if(chainInfos == null || chainInfos.isEmpty()){
            return money;
        }
        String cdShareRate = "100";
        if(StringUtils.isNotBlank(chainInfos.get(0).getCdShareRate())){
            cdShareRate = chainInfos.get(0).getCdShareRate();
        }
        BigDecimal cdShareRateDecimal = new BigDecimal(cdShareRate).divide(new BigDecimal("100"));
        return money.multiply(cdShareRateDecimal);

    }

    @Override
    public BigDecimal incrementMoneyShareModify(CDGrabInfoSaveRequestDto requestDto) {
        ZContractsFactor fBottom = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Bottom.getValue()));
        ZContractsFactor fE2E = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.E2E.getValue()));
        ZContractsFactor fGrab = zContractsFactorDao.selectOne(new QueryWrapper<ZContractsFactor>()
                .eq("contract_id" , requestDto.getContractId())
                .eq("factor_code" , Constant.FactorCode.Lre.getValue())
                .eq("factor_type" , Constant.FactorType.Grab.getValue())
                .isNull("region_code"));
        if(fBottom == null || fE2E == null || fGrab == null){
            return BigDecimal.ZERO;
        }

        BigDecimal bottom = new BigDecimal(fBottom.getFactorValue());
        BigDecimal e2e = new BigDecimal(fE2E.getFactorValue());
        BigDecimal grab = new BigDecimal(fGrab.getFactorValue());


        BigDecimal money = new BigDecimal("0");

        // 抢单利润大于  e2e 利润才有 第二阶梯  50%
        if(grab.compareTo(e2e)>0){
            money = grab.subtract(e2e).multiply(new BigDecimal("0.5"));
        }
        // 第一阶段 20%
        money = money.add(e2e.subtract(bottom).multiply(new BigDecimal("0.2")));

        // 分享比例5%
        money = money.multiply(new BigDecimal(requestDto.getSharePercent())).divide(new BigDecimal("100"));

        // 乘以创单分享比例
        ZContracts contracts = zContractsDao.selectById(requestDto.getContractId());
        if(contracts == null){
            return money;
        }
        List<ZHrChainInfo> chainInfos = zHrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));
        if(chainInfos == null || chainInfos.isEmpty()){
            return money;
        }
        String cdShareRate = "100";
        if(StringUtils.isNotBlank(chainInfos.get(0).getCdShareRate())){
            cdShareRate = chainInfos.get(0).getCdShareRate();
        }
        String zzfxRate = "100";
        if(StringUtils.isNotBlank(chainInfos.get(0).getZzfxRate())){
            zzfxRate = chainInfos.get(0).getZzfxRate();
        }
        BigDecimal cdShareRateDecimal = new BigDecimal(cdShareRate).divide(new BigDecimal("100"));
        BigDecimal zzfxRateDecimal = new BigDecimal(zzfxRate).divide(new BigDecimal("100"));
        return money.multiply(cdShareRateDecimal).multiply(zzfxRateDecimal);

    }

}
