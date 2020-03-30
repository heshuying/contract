package com.haier.hailian.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.dao.ZContractsFactorDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DigitalUtils;
import com.haier.hailian.contract.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class TargetReachServiceImpl implements com.haier.hailian.contract.service.TargetReachService {

    private static final ExcelUtil.CellHeadField[] TEMPLATE_TARGET_REACH = {
            new ExcelUtil.CellHeadField("单名称", "factorName"),
            new ExcelUtil.CellHeadField("单位", "factorUnit"),
            new ExcelUtil.CellHeadField("正负项", "factorDirecton"),
            new ExcelUtil.CellHeadField("计算逻辑", "computeLogic"),
            new ExcelUtil.CellHeadField("最小作战单元", "nodeName"),
            new ExcelUtil.CellHeadField("分享比例", "sharePercent"),
            new ExcelUtil.CellHeadField("抢单目标", "factorValue"),
            new ExcelUtil.CellHeadField("实际达成", "factorValueActual")
    };

    @Autowired
    ZContractsFactorDao factorDao;
    @Autowired
    ZContractsFactorService factorService;
    @Autowired
    ZContractsDao contractsDao;
    @Autowired
    ZHrChainInfoDao chainInfoDao;

    @Override
    public List<ZContracts> selectContractListForTarget(QueryContractListDTO queryDTO) {
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        queryDTO.setUserCode(sysUser.getEmpSn());
        queryDTO.setParentId(0);
        List<ZContracts> list = contractsDao.selectContractListForTarget(queryDTO);
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
    public List<TargetListResDTO> getFactorGrabList(String contractId){
        List<TargetListResDTO> resultList = new ArrayList<>();

        ZContracts contracts = contractsDao.selectById(Integer.parseInt(contractId));
        if(contracts == null){
            return new ArrayList<>();
        }
        ZHrChainInfo chain = chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));

        Map<String,Object> map = new HashMap<>();
        map.put("contractId", contractId);
        map.put("chainCode", chain.getChainCode());
        map.put("ptCode", chain.getChainPtCode());
        List<FactorGrabResDTO> list = factorDao.getFactorGrabList(map);
        for(FactorGrabResDTO item : list){
            TargetListResDTO target = new TargetListResDTO();
            BeanUtils.copyProperties(item, target);
            if(!resultList.contains(target)){
                target.getGrabList().add(item);
                resultList.add(target);
            }else{
                target = resultList.get(resultList.indexOf(target));
                target.getGrabList().add(item);
            }

        }
        return resultList;
    }

    @Override
    @Transactional
    public void saveTargetActual(TargetReachSaveReqDTO data){
        List<ZContractsFactor> factors = new ArrayList<>();
        if(data.getTargetList() == null || data.getTargetList().isEmpty()){
            return;
        }

        for(FactorGrabResDTO item : data.getTargetList()){
            ZContractsFactor factor = new ZContractsFactor();
            factor.setId(Integer.parseInt(item.getFactId()));
            factor.setFactorValueActual(item.getFactorValueActual());
            factors.add(factor);
        }
        factorService.updateBatchById(factors);

        ZContracts contracts = new ZContracts();
        contracts.setId(Integer.parseInt(data.getContractId()));
        contracts.setTargetUpdateTime(new Date());
        contractsDao.updateById(contracts);
    }

    @Override
    public void templetDownload(String contractId, HttpServletRequest request, HttpServletResponse response, boolean isTemplet) throws IOException {
        List<TargetListResDTO> resultList = new ArrayList<>();

        ZContracts contracts = contractsDao.selectById(Integer.parseInt(contractId));
        if(contracts == null){
            return;
        }
        ZHrChainInfo chain = chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>().eq("chain_code", contracts.getChainCode()));

        Map<String,Object> map = new HashMap<>();
        map.put("contractId", contractId);
        map.put("chainCode", chain.getChainCode());
        map.put("ptCode", chain.getChainPtCode());
        List<FactorGrabResDTO> list = factorDao.getFactorGrabList(map);
        for(FactorGrabResDTO item : list){
            TargetListResDTO target = new TargetListResDTO();
            BeanUtils.copyProperties(item, target);
            if(!resultList.contains(target)){
                target.getGrabList().add(item);
                resultList.add(target);
            }else{
                target = resultList.get(resultList.indexOf(target));
                target.getGrabList().add(item);
            }

        }

        Workbook workbook = new HSSFWorkbook();
        buildSheet(workbook, "目标达成", resultList, list, isTemplet, TEMPLATE_TARGET_REACH);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        workbook.write(bot);
        ExcelUtil.export(request,response,workbook,"目标达成.xls");
    }

    private Sheet buildSheet(Workbook workbook, String sheetName, List<TargetListResDTO> data, List<FactorGrabResDTO> list, boolean isTemplet, final ExcelUtil.CellHeadField... cellFields) throws IOException {
        //行索引
        int rowIndex = 0;
        //创建表
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle headerCellStyle = ExcelUtil.getHeaderCellStyle(workbook);
        CellStyle contentCellStyle = ExcelUtil.getContentCellStyle(workbook);

        //创建 表头 行
        Row headerRow = sheet.createRow(rowIndex);
        //设置 表头 行高
        headerRow.setHeightInPoints(16.5F);
        //创建表头
        for (int i = 0; i < cellFields.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(cellFields[i].getHeadName());
            sheet.setColumnWidth(i, cellFields[i].getHeadName().getBytes().length * 2 * 256);
        }
        if(isTemplet){
            Cell dataCell = headerRow.createCell(8);
            dataCell.setCellValue(JSON.toJSONString(list));
            //隐藏
            sheet.setColumnHidden(8, true);
        }
        //首行冻结
        sheet.createFreezePane(0, 1);
        //行索引自加 1
        rowIndex++;
        //生成数据
        buildContent(rowIndex,data, isTemplet, sheet,contentCellStyle,cellFields);
        return sheet;
    }

    private void buildContent(int rowIndex, List<TargetListResDTO> data, boolean isTemplet, Sheet sheet, CellStyle contentCellStyle, ExcelUtil.CellField...cellFields){
        //判断 数据是否为空
        if (null != data && !data.isEmpty()) {
            //用于生成序号
            @SuppressWarnings("unused")
            int index = 1;
            //填充表数据
            for (final TargetListResDTO object : data) {
                //获取索引行
                Row row = sheet.getRow(rowIndex);
                //判断索引行是否已存在
                if (null == row) {
                    //创建索引行
                    row = sheet.createRow(rowIndex);
                }

                for (int i = 0; i < 4; i++) {
                    if(object.getGrabList().size() > 1){
                        CellRangeAddress cra=new CellRangeAddress(rowIndex, rowIndex+object.getGrabList().size()-1, i, i);
                        //在sheet里增加合并单元格
                        sheet.addMergedRegion(cra);
                        //设置合并单元格边框
                        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
                        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
                        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
                        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
                    }

                    final String fieldName = cellFields[i].getFieldName();
                    final Cell contentCell = row.createCell(i);
                    contentCell.setCellStyle(contentCellStyle);
                    final ExcelUtil.Converter converter = cellFields[i].getConverter();
                    ReflectionUtils.doWithFields(object.getClass(), new ReflectionUtils.FieldCallback() {
                        @Override
                        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                            if (field.getName().equals(fieldName)) {
                                field.setAccessible(true);
                                Object value = field.get(object);
                                if (converter != null){
                                    contentCell.setCellValue(converter.convert(value));
                                }else {
                                    contentCell.setCellValue(null == value ? null : value.toString());
                                }
                            }
                        }
                    });
                }

                for (final FactorGrabResDTO grab : object.getGrabList()) {
                    int end = cellFields.length;
                    for (int i = 4; i < end; i++) {
                        final String fieldName = cellFields[i].getFieldName();
                        final Cell contentCell = row.createCell(i);
                        contentCell.setCellStyle(contentCellStyle);
                        contentCell.setCellType(CellType.STRING);
                        final ExcelUtil.Converter converter = cellFields[i].getConverter();
                        ReflectionUtils.doWithFields(grab.getClass(), new ReflectionUtils.FieldCallback() {
                            @Override
                            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                                if (field.getName().equals(fieldName)) {
                                    field.setAccessible(true);
                                    Object value = field.get(grab);
                                    if (isTemplet && fieldName.equals("factorValueActual")) {
                                        value = "";
                                    }
                                    if (converter != null){
                                        contentCell.setCellValue(converter.convert(value));
                                    }else {
                                        contentCell.setCellValue(null == value ? "" : value.toString());
                                    }
                                }
                            }
                        });
                    }
                    if(isTemplet){
                        final Cell contentCell = row.createCell(8);
                        contentCell.setCellStyle(contentCellStyle);
                        contentCell.setCellValue(grab.getFactId());
                    }
                    //索引行自加 1
                    rowIndex++;
                    row = sheet.getRow(rowIndex);
                    //判断索引行是否已存在
                    if (null == row) {
                        //创建索引行
                        row = sheet.createRow(rowIndex);
                    }
                }

                //序号自加 1
                index++;
            }

        }
    }


    @Override
    public List<TargetListResDTO> getDataByExcel(InputStream inputStream, String fileName) throws Exception{
        List<TargetListResDTO> resultList = new ArrayList<>();
        List<FactorGrabResDTO> list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = this.getWorkbook(inputStream, fileName);
        if (null == work) {
            throw new RException("Excle工作簿为空", Constant.CODE_VALIDFAIL);
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
                String title7 = row.getCell(6)==null?"":row.getCell(6).getStringCellValue();
                String title8 = row.getCell(7)==null?"":row.getCell(7).getStringCellValue();
                String data = row.getCell(8).getStringCellValue();
                if("单名称".equals(title1)&&"单位".equals(title2)&&"正负项".equals(title3)&&"计算逻辑".equals(title4)
                        &&"最小作战单元".equals(title5)&&"分享比例".equals(title6)&&"抢单目标".equals(title7)&&"实际达成".equals(title8)){
                    if(StringUtils.isNotBlank(data)){
                        list = JSON.parseArray(data, FactorGrabResDTO.class);
                    }

                    if(list == null || list.isEmpty()){
                        return resultList;
                    }
                    continue;
                }else {
                    throw new RException("请先下载模板，再上传",Constant.CODE_VALIDFAIL);
                }
            }

            log.info("第" + j + "行");
            cell = row.getCell(8);
            if(cell == null){
                continue;
            }
            String factId;
            if(cell.getCellTypeEnum().equals(CellType.STRING)){
                factId = cell.getStringCellValue();
            }else {
                factId = String.valueOf(cell.getNumericCellValue());
            }
            cell = row.getCell(7);
            String factorValueActual;
            if(cell.getCellTypeEnum().equals(CellType.STRING)){
                factorValueActual = cell.getStringCellValue();
            }else {
                factorValueActual = String.valueOf(cell.getNumericCellValue());
            }
            log.info("factId:" + factId + " factorValueActual:" + factorValueActual);

            if(StringUtils.isBlank(factId)){
                throw new RException("不要修改excel模板格式");
            }
            if(StringUtils.isNotBlank(factorValueActual) && !DigitalUtils.isNumeric(factorValueActual)){
                log.error("目标达成只能填写数字:" + factorValueActual);
                throw new RException("目标达成只能填写数字");
            }
            // 填充实际达成
            list.stream().filter(s -> s.getFactId().equals(factId)).findFirst().get().setFactorValueActual(factorValueActual);

        }

        for(FactorGrabResDTO item : list){
            TargetListResDTO target = new TargetListResDTO();
            BeanUtils.copyProperties(item, target);
            if(!resultList.contains(target)){
                target.getGrabList().add(item);
                resultList.add(target);
            }else{
                target = resultList.get(resultList.indexOf(target));
                target.getGrabList().add(item);
            }

        }
        work.close();
        return resultList;
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
}
