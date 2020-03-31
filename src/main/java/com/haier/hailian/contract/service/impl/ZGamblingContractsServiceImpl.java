package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import com.haier.hailian.contract.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
    @Autowired
    private ZHrChainInfoDao hrChainInfoDao;
    @Autowired
    private ZContractsXwType3Dao xwtype3Dao;


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
            contracts.setCheckTime(sf.parse(dto.getCheckTime()));
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
            contracts.setCheckTime(sf.parse(dto.getCheckTime()));
            contracts.setShareSpace(dto.getShareSpace());
            contractsDao.updateById(contracts);
            //修改时删除原有目标
            factorDao.delete(new QueryWrapper<ZContractsFactor>().eq("contract_id",dto.getId()));
            contractsProductDao.delete(new QueryWrapper<ZContractsProduct>().eq("contract_id",dto.getId()));
        }
        //2.保存链群目标到目标表
        List<ChainGroupTargetDTO> chainGroupTargetList = dto.getChainGroupTargetList();
        for(ChainGroupTargetDTO chainGroupTarget:chainGroupTargetList){
            if(null==chainGroupTarget.getGrab()) continue;
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
                    factor.setMeshCode(marketTarget.getNodeCode());
                    factor.setMeshName(marketTarget.getNodeName());
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
                contractsProductDao.insert(contractsProduct);
            }
        }

    }

    @Override
    public MarketReturnDTO selectMarket(String chainCode) {

        MarketReturnDTO dto = new MarketReturnDTO();
        //查询42个市场小微
        List<ZNodeTargetPercentInfo> list = nodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("lq_code",chainCode).isNull("share_percent"));
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
        queryDTO.setParentId(0);
        List<ZContracts> list = contractsDao.selectContractList(queryDTO);
        //查询还没有抢入、抢入未截止的合约
        String contractIds = contractsDao.selectContractToUpdate()+",";
        if(null != list && list.size() > 0){
            for(ZContracts zContracts:list){
                int id = zContracts.getId();
                if(contractIds.indexOf(id+",")<0){
                    zContracts.setStatus2("0");
                }else {
                    zContracts.setStatus2("1");
                }
                if(null != zContracts.getCheckTime() && zContracts.getCheckTime().after(new Date())){
                    zContracts.setStatus4("1");
                }else {
                    zContracts.setStatus4("0");
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
    public List<ContractProductDTO> selectProductSeries(QueryProductChainDTO dto) {
        //1.z_target_basic 表查询爆款产品目标
        List<TargetBasic> targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("chain_code",dto.getChainCode()).eq("period_code",dto.getMonth()).eq("target_diff_type","002"));
        if( targetList == null || targetList.size() == 0 ) {
            targetList = targetBasicDao.selectList(new QueryWrapper<TargetBasic>().eq("target_diff_type","002"));
        }
        //2.查询链群下的爆款产品的系列和场景
        List<ZProductChain> list = productChainDao.selectSeriesByChainCode(dto.getChainCode(),dto.getMonth());
        List<ContractProductDTO> productList = new ArrayList<>();
        for(ZProductChain productChain : list){
            ContractProductDTO productDTO = new ContractProductDTO();
            productDTO.setProductSeries(productChain.getProductSeries());
            productDTO.setSceneName(productChain.getSceneName());
            List<ProductTargetDTO> productTargetList = new ArrayList<>();
            for(TargetBasic targetBasic:targetList){
                ProductTargetDTO targetDTO = new ProductTargetDTO();
                targetDTO.setTargetCode(targetBasic.getTargetCode());
                targetDTO.setTargetName(targetBasic.getTargetName());
                targetDTO.setTargetUnit(targetBasic.getTargetUnit());
                targetDTO.setQtyMonth(null);
                targetDTO.setQtyYear(null);
                productTargetList.add(targetDTO);
            }
            productDTO.setTargetList(productTargetList);
            productList.add(productDTO);
        }
        return productList;
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
        dto.setCheckTime(contracts.getCheckTimeStr());
        dto.setId(contractId);
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
                    marketTargetDTO2.setIsLqTarget(zContractsFactor.getIsLqTarget());
                    targetList.add(marketTargetDTO2);
                    marketTargetDTO.setXwCode(zContractsFactor.getRegionCode());
                    marketTargetDTO.setXwName(zContractsFactor.getRegionName());
                    marketTargetDTO.setNodeCode(zContractsFactor.getMeshCode());
                    marketTargetDTO.setNodeName(zContractsFactor.getMeshName());
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
                    marketTargetDTO2.setIsLqTarget(zContractsFactor.getIsLqTarget());
                    targetList.add(marketTargetDTO2);
                    marketTargetDTO.setXwCode(zContractsFactor.getRegionCode());
                    marketTargetDTO.setXwName(zContractsFactor.getRegionName());
                    marketTargetDTO.setNodeCode(zContractsFactor.getMeshCode());
                    marketTargetDTO.setNodeName(zContractsFactor.getMeshName());
                }
            }
            marketTargetDTO.setTargetList(targetList);
            marketTargetList.add(marketTargetDTO);
        }
        dto.setMarketTargetList(marketTargetList);
        //4.查询产品目标
        List<ContractProductDTO> productDTOs = contractsProductDao.selectProductByContractId(contractId);
        dto.setProductList(productDTOs);
        //5.查询主链群的资源类型的最小作战单元个数
        String chainCode = contracts.getChainCode();
        List<ContractXwType3DTO> xwType3DTOs = xwtype3Dao.selectXwType3ByContractId(chainCode,contractId);
        dto.setXwType3List(xwType3DTOs);
        //5.查询子链群的合约
        List<ZContracts> children = contractsDao.selectList(new QueryWrapper<ZContracts>().eq("parent_id",contractId));
        if(null != children && children.size()>0){
            List<ChildTargetDTO> childrenList = new ArrayList<>();
            for(ZContracts child:children){
                ChildTargetDTO childTargetDTO = new ChildTargetDTO();
                Integer childId = child.getId();
                childTargetDTO.setId(childId);
                childTargetDTO.setShareSpace(child.getShareSpace());
                childTargetDTO.setChildChainCode(child.getChainCode());
                childTargetDTO.setChildChainName(child.getContractName().substring(0,child.getContractName().lastIndexOf("-")));
                //6.查询子链群的链群目标
                List<ChainGroupTargetDTO> childTarget = factorDao.selectChainFactorByContractId(childId);
                childTargetDTO.setChildTargetList(childTarget);
                //7.查询子链群的市场目标
                List<ZContractsFactor> childMarkets = factorDao.selectList(new QueryWrapper<ZContractsFactor>().eq("contract_id",childId).isNotNull("region_code").orderByAsc("region_code").orderByAsc("factor_code"));
                List<MarketTargetDTO> childMarketList = new ArrayList<>();
                if(null != childMarkets && childMarkets.size()>0){
                    String regionCode = "";
                    MarketTargetDTO childMarket = new MarketTargetDTO();
                    List<MarketTargetDTO2> childMarket2 = new ArrayList<>();
                    for(int i=0;i<childMarkets.size();i++){
                        ZContractsFactor zContractsFactor = childMarkets.get(i);
                        if(i==0){
                            regionCode = zContractsFactor.getRegionCode();
                        }
                        if(regionCode.equals(zContractsFactor.getRegionCode())){
                            MarketTargetDTO2 marketTargetDTO2 = new MarketTargetDTO2();
                            marketTargetDTO2.setIsLqTarget(zContractsFactor.getIsLqTarget());
                            marketTargetDTO2.setTargetCode(zContractsFactor.getFactorCode());
                            marketTargetDTO2.setTargetName(zContractsFactor.getFactorName());
                            marketTargetDTO2.setTargetUnit(zContractsFactor.getFactorUnit());
                            marketTargetDTO2.setTargetValue(zContractsFactor.getFactorValue());
                            childMarket2.add(marketTargetDTO2);
                            childMarket.setXwCode(zContractsFactor.getRegionCode());
                            childMarket.setXwName(zContractsFactor.getRegionName());
                            childMarket.setNodeCode(zContractsFactor.getMeshCode());
                            childMarket.setNodeName(zContractsFactor.getMeshName());
                        }else {
                            childMarket.setTargetList(childMarket2);
                            childMarketList.add(childMarket);
                            childMarket = new MarketTargetDTO();
                            childMarket2 = new ArrayList<>();
                            regionCode = zContractsFactor.getRegionCode();
                            MarketTargetDTO2 marketTargetDTO2 = new MarketTargetDTO2();
                            marketTargetDTO2.setIsLqTarget(zContractsFactor.getIsLqTarget());
                            marketTargetDTO2.setTargetCode(zContractsFactor.getFactorCode());
                            marketTargetDTO2.setTargetName(zContractsFactor.getFactorName());
                            marketTargetDTO2.setTargetUnit(zContractsFactor.getFactorUnit());
                            marketTargetDTO2.setTargetValue(zContractsFactor.getFactorValue());
                            childMarket2.add(marketTargetDTO2);
                            childMarket.setXwCode(zContractsFactor.getRegionCode());
                            childMarket.setXwName(zContractsFactor.getRegionName());
                            childMarket.setNodeCode(zContractsFactor.getMeshCode());
                            childMarket.setNodeName(zContractsFactor.getMeshName());
                        }
                    }
                    childMarket.setTargetList(childMarket2);
                    childMarketList.add(childMarket);
                }
                childTargetDTO.setChildMarketList(childMarketList);
                //8.查询子链群的产品目标
                List<ContractProductDTO> childProductList = contractsProductDao.selectProductByContractId(childId);
                childTargetDTO.setChildProductList(childProductList);
                //9.查询主链群的资源类型的最小作战单元个数
                List<ContractXwType3DTO> childXwType3List = xwtype3Dao.selectXwType3ByContractId(child.getChainCode(),childId);
                childTargetDTO.setChildXwType3List(childXwType3List);
                childrenList.add(childTargetDTO);
            }
            dto.setChildren(childrenList);
        }

        return dto;
    }

    @Override
    public void exportMarket(String chainCode,HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ZNodeTargetPercentInfo> list = nodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>().eq("lq_code",chainCode).isNull("share_percent"));
        Workbook workbook = new HSSFWorkbook();
        ExcelUtil.buildSheet(workbook, "中心", list, TEMPLATE_TITLE_MARKET);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        workbook.write(bot);
        ExcelUtil.export(request,response,workbook,"中心.xls");

    }

    @Override
    public List<MarketTargetDTO> getMarketTargetListByExcel(InputStream inputStream, String fileName) throws Exception{

        List list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(inputStream, fileName);
        if (null == work) {
            throw new RException("Excle工作簿为空",Constant.CODE_VALIDFAIL);
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        sheet = work.getSheetAt(0);
        if (sheet == null) {
            throw new RException("Excle工作簿为空",Constant.CODE_VALIDFAIL);
        }
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
             row = sheet.getRow(j);
            if (row == null ) {
                continue;
            }
            if(row.getFirstCellNum() == j){
                String title1 = row.getCell(0)==null?"":row.getCell(0).getStringCellValue();
                String title2 = row.getCell(1)==null?"":row.getCell(1).getStringCellValue();
                String title3 = row.getCell(2)==null?"":row.getCell(2).getStringCellValue();
                String title4 = row.getCell(3)==null?"":row.getCell(3).getStringCellValue();
                String title5 = row.getCell(4)==null?"":row.getCell(4).getStringCellValue();
                String title6 = row.getCell(5)==null?"":row.getCell(5).getStringCellValue();
                if("中心编码".equals(title1)&&"中心名称".equals(title2)&&"最小作战单元编码".equals(title3)&&"最小作战单元名称".equals(title4)&&"收入(万元)".equals(title5)&&"高端占比(%)".equals(title6)){
                    continue;
                }else {
                    throw new RException("请先下载模板，再上传",Constant.CODE_VALIDFAIL);
                }
            }
            MarketTargetDTO3 marketTargetDTO3 = new MarketTargetDTO3();
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                if(cell != null){
                    if(y==0) marketTargetDTO3.setXwCode(cell.getStringCellValue());
                    if(y==1) marketTargetDTO3.setXwName(cell.getStringCellValue());
                    if(y==2) marketTargetDTO3.setNodeCode(cell.getStringCellValue());
                    if(y==3) marketTargetDTO3.setNodeName(cell.getStringCellValue());
                    if(y==4){
                        if(cell.getCellTypeEnum().equals(CellType.STRING)){
                            throw new RException("第"+(j+1)+"行第"+(y+1)+"列，请填写数字，不需要单位",Constant.CODE_VALIDFAIL);
                        }else {
                            BigDecimal income = BigDecimal.valueOf(cell.getNumericCellValue());
                            if(income.compareTo(BigDecimal.ZERO)==-1 ){
                                throw new RException("第" + (j + 1) + "行第" + (y + 1) + "列，收入不能小于0", Constant.CODE_VALIDFAIL);
                            }
                            marketTargetDTO3.setIncome(income.setScale(0,BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    if(y==5) {
                        if(cell.getCellTypeEnum().equals(CellType.STRING)) {
                            throw new RException("第" + (j + 1) + "行第" + (y + 1) + "列，请填写数字，不需要单位", Constant.CODE_VALIDFAIL);
                        }else {
                            BigDecimal high = BigDecimal.valueOf(cell.getNumericCellValue());
                            if(high.compareTo(BigDecimal.ZERO)==-1 || high.compareTo(BigDecimal.valueOf(100))==1){
                                throw new RException("第" + (j + 1) + "行第" + (y + 1) + "列，高端占比要介于0和100之间", Constant.CODE_VALIDFAIL);
                            }
                            marketTargetDTO3.setHigh(high.setScale(2,BigDecimal.ROUND_HALF_UP));
                        }

                    }
                }
            }
            list.add(marketTargetDTO3);
        }

        work.close();
        return list;
    }

    @Override
    public List<ZContracts> selectHomePageContract(QueryContractListDTO2 queryDTO) {
        List<ZContracts> list = new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        List<ZContracts> list2 = contractsDao.selectHomePageContract(queryDTO);
        //查当前月份的时候，链群主假数据
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        //1.查询链群主的链群
        List<ZHrChainInfo> chainList = hrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("master_code",sysUser.getEmpSn()));
        //2.查询每个链群是否举了下个月的单，未举单的链群产生假数据
        if(null != chainList && chainList.size() > 0){
            for(ZHrChainInfo chainInfo : chainList){
                if(null != chainInfo.getParentCode() && !"0".equals(chainInfo.getParentCode()) && !"".equals(chainInfo.getParentCode())) continue;
                QueryContractListDTO dto = new QueryContractListDTO();
                dto.setChainCode(chainInfo.getChainCode());
                int next = DateFormatUtil.getMonthOfDate(new Date())+1;
                int year = DateFormatUtil.getYearOfDate(new Date());
                String nextMonth = "";
                nextMonth = next<10?year+"0"+next:year+""+next;
                dto.setNextMonth(nextMonth);
                List<ZContracts> contractsList = contractsDao.selectContractList(dto);
                if(null == contractsList || contractsList.size()==0){
                    ZContracts zContracts = new ZContracts();
                    zContracts.setContractName(chainInfo.getChainName()+"-"+chainInfo.getMasterName());
                    //从z_waring_period_config表中查询举单开始日期
                    Date begin = contractsDao.selectGamnlingBeginDate(chainInfo.getChainCode());
                    int day = 0;
                    if(begin != null){
                        day = DateFormatUtil.getDAYOfDate(begin);
                    }else {
                        day = 20;
                    }
                    if(DateFormatUtil.getDAYOfDate(new Date())<day){
                        zContracts.setStatus("close");
                    }else{
                        zContracts.setStatus("open");
                    }
                    list.add(zContracts);
                }
            }
        }
        list.addAll(list2);
        return list;
    }

    @Override
    public void exportProductSeries(String chainCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        List<ZProductChain> list = productChainDao.selectSeriesByChainCode(chainCode,sf.format(new Date()));

        Workbook workbook = new HSSFWorkbook();
        ExcelUtil.buildSheet(workbook, "产品系列", list, TEMPLATE_TITLE_PRODUCT);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        workbook.write(bot);
        ExcelUtil.export(request,response,workbook,"产品系列.xls");
    }

    @Override
    public List<ContractProductDTO> getProductSeriesListByExcel(InputStream inputStream, String fileName) throws Exception{
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        List list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(inputStream, fileName);
        if (null == work) {
            throw new RException("Excle工作簿为空",Constant.CODE_VALIDFAIL);
        }
        sheet = work.getSheetAt(0);
        if (sheet == null) {
            throw new RException("Excle工作簿为空",Constant.CODE_VALIDFAIL);
        }
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null ) {
                continue;
            }
            if(row.getFirstCellNum() == j){
                String title1 = row.getCell(0)==null?"":row.getCell(0).getStringCellValue();
                String title2 = row.getCell(1)==null?"":row.getCell(1).getStringCellValue();
                String title3 = row.getCell(2)==null?"":row.getCell(2).getStringCellValue();
                if("系列名称".equals(title1)&&"年度销量(台)".equals(title2)&&"月度销量(台)".equals(title3)){
                    continue;
                }else {
                    throw new RException("请先下载模板，再上传",Constant.CODE_VALIDFAIL);
                }
            }
            ContractProductDTO dto = new ContractProductDTO();
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                if(cell != null){
                    if(y==0) dto.setProductSeries(cell.getStringCellValue());
                    if(y==1){
                        if(cell.getCellTypeEnum().equals(CellType.STRING)){
                            throw new RException("第"+(j+1)+"行第"+(y+1)+"列，请填写数字，不需要单位",Constant.CODE_VALIDFAIL);
                        }else {
                            BigDecimal qty = BigDecimal.valueOf(cell.getNumericCellValue());
                            if(qty.compareTo(BigDecimal.ZERO)==-1 || qty.compareTo(new BigDecimal(qty.intValue()))==1){
                                throw new RException("第" + (j + 1) + "行第" + (y + 1) + "列，销量必须为大于等于零的整数", Constant.CODE_VALIDFAIL);
                            }
                            //dto.setQtyYear(Integer.valueOf((int) cell.getNumericCellValue()));
                        }
                    }
                    if(y==2){
                        if(cell.getCellTypeEnum().equals(CellType.STRING)){
                            throw new RException("第"+(j+1)+"行第"+(y+1)+"列，请填写数字，不需要单位",Constant.CODE_VALIDFAIL);
                        }else {
                            BigDecimal qty = BigDecimal.valueOf(cell.getNumericCellValue());
                            if(qty.compareTo(BigDecimal.ZERO)==-1 || qty.compareTo(new BigDecimal(qty.intValue()))==1){
                                throw new RException("第" + (j + 1) + "行第" + (y + 1) + "列，销量必须为大于零的整数", Constant.CODE_VALIDFAIL);
                            }
                            //dto.setQtyMonth(Integer.valueOf((int) cell.getNumericCellValue()));
                        }
                    }
                }
            }
            list.add(dto);
        }

        work.close();
        return list;
    }

    @Override
    public void saveGamblingNew(SaveGamblingContractDTO dto) throws Exception{


        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu currentUser = sysUser.getMinbu();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMM");
        //1.保存链群主抢单信息到合同主表
        ZContracts contracts = new ZContracts();
        if(null == dto.getId() || dto.getId() == 0){
            //ID 为空时为新增
            BeanUtils.copyProperties(dto,contracts);
            contracts.setStartDate(sf.parse(dto.getStartDate()));
            contracts.setEndDate(sf.parse(dto.getEndDate()));
            contracts.setJoinTime(sf.parse(dto.getJoinTime()));
            contracts.setCheckTime(sf.parse(dto.getCheckTime()));
            contracts.setContractType("10");
            if("1".equals(dto.getIsDraft())){
                contracts.setStatus("9");
            }else{
                contracts.setStatus("0");
            }
            contracts.setCreateName(sysUser.getEmpName());
            contracts.setCreateCode(sysUser.getEmpSn());
            contracts.setCreateTime(new Date());
            contracts.setXiaoweiCode(currentUser.getXwCode());
            contracts.setOrgName(currentUser.getLittleXwName());
            contracts.setOrgCode(currentUser.getLittleXwCode());
            contracts.setContractName(dto.getContractName()+"-"+sysUser.getEmpName());
            contractsDao.insert(contracts);
        }else{
            //ID 不为0时，为修改
            contracts = contractsDao.selectByContractId(dto.getId());
            contracts.setShareSpace(dto.getShareSpace());
            contracts.setStartDate(sf.parse(dto.getStartDate()));
            contracts.setEndDate(sf.parse(dto.getEndDate()));
            contracts.setJoinTime(sf.parse(dto.getJoinTime()));
            contracts.setCheckTime(sf.parse(dto.getCheckTime()));
            if("1".equals(dto.getIsDraft())){
                contracts.setStatus("9");
            }else{
                contracts.setStatus("0");
            }
            contractsDao.updateById(contracts);
            //修改时删除原有目标
            factorDao.delete(new QueryWrapper<ZContractsFactor>().eq("contract_id",dto.getId()));
            contractsProductDao.delete(new QueryWrapper<ZContractsProduct>().eq("contract_id",dto.getId()));
            xwtype3Dao.delete(new QueryWrapper<ZContractsXwType3>().eq("contract_id",dto.getId()));

        }
        //2.保存链群目标到目标表
        List<ChainGroupTargetDTO> chainGroupTargetList = dto.getChainGroupTargetList();
        for(ChainGroupTargetDTO chainGroupTarget:chainGroupTargetList){
            if(null==chainGroupTarget.getGrab()) continue;
            if(null == chainGroupTarget.getE2E() || null == chainGroupTarget.getBottom()) throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            ZContractsFactor factor1 = new ZContractsFactor();
            factor1.setContractId(contracts.getId());
            factor1.setFactorValue(chainGroupTarget.getBottom()+"");
            factor1.setFactorCode(chainGroupTarget.getTargetCode());
            factor1.setFactorName(chainGroupTarget.getTargetName());
            factor1.setFactorType(Constant.FactorType.Bottom.getValue());
            factor1.setFactorUnit(chainGroupTarget.getTargetUnit());
            factor1.setIsLqTarget(chainGroupTarget.getIsLqTarget());
            factorDao.insert(factor1);
            factor1.setId(null);
            factor1.setFactorValue(chainGroupTarget.getE2E()+"");
            factor1.setFactorType(Constant.FactorType.E2E.getValue());
            factor1.setIsLqTarget(chainGroupTarget.getIsLqTarget());
            factorDao.insert(factor1);
            factor1.setId(null);
            factor1.setFactorValue(chainGroupTarget.getGrab()+"");
            factor1.setFactorType(Constant.FactorType.Grab.getValue());
            factor1.setIsLqTarget(chainGroupTarget.getIsLqTarget());
            factorDao.insert(factor1);
        }
        //3.保存主链群市场目标到目标表
        List<MarketTargetDTO> marketTargetList = dto.getMarketTargetList();
        if(null != marketTargetList){
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
                    factor.setMeshName(marketTarget.getNodeName());
                    factor.setMeshCode(marketTarget.getNodeCode());
                    factor.setIsLqTarget(dto2.getIsLqTarget());
                    factorDao.insert(factor);
                }
            }
        }

        //4 保存主链群产品系列目标到合约产品表
        List<ContractProductDTO> productList = dto.getProductList();
        if(null != productList){
            for (ContractProductDTO productDTO : productList){
                ZContractsProduct contractsProduct = new ZContractsProduct();
                contractsProduct.setContractId(contracts.getId());
                contractsProduct.setProductSeries(productDTO.getProductSeries());
                contractsProduct.setSceneName(productDTO.getSceneName());
                List<ProductTargetDTO>  targetList = productDTO.getTargetList();
                for(ProductTargetDTO targetDTO:targetList){
                    contractsProduct.setQtyMonth(targetDTO.getQtyMonth());
                    contractsProduct.setQtyYear(targetDTO.getQtyYear());
                    contractsProduct.setTargetUnit(targetDTO.getTargetUnit());
                    contractsProduct.setTargetName(targetDTO.getTargetName());
                    contractsProduct.setTargetCode(targetDTO.getTargetCode());
                    contractsProduct.setPeriodCode(sf2.format(sf.parse(dto.getStartDate())));
                    contractsProduct.setChainCode(dto.getChainCode());
                    contractsProduct.setChainName(dto.getContractName());
                    contractsProductDao.insert(contractsProduct);
                }
            }
        }
        //5.保存主链群资源类型下的最小作战单元数量
        List<ContractXwType3DTO> xwType3List = dto.getXwType3List();
        if(null != xwType3List){
            for (ContractXwType3DTO xwType3DTO : xwType3List){
                ZContractsXwType3 xwType3 = new ZContractsXwType3();
                xwType3.setContractId(contracts.getId());
                xwType3.setNodeNumber(xwType3DTO.getInputNumber());
                xwType3.setXwType3(xwType3DTO.getXwType3());
                xwType3.setXwType3Code(xwType3DTO.getXwType3Code());
                xwtype3Dao.insert(xwType3);
            }
        }
        //6.保存子链群合约
        List<ChildTargetDTO> children = dto.getChildren();
        if(null != children && children.size()>0){
            for(ChildTargetDTO child:children){
                ZContracts childContracts = new ZContracts();
                if(null == child.getId() || child.getId() == 0){
                    //ID 为空时为新增
                    childContracts.setParentId(contracts.getId());
                    childContracts.setShareSpace(child.getShareSpace());
                    childContracts.setChainCode(child.getChildChainCode());
                    childContracts.setStartDate(sf.parse(dto.getStartDate()));
                    childContracts.setEndDate(sf.parse(dto.getEndDate()));
                    childContracts.setJoinTime(sf.parse(dto.getJoinTime()));
                    childContracts.setCheckTime(sf.parse(dto.getCheckTime()));
                    childContracts.setContractType("10");
                    childContracts.setStatus("0");
                    childContracts.setCreateName(sysUser.getEmpName());
                    childContracts.setCreateCode(sysUser.getEmpSn());
                    childContracts.setCreateTime(new Date());
                    childContracts.setXiaoweiCode(currentUser.getXwCode());
                    childContracts.setOrgName(currentUser.getLittleXwName());
                    childContracts.setOrgCode(currentUser.getLittleXwCode());
                    childContracts.setContractName(child.getChildChainName()+"-"+sysUser.getEmpName());
                    contractsDao.insert(childContracts);
                }else{
                    //ID 不为0时，为修改
                    childContracts = contractsDao.selectByContractId(child.getId());
                    childContracts.setShareSpace(child.getShareSpace());
                    contractsDao.updateById(childContracts);
                    //修改时删除原有目标
                    contractsProductDao.delete(new QueryWrapper<ZContractsProduct>().eq("contract_id",child.getId()));
                    factorDao.delete(new QueryWrapper<ZContractsFactor>().eq("contract_id",child.getId()));
                    xwtype3Dao.delete(new QueryWrapper<ZContractsXwType3>().eq("contract_id",child.getId()));

                }
                //7.保存子链群的链群目标
                List<ChainGroupTargetDTO> childTargetList = child.getChildTargetList();
                for(ChainGroupTargetDTO childTarget:childTargetList){
                    if(null==childTarget.getGrab()) continue;
                    if(null == childTarget.getE2E() || null == childTarget.getBottom()) throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
                    ZContractsFactor factor1 = new ZContractsFactor();
                    factor1.setIsLqTarget(childTarget.getIsLqTarget());
                    factor1.setContractId(childContracts.getId());
                    factor1.setFactorValue(childTarget.getBottom()+"");
                    factor1.setFactorCode(childTarget.getTargetCode());
                    factor1.setFactorName(childTarget.getTargetName());
                    factor1.setFactorType(Constant.FactorType.Bottom.getValue());
                    factor1.setFactorUnit(childTarget.getTargetUnit());
                    factorDao.insert(factor1);
                    factor1.setId(null);
                    factor1.setFactorValue(childTarget.getE2E()+"");
                    factor1.setFactorType(Constant.FactorType.E2E.getValue());
                    factor1.setIsLqTarget(childTarget.getIsLqTarget());
                    factorDao.insert(factor1);
                    factor1.setId(null);
                    factor1.setFactorValue(childTarget.getGrab()+"");
                    factor1.setFactorType(Constant.FactorType.Grab.getValue());
                    factor1.setIsLqTarget(childTarget.getIsLqTarget());
                    factorDao.insert(factor1);
                }
                //8.保存子链群的市场目标
                List<MarketTargetDTO> chilidMarketList = child.getChildMarketList();
                if(null != chilidMarketList){
                    for (MarketTargetDTO chilidMarket : chilidMarketList){
                        List<MarketTargetDTO2> dto2List = chilidMarket.getTargetList();
                        for(MarketTargetDTO2 dto2:dto2List){
                            ZContractsFactor factor = new ZContractsFactor();
                            factor.setIsLqTarget(dto2.getIsLqTarget());
                            factor.setContractId(childContracts.getId());
                            factor.setFactorValue(dto2.getTargetValue());
                            factor.setFactorCode(dto2.getTargetCode());
                            factor.setFactorName(dto2.getTargetName());
                            factor.setFactorType(Constant.FactorType.Grab.getValue());
                            factor.setFactorUnit(dto2.getTargetUnit());
                            factor.setRegionCode(chilidMarket.getXwCode());
                            factor.setRegionName(chilidMarket.getXwName());
                            factor.setMeshCode(chilidMarket.getNodeCode());
                            factor.setMeshName(chilidMarket.getNodeName());
                            factorDao.insert(factor);
                        }
                    }
                }
                //9.保存子链群的产品目标
                List<ContractProductDTO> childProductList = child.getChildProductList();
                if(null != childProductList){
                    for (ContractProductDTO chilidProduct : childProductList){
                        ZContractsProduct contractsProduct = new ZContractsProduct();
                        contractsProduct.setProductSeries(chilidProduct.getProductSeries());
                        contractsProduct.setContractId(childContracts.getId());
                        contractsProduct.setSceneName(chilidProduct.getSceneName());
                        List<ProductTargetDTO>  targetList = chilidProduct.getTargetList();
                        for(ProductTargetDTO targetDTO:targetList){
                            contractsProduct.setTargetUnit(targetDTO.getTargetUnit());
                            contractsProduct.setTargetName(targetDTO.getTargetName());
                            contractsProduct.setTargetCode(targetDTO.getTargetCode());
                            contractsProduct.setQtyMonth(targetDTO.getQtyMonth());
                            contractsProduct.setQtyYear(targetDTO.getQtyYear());
                            contractsProduct.setPeriodCode(sf2.format(sf.parse(dto.getStartDate())));
                            contractsProduct.setChainCode(dto.getChainCode());
                            contractsProduct.setChainName(dto.getContractName());
                            contractsProductDao.insert(contractsProduct);
                        }
                    }
                }
                //10.保存子链群资源类型下的最小作战单元数量
                List<ContractXwType3DTO> childXwType3List = child.getChildXwType3List();
                if(null != childXwType3List){
                    for (ContractXwType3DTO xwType3DTO : childXwType3List){
                        ZContractsXwType3 xwType3 = new ZContractsXwType3();
                        xwType3.setNodeNumber(xwType3DTO.getInputNumber());
                        xwType3.setContractId(childContracts.getId());
                        xwType3.setXwType3(xwType3DTO.getXwType3());
                        xwType3.setXwType3Code(xwType3DTO.getXwType3Code());
                        xwtype3Dao.insert(xwType3);
                    }
                }
            }
        }
    }

    @Override
    public List<ContractXwType3DTO> selectXwType3(String chainCode) {
        List<ContractXwType3DTO> xwType3DTOList = nodeTargetPercentInfoDao.selectXwType3ListByChainCode(chainCode);
        return xwType3DTOList;
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
            throw new RException("请上传excle文件",Constant.CODE_VALIDFAIL);
        }
        return workbook;
    }


    private static final ExcelUtil.CellHeadField[] TEMPLATE_TITLE_MARKET = {
            new ExcelUtil.CellHeadField("中心编码", "xwCode"),
            new ExcelUtil.CellHeadField("中心名称", "xwName"),
            new ExcelUtil.CellHeadField("最小作战单元编码", "nodeCode"),
            new ExcelUtil.CellHeadField("最小作战单元名称", "nodeName"),
            new ExcelUtil.CellHeadField("收入(万元)", "income"),
            new ExcelUtil.CellHeadField("高端占比(%)", "high")
    };

    private static final ExcelUtil.CellHeadField[] TEMPLATE_TITLE_PRODUCT = {
            new ExcelUtil.CellHeadField("系列名称", "productSeries"),
            new ExcelUtil.CellHeadField("年度销量(台)", ""),
            new ExcelUtil.CellHeadField("月度销量(台)", "")
    };



}
