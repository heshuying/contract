package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.SysXiaoweiEhrDao;
import com.haier.hailian.contract.dao.TargetBasicDao;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import com.haier.hailian.contract.util.Constant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    @Autowired
    private TargetBasicDao targetBasicDao;

    @Override
    public void saveGambling(GamblingContractDTO dto) throws Exception{
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        CurrentUser currentUser = sysUser.getCurrentUser();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //1.保存链群主抢单信息到合同主表
        ZContracts contracts = new ZContracts();
        BeanUtils.copyProperties(dto,contracts);
        contracts.setStartDate(sf.parse(dto.getStartDate()));
        contracts.setEndDate(sf.parse(dto.getEndDate()));
        contracts.setJoinTime(sf.parse(dto.getJoinTime()));
        contracts.setContractType("10");
        contracts.setStatus("0");
        contracts.setCreateName(sysUser.getEmpName());
        contracts.setCreateCode(sysUser.getEmpSn());
        contracts.setCreateTime(new Date());
        contracts.setXiaoweiCode(currentUser.getXwCode());
        contracts.setOrgName(currentUser.getOrgName());
        contracts.setOrgCode(currentUser.getOrgNum());
        contracts.setContractName(dto.getContractName()+"-"+sysUser.getEmpName());
        contractsDao.insert(contracts);
        //2.保存链群目标到目标表
        List<ChainGroupTargetDTO> chainGroupTargetList = dto.getChainGroupTargetList();
        for(ChainGroupTargetDTO chainGroupTarget:chainGroupTargetList){
                ZContractsFactor factor1 = new ZContractsFactor();
                factor1.setContractId(contracts.getId());
                factor1.setFactorValue(chainGroupTarget.getBottom()+"");
                factor1.setFactorCode(chainGroupTarget.getTargetCode());
                factor1.setFactorName(chainGroupTarget.getTargetName());
                factor1.setFactorType(Constant.FactorType.Bottom.getValue());
                factor1.setFactorUnit(chainGroupTarget.getTargetUnit());
                factorDao.insert(factor1);
                factor1.setId(null);
                factor1.setFactorValue(chainGroupTarget.getE2E()+"");
                factor1.setFactorType(Constant.FactorType.E2E.getValue());
                factorDao.insert(factor1);
                factor1.setId(null);
                factor1.setFactorValue(chainGroupTarget.getGrab()+"");
                factor1.setFactorType(Constant.FactorType.Grab.getValue());
                factorDao.insert(factor1);
        }
        //3.保存市场目标到目标表
        List<MarketTargetDTO> marketTargetList = dto.getMarketTargetList();
        for (MarketTargetDTO marketTarget : marketTargetList){
                List<MarketTargetDTO2> dto2List = marketTarget.getTargetList();
                for(MarketTargetDTO2 dto2:dto2List){
                    ZContractsFactor factor = new ZContractsFactor();
                    factor.setContractId(contracts.getId());
                    factor.setFactorValue(dto2.getTargetValue());
                    factor.setFactorCode(dto2.getTargetCode());
                    factor.setFactorName(dto2.getTargetName());
                    factor.setFactorType(Constant.FactorType.Grab.getValue());
                    factor.setFactorUnit(dto2.getTargetUnit());
                    factor.setRegionCode(marketTarget.getXwCode());
                    factor.setRegionName(marketTarget.getXwName());
                    factorDao.insert(factor);
                }
        }
    }

    @Override
    public MarketReturnDTO selectMarket() {

        MarketReturnDTO dto = new MarketReturnDTO();
        //查询42个市场小微
        List<XiaoweiEhr> list = sysXiaoweiEhrDao.selectMarket();
        dto.setMarket(list);
        TargetBasic targetBasic = new TargetBasic();
        //查询链群主举单时商圈的必填目标
        targetBasic.setTargetDim("02");
        targetBasic.setJudanFlag("1");
        List<TargetBasic> list1 = targetBasicDao.selectTarget(targetBasic);
        dto.setTargetRequired(list1);
        //查询链群主举单时商圈的可选目标
        targetBasic.setJudanFlag("0");
        List<TargetBasic> list2 = targetBasicDao.selectTarget(targetBasic);
        dto.setTarget(list2);
        return dto;
    }

    @Override
    public List<ZContracts> selectContractList(QueryContractListDTO queryDTO) {
        List<ZContracts> list = contractsDao.selectContractList(queryDTO);
        return list;
    }
}
