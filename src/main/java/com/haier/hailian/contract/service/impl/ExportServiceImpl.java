package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.ExportChainUnitInfo;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.service.ExportService;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExportServiceImpl implements ExportService {


    @Override
    public void export(String fileName , String SheetName , ExcelUtil.CellHeadField[] headFields , List rows, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        ExcelUtil.buildSheet(workbook, SheetName, rows, headFields);
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        workbook.write(bot);
        ExcelUtil.export(request,response,workbook,fileName);
    }


    @Override
    public List<ExportChainUnitInfo> importExcel(InputStream inputStream, String fileName) throws Exception {
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
                if (row == null) {
                    continue;
                }
                if(row.getFirstCellNum() == j){
                    String title1 = row.getCell(0)==null?"":row.getCell(0).getStringCellValue();
                    String title2 = row.getCell(1)==null?"":row.getCell(1).getStringCellValue();
                    String title3 = row.getCell(2)==null?"":row.getCell(2).getStringCellValue();
                    if("单元".equals(title1)&&"小微".equals(title2)&&"分享比例（100%）".equals(title3)){
                        continue;
                    }else {
                        throw new RException("请先下载模板，再上传", Constant.CODE_VALIDFAIL);
                    }
                }
                ExportChainUnitInfo exportChainUnitInfo = new ExportChainUnitInfo();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    if(y==0) exportChainUnitInfo.setXwName(cell.getStringCellValue());
                    if(y==1) exportChainUnitInfo.setLittleXwName(cell.getStringCellValue());
                    if(y==2) exportChainUnitInfo.setSharePercent(BigDecimal.valueOf(cell.getNumericCellValue()));
                }
                list.add(exportChainUnitInfo);
            }
        }
        work.close();
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


}
