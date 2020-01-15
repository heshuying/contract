package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
        if(null == dto.getId() || dto.getId() == 0){
           //ID 为空时为新增
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
        }else{
           //ID 不为0时，为修改
            contracts = contractsDao.selectByContractId(dto.getId());
            contracts.setStartDate(sf.parse(dto.getStartDate()));
            contracts.setEndDate(sf.parse(dto.getEndDate()));
            contracts.setJoinTime(sf.parse(dto.getJoinTime()));
            contracts.setShareSpace(dto.getShareSpace());
            contractsDao.updateById(contracts);
            //修改时删除原有目标
            factorDao.delete(new QueryWrapper<ZContractsFactor>().eq("contract_id",dto.getId()));
            contractsProductDao.delete(new QueryWrapper<ZContractsProduct>().eq("contract_id",dto.getId()));
        }
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
        if(null != productList){
            for (ContractProductDTO productDTO : productList){
                ZContractsProduct contractsProduct = new ZContractsProduct();
                contractsProduct.setContractId(contracts.getId());
                contractsProduct.setProductSeries(productDTO.getProductSeries());
                contractsProduct.setQtyYear(productDTO.getQtyYear());
                contractsProduct.setQtyMonth(productDTO.getQtyMonth());
                contractsProductDao.insert(contractsProduct);
            }
        }

    }

    @Override
    public MarketReturnDTO selectMarket() {

        MarketReturnDTO dto = new MarketReturnDTO();
        //查询42个市场小微
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu minbu = sysUser.getMinbu();
        if(minbu == null){
            throw new RException("没有维护最小作战单元，无法举单",Constant.CODE_VALIDFAIL);
        }
        String ptCode = minbu.getPtCode();
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
        //查询还没有抢入、抢入未截止的合约
        String contractIds = contractsDao.selectContractToUpdate()+",";
        if(null != list && list.size() > 0){
            for(ZContracts zContracts:list){
                int id = zContracts.getId();
                if(contractIds.indexOf(id+",")>0){
                    zContracts.setStatus2("1");
                }else {
                    zContracts.setStatus2("0");
                }
            }
        }
        return list;
    }

    @Override
    public List<ZContracts> selectMyGrabContract(QueryContractListDTO queryDTO) {

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        List<ZContracts> list = contractsDao.selectMyGrabContract(queryDTO);
        if(null != list && list.size() > 0){
            for(ZContracts zContracts:list){
                Date endDate = zContracts.getEndDate();
                Date joinTime = zContracts.getJoinTime();
                if(joinTime.after(new Date())){
                    zContracts.setStatus2("1");
                }else {
                    zContracts.setStatus2("0");
                }
                if(endDate.after(new Date())){
                    zContracts.setStatus3("1");
                }else {
                    zContracts.setStatus3("0");
                }
            }
        }
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
        String xwCode = "";
        if(sysUser.getMinbu() != null) xwCode = sysUser.getMinbu().getLittleXwCode();
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
            //查询抢入未截止的可抢合约
            contractsList = contractsDao.selectToGrabContract(dto);
            //查询抢入已截止但是本作战单元被踢出的可抢合约
            List<ZContracts> list = contractsDao.selectKickedOutContract(dto);
            if(null == contractsList) contractsList = new ArrayList<>();
            if(null != list && list.size()>0){
                contractsList.addAll(list);
            }
        }

        return contractsList;
    }

    @Override
    public List<ZProductChain> selectProductSeries(QueryProductChainDTO dto) {
        return productChainDao.selectSeriesByChainCode(dto.getChainCode());
    }

    @Override
    public GamblingContractDTO selectContractById(Integer contractId) {
        GamblingContractDTO dto = new GamblingContractDTO();
        //1.查询抢单主表数据
        ZContracts contracts = contractsDao.selectByContractId(contractId);
        dto.setContractName(contracts.getContractName());
        dto.setJoinTime(contracts.getJoinTimeStr());
        dto.setStartDate(contracts.getStartDateStr());
        dto.setEndDate(contracts.getEndDateStr());
        dto.setShareSpace(contracts.getShareSpace());
        dto.setChainCode(contracts.getChainCode());
        //2.查询链群目标
        List<ChainGroupTargetDTO> chainList = factorDao.selectChainFactorByContractId(contractId);
        dto.setChainGroupTargetList(chainList);
        //3.查询42市场的目标
        List<ZContractsFactor> maketList = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id",contractId).isNotNull("region_code").orderByAsc("region_code").orderByAsc("factor_code"));
        List<MarketTargetDTO> marketTargetList = new ArrayList<>();
        if(null != maketList && maketList.size()>0){
            MarketTargetDTO marketTargetDTO = new MarketTargetDTO();
            List<MarketTargetDTO2> targetList = new ArrayList<>();
            String reginCode = "";
            for(int i=0;i<maketList.size();i++){
                ZContractsFactor zContractsFactor = maketList.get(i);
                if(i==0){
                    reginCode = zContractsFactor.getRegionCode();
                }
                if(reginCode.equals(zContractsFactor.getRegionCode())){
                    MarketTargetDTO2 marketTargetDTO2 = new MarketTargetDTO2();
                    marketTargetDTO2.setTargetCode(zContractsFactor.getFactorCode());
                    marketTargetDTO2.setTargetName(zContractsFactor.getFactorName());
                    marketTargetDTO2.setTargetUnit(zContractsFactor.getFactorUnit());
                    marketTargetDTO2.setTargetValue(zContractsFactor.getFactorValue());
                    targetList.add(marketTargetDTO2);
                    marketTargetDTO.setXwCode(zContractsFactor.getRegionCode());
                    marketTargetDTO.setXwName(zContractsFactor.getRegionName());
                }else {
                    marketTargetDTO.setTargetList(targetList);
                    marketTargetList.add(marketTargetDTO);
                    marketTargetDTO = new MarketTargetDTO();
                    targetList = new ArrayList<>();
                    reginCode = zContractsFactor.getRegionCode();
                    MarketTargetDTO2 marketTargetDTO2 = new MarketTargetDTO2();
                    marketTargetDTO2.setTargetCode(zContractsFactor.getFactorCode());
                    marketTargetDTO2.setTargetName(zContractsFactor.getFactorName());
                    marketTargetDTO2.setTargetUnit(zContractsFactor.getFactorUnit());
                    marketTargetDTO2.setTargetValue(zContractsFactor.getFactorValue());
                    targetList.add(marketTargetDTO2);
                    marketTargetDTO.setXwCode(zContractsFactor.getRegionCode());
                    marketTargetDTO.setXwName(zContractsFactor.getRegionName());
                }
            }
            marketTargetDTO.setTargetList(targetList);
            marketTargetList.add(marketTargetDTO);
        }
        dto.setMarketTargetList(marketTargetList);
        //4.查询产品目标
        List<ZContractsProduct> productList = contractsProductDao.selectList(new QueryWrapper<ZContractsProduct>().eq("contract_id",contractId));
        List<ContractProductDTO> productDTOs = new ArrayList<>();
        for(ZContractsProduct product : productList){
            ContractProductDTO productDTO = new ContractProductDTO();
            BeanUtils.copyProperties(product,productDTO);
            productDTOs.add(productDTO);
        }
        dto.setProductList(productDTOs);
        return dto;
    }

    @Override
    public void exportMarket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu minbu = sysUser.getMinbu();
        String ptCode = "";
        if(minbu != null) ptCode = minbu.getPtCode();
        TOdsMinbu tOdsMinbu = new TOdsMinbu();
        tOdsMinbu.setPtCode(ptCode);
        tOdsMinbu.setXwType3Code("4");
        tOdsMinbu.setXwType5Code("2");
        List<TOdsMinbu> list = tOdsMinbuDao.selectMarket(tOdsMinbu);

        Workbook workbook = new HSSFWorkbook();
        ExcelUtil.buildSheet(workbook, "42中心", list, TEMPLATE_TITLE);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        workbook.write(bot);
        ExcelUtil.export(request,response,workbook,"42中心.xls");

    }

    @Override
    public List<MarketTargetDTO> getMarketTargetListByExcel(InputStream inputStream, String fileName) throws Exception{

        List list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(inputStream, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }
                MarketTargetDTO3 marketTargetDTO3 = new MarketTargetDTO3();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    if(y==0) marketTargetDTO3.setXwCode(cell.getStringCellValue());
                    if(y==1) marketTargetDTO3.setXwName(cell.getStringCellValue());
                    if(y==2) marketTargetDTO3.setIncome(BigDecimal.valueOf(cell.getNumericCellValue()));
                    if(y==3) marketTargetDTO3.setHigh(BigDecimal.valueOf(cell.getNumericCellValue()));
                }
                list.add(marketTargetDTO3);
            }
        }
        work.close();
        return list;
    }

    @Override
    public List<ZContracts> selectHomePageContract(QueryContractListDTO2 queryDTO) {
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        List<ZContracts> list = contractsDao.selectHomePageContract(queryDTO);
        return list;
    }

    //校验excle格式
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }


    private static final ExcelUtil.CellHeadField[] TEMPLATE_TITLE = {
            new ExcelUtil.CellHeadField("42中心", "xwCode"),
            new ExcelUtil.CellHeadField("42中心", "xwName"),
            new ExcelUtil.CellHeadField("收入(万元)", "income"),
            new ExcelUtil.CellHeadField("高端占比(%)", "high")
    };



}
