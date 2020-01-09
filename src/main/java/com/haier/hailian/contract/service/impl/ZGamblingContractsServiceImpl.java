package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
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
import java.util.ArrayList;
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
    private TargetBasicDao targetBasicDao;
    @Autowired
    private TOdsMinbuEmpDao tOdsMinbuEmpDao;
    @Autowired
    private ZNodeTargetPercentInfoDao nodeTargetPercentInfoDao;
    @Autowired
    private TOdsMinbuDao tOdsMinbuDao;
    @Autowired
    private ZProductChainDao productChainDao;
    @Autowired
    private ZContractsProductDao contractsProductDao;


    @Override
    public void saveGambling(GamblingContractDTO dto) throws Exception{
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu currentUser = sysUser.getMinbu();
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
        contracts.setOrgName(currentUser.getLittleXwName());
        contracts.setOrgCode(currentUser.getLittleXwCode());
        contracts.setContractName(dto.getContractName()+"-"+sysUser.getEmpName());
        contracts.setOpenValid(dto.getOpenValid());
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
        //4 保存产品系列目标到合约产品表
        List<ContractProductDTO> productList = dto.getProductList();
        for (ContractProductDTO productDTO : productList){
            ZContractsProduct contractsProduct = new ZContractsProduct();
            contractsProduct.setContractId(contracts.getId());
            contractsProduct.setProductSeries(productDTO.getProductSeries());
            contractsProduct.setQtyYear(productDTO.getQtyYear());
            contractsProduct.setQtyMonth(productDTO.getQtyMonth());
            contractsProductDao.insert(contractsProduct);
        }
    }

    @Override
    public MarketReturnDTO selectMarket() {

        MarketReturnDTO dto = new MarketReturnDTO();
        //查询42个市场小微
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu currentUser = sysUser.getMinbu();
        String ptCode = currentUser.getPtCode();
        TOdsMinbu tOdsMinbu = new TOdsMinbu();
        tOdsMinbu.setPtCode(ptCode);
        tOdsMinbu.setXwType3Code("4");
        tOdsMinbu.setXwType5Code("2");
        List<TOdsMinbu> list = tOdsMinbuDao.selectMarket(tOdsMinbu);
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

    @Override
    public List<ZContracts> selectMyStartContract(QueryContractListDTO queryDTO) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        List<ZContracts> list = contractsDao.selectContractList(queryDTO);
        return list;
    }

    @Override
    public List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO) {

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        List<ZContracts> list = contractsDao.selectMyGrabContract(queryDTO);
        return list;
    }

    @Override
    public List<ZContracts> selectToGrabContract(QueryContractListDTO dto) {
        List<ZContracts> contractsList = new ArrayList<>();
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        String userCode = sysUser.getEmpSn();
        //查询用户所属的最小作战单元
        List<TOdsMinbuEmp> empList = tOdsMinbuEmpDao.selectList(new QueryWrapper<TOdsMinbuEmp>().eq("littleEmpsn", userCode));
        if(null != empList && empList.size() > 0 ){
            String xwCodeStr = "";
            //查询最小作战单元所属的链群
            for(TOdsMinbuEmp emp:empList){
                xwCodeStr += emp.getLittleXwCode()+",";
            }
            String[] xwCode = xwCodeStr.split(",");
            List<ZNodeTargetPercentInfo> chainList = nodeTargetPercentInfoDao.selectChainByLittleXwCode(xwCode);
            //查询所属链群可抢入的合约
            if(null != chainList && chainList.size()>0){
                String chainStr = "";
                for(ZNodeTargetPercentInfo percentInfo:chainList){
                    chainStr += percentInfo.getLqCode()+",";
                }
                String[] chainCode = chainStr.split(",");
                dto.setStatus("0");
                dto.setChainCodeList(chainCode);
                dto.setUserCode(userCode);
                dto.setLittleXwCode(xwCode);
                contractsList = contractsDao.selectToGrabContract(dto);
            }
        }
        return contractsList;
    }

    @Override
    public List<ZProductChain> selectProductSeries(QueryProductChainDTO dto) {
        return productChainDao.selectList(new QueryWrapper<ZProductChain>().eq("chain_code",dto.getChainCode()));
    }


}
