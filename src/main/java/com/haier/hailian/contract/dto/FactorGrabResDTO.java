package com.haier.hailian.contract.dto;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Data
public class FactorGrabResDTO {
    private String factId;
    private String grabId;
    private String factorCode;
    private String factorName;
    private String factorValue;
    private String factorUnit;
    private String factorDirecton;
    private String factorType;
    private String nodeName;
    private String nodeCode;
    // 计算逻辑
    private String computeLogic;

    private String targetXwCategoryCode;
    private String factorValueActual;
    private String sharePercent;

    public static void main(String[] args) throws IOException {
        FileOutputStream fos=new FileOutputStream("D:\\13.xls");

        Workbook wb=new HSSFWorkbook();

        Sheet sheet=wb.createSheet();
        /*
         * 设定合并单元格区域范围
         *  firstRow  0-based
         *  lastRow   0-based
         *  firstCol  0-based
         *  lastCol   0-based
         */
        CellRangeAddress cra=new CellRangeAddress(0, 3, 3, 9);

        //在sheet里增加合并单元格
        sheet.addMergedRegion(cra);

        Row row = sheet.createRow(0);

        Cell cell_1 = row.createCell(3);

        cell_1.setCellValue("When you're right , no one remembers, when you're wrong ,no one forgets .");

        //cell 位置3-9被合并成一个单元格，不管你怎样创建第4个cell还是第5个cell…然后在写数据。都是无法写入的。
        Cell cell_2 = row.createCell(10);

        cell_2.setCellValue("what's up ! ");

        Row row1 = sheet.createRow(1);
        Cell cell1 = row1.createCell(1);
        cell1.setCellValue("第1行第1列");
        Cell cell3 = row1.createCell(3);
        cell3.setCellValue("第1行第3列");
        sheet.setColumnHidden(1, true);
        wb.write(fos);

        fos.close();
    }

}
