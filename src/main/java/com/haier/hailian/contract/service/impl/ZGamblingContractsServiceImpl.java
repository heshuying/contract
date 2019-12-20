package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.SysXiaoweiEhrDao;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.XiaoweiEhr;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import com.haier.hailian.contract.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-19
 */
@Service
public class ZGamblingContractsServiceImpl implements ZGamblingContractsService {

    @Autowired
    private ZContractsDao contractsDao;

    @Autowired
    private ZContractsFactorDao factorDao;

    @Autowired
    private SysXiaoweiEhrDao sysXiaoweiEhrDao;

    @Override
    public void saveGambling(GamblingContractDTO dto) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        CurrentUser currentUser = sysUser.getCurrentUser();
        //1.保存链群主抢单信息到合同主表
        ZContracts contracts = new ZContracts();
        BeanUtils.copyProperties(dto,contracts);
        contracts.setContractType("10");
        contracts.setStatus("0");
        contracts.setCreateName(sysUser.getEmpName());
        contracts.setCreateCode(sysUser.getEmpSn());
        contracts.setCreateTime(new Date());
        contracts.setOrgName(currentUser.getOrgName());
        contracts.setOrgCode(currentUser.getOrgNum());
        contractsDao.insert(contracts);
        //2.保存链群目标到目标表
        List<ChainGroupTargetDTO> chainGroupTargetList = dto.getChainGroupTargetList();
        for(ChainGroupTargetDTO chainGroupTarget:chainGroupTargetList){
            if(chainGroupTarget.getTargetCode().equals(Constant.FactorCode.Incom.getValue())){
                ZContractsFactor factor1 = new ZContractsFactor();
                factor1.setContractId(contracts.getId());
                factor1.setFactorValue(chainGroupTarget.getBottom()+"");
                factor1.setFactorCode(Constant.FactorCode.Incom.getValue());
                factor1.setFactorName(Constant.FactorCode.Incom.getName());
                factor1.setFactorType(Constant.FactorType.Bottom.getValue());
                factor1.setFactorUnit("万元");
                factorDao.insert(factor1);
                factor1.setId(null);
                factor1.setFactorValue(chainGroupTarget.getE2E()+"");
                factor1.setFactorType(Constant.FactorType.E2E.getValue());
                factorDao.insert(factor1);
                factor1.setId(null);
                factor1.setFactorValue(chainGroupTarget.getGrab()+"");
                factor1.setFactorType(Constant.FactorType.Grab.getValue());
                factorDao.insert(factor1);
            }else if(chainGroupTarget.getTargetCode().equals(Constant.FactorCode.Lre.getValue())){
                ZContractsFactor factor2 = new ZContractsFactor();
                factor2.setContractId(contracts.getId());
                factor2.setFactorValue(chainGroupTarget.getBottom()+"");
                factor2.setFactorCode(Constant.FactorCode.Lre.getValue());
                factor2.setFactorName(Constant.FactorCode.Lre.getName());
                factor2.setFactorType(Constant.FactorType.Bottom.getValue());
                factor2.setFactorUnit("万元");
                factorDao.insert(factor2);
                factor2.setId(null);
                factor2.setFactorValue(chainGroupTarget.getE2E()+"");
                factor2.setFactorType(Constant.FactorType.E2E.getValue());
                factorDao.insert(factor2);
                factor2.setId(null);
                factor2.setFactorValue(chainGroupTarget.getGrab()+"");
                factor2.setFactorType(Constant.FactorType.Grab.getValue());
                factorDao.insert(factor2);
            }else if(chainGroupTarget.getTargetCode().equals(Constant.FactorCode.Mll.getValue())){
                ZContractsFactor factor3 = new ZContractsFactor();
                factor3.setContractId(contracts.getId());
                factor3.setFactorValue(chainGroupTarget.getBottom()+"");
                factor3.setFactorCode(Constant.FactorCode.Mll.getValue());
                factor3.setFactorName(Constant.FactorCode.Mll.getName());
                factor3.setFactorType(Constant.FactorType.Bottom.getValue());
                factor3.setFactorUnit("万元");
                factorDao.insert(factor3);
                factor3.setId(null);
                factor3.setFactorValue(chainGroupTarget.getE2E()+"");
                factor3.setFactorType(Constant.FactorType.E2E.getValue());
                factorDao.insert(factor3);
                factor3.setId(null);
                factor3.setFactorValue(chainGroupTarget.getGrab()+"");
                factor3.setFactorType(Constant.FactorType.Grab.getValue());
                factorDao.insert(factor3);
            }

        }
        //3.保存市场目标到目标表
        List<MarketTargetDTO> marketTargetList = dto.getMarketTargetList();
        for (MarketTargetDTO marketTarget : marketTargetList){
            ZContractsFactor factor = new ZContractsFactor();
            factor.setContractId(contracts.getId());
            factor.setFactorValue(marketTarget.getIncome()+"");
            factor.setFactorCode(Constant.FactorCode.Incom.getValue());
            factor.setFactorName(Constant.FactorCode.Incom.getName());
            factor.setFactorType(Constant.FactorType.Grab.getValue());
            factor.setFactorUnit("万元");
            factor.setRegionCode(marketTarget.getXwCode());
            factor.setRegionName(marketTarget.getXwName());
            factorDao.insert(factor);
            ZContractsFactor factor1 = new ZContractsFactor();
            factor1.setContractId(contracts.getId());
            factor1.setFactorValue(marketTarget.getHigh()+"");
            factor1.setFactorCode(Constant.FactorCode.HighPercent.getValue());
            factor1.setFactorName(Constant.FactorCode.HighPercent.getName());
            factor1.setFactorType(Constant.FactorType.Grab.getValue());
            factor1.setFactorUnit("%");
            factor1.setRegionCode(marketTarget.getXwCode());
            factor1.setRegionName(marketTarget.getXwName());
            factorDao.insert(factor1);
            ZContractsFactor factor2 = new ZContractsFactor();
            factor2.setContractId(contracts.getId());
            factor2.setFactorValue(marketTarget.getLow()+"");
            factor2.setFactorCode(Constant.FactorCode.LowPercent.getValue());
            factor2.setFactorName(Constant.FactorCode.LowPercent.getName());
            factor2.setFactorType(Constant.FactorType.Grab.getValue());
            factor2.setFactorUnit("%");
            factor2.setRegionCode(marketTarget.getXwCode());
            factor2.setRegionName(marketTarget.getXwName());
            factorDao.insert(factor2);
        }
    }

    @Override
    public List<XiaoweiEhr> selectMarket() {

        List<XiaoweiEhr> list = sysXiaoweiEhrDao.selectMarket();
        return list;
    }

    @Override
    public List<ZContracts> selectContractList(QueryContractListDTO queryDTO) {
        List<ZContracts> list = contractsDao.selectContractList(queryDTO);
        return list;
    }
}
