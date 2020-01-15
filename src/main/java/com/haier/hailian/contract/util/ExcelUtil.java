package com.haier.hailian.contract.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 01439613
 */
public class ExcelUtil {
    /**
     * 两位小数
     */
    private static String regTwoNumber = "[0-9]*(\\.?)[0-9]{1,2}";
    /**
     * 三位小数
     */
    private static String regThreeNumber = "[0-9]*(\\.?)[0-9]{1,3}";
    /**
     * 11位正整数
     */
    private static String regInteger = "[0-9]{1,11}";
    /**
     * 联系方式
     */
    private static String regTel = "([0-9]|-){1,20}";
    /**
     * 创建表头
     * @param headerRow
     * @param headerCellStyle
     * @param headerNames
     */
    public static void CreateHeaderCells(Sheet sheet, Row headerRow, CellStyle headerCellStyle, String... headerNames) {
        if (null != headerNames && headerNames.length > 0) {
            CellValue[] cellValues = new CellValue[headerNames.length];
            for (int i = 0; i < headerNames.length; i++) {
                cellValues[i] = new CellValue(i, headerNames[i]);
            }
            CreateHeaderCells(sheet,headerRow, headerCellStyle, cellValues);
        }
    }

    /**
     * 创建表头
     * @param headerRow
     * @param headerCellStyle
     * @param cellValues
     */
    public static void CreateHeaderCells(Sheet sheet, Row headerRow, CellStyle headerCellStyle, CellValue... cellValues) {
        if (null != cellValues && cellValues.length > 0) {
            for (CellValue cellValue : cellValues) {
                Cell headerCell = headerRow.createCell(cellValue.getCellIndex());
                headerCell.setCellStyle(headerCellStyle);
                headerCell.setCellValue(cellValue.getValue());
                sheet.setColumnWidth(cellValue.getCellIndex(), cellValue.getValue().getBytes().length * 2 * 256);
            }
        }
    }

    public static void CreateCells(Row contentRow, CellStyle cellStyle, Object... cellValues) {
        if (null != cellValues && cellValues.length > 0) {
            CellValue[] cellValueArray = new CellValue[cellValues.length];
            for (int i = 0; i < cellValues.length; i++) {
                cellValueArray[i] = new CellValue(i, cellValues[i]);
            }
            CreateCells(contentRow, cellStyle, cellValueArray);
        }
    }

    /**
     * 创建列
     * @param contentRow
     * @param cellStyle
     * @param cellValues
     */
    public static void CreateCells(Row contentRow, CellStyle cellStyle, CellValue... cellValues) {
        if (null != cellValues && cellValues.length > 0) {
            for (CellValue cellValue : cellValues) {
                Cell contentCell = contentRow.createCell(cellValue.getCellIndex());
                contentCell.setCellStyle(cellStyle);
                contentCell.setCellValue(cellValue.getValue());
            }
        }
    }

    /**
     * 合并单元格
     * @param sheet
     * @param rowIndex
     * @param rowSize
     * @param cellIndexs
     */
    public static void AddMergedRegion(Sheet sheet, int rowIndex, int rowSize, int... cellIndexs) {
        if (null != cellIndexs && cellIndexs.length > 0) {
            for (int i = 0; i < cellIndexs.length; i++) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowIndex, rowIndex + rowSize, cellIndexs[i], cellIndexs[i]);
                sheet.addMergedRegion(cellRangeAddress);
                //设置合并单元格边框
                RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
            }
        }
    }

    /**
     * 生成sheet 简单表头
     * @param workbook
     * @param sheetName
     * @param data
     * @param cellFields
     * @return
     * @throws IOException
     */
    public static Sheet buildSheet(Workbook workbook, String sheetName, List<?> data, final CellHeadField... cellFields) throws IOException {
        //行索引
        int rowIndex = 0;
        //创建表
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle headerCellStyle = getHeaderCellStyle(workbook);
        CellStyle contentCellStyle = getContentCellStyle(workbook);

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
        //首行冻结
        sheet.createFreezePane(0, 1);
        //行索引自加 1
        rowIndex++;
        //生成数据
        buildContent(rowIndex,data,sheet,contentCellStyle,cellFields);
        return sheet;
    }

    /**
     * 生成sheet 多层表头
     * @param hssfWorkbook
     * @param sheetName
     * @param data
     * @param cellHeadss
     * @param cellFields
     * @return
     * @throws IOException
     */
    public static Sheet buildSheet(HSSFWorkbook hssfWorkbook, String sheetName, List<?> data, final CellHead[][] cellHeadss, final CellField... cellFields) throws IOException {
        //行索引
        int rowIndex = 0;
        //创建表
        Sheet sheet = hssfWorkbook.createSheet(sheetName);
        CellStyle headerCellStyle = getHeaderCellStyle(hssfWorkbook);
        CellStyle contentCellStyle = getContentCellStyle(hssfWorkbook);

        String[][] sss = new String[cellHeadss.length][0];
        for (int i = 0; i<cellHeadss.length; i++){
            String[] ss = sss[i];
            CellHead[] cellHeads = cellHeadss[i];
            int emptyColCount = 0;
            for (String s:ss){
                if (s == null){
                    emptyColCount++;
                }
            }
            int colCount = 0;
            for (CellHead cellHead:cellHeads){
                colCount += cellHead.getColspan();
            }
            //如果某一行的字段数比已有的空单元格要多，增加列
            if (emptyColCount<colCount){
                ss = sss[i] = Arrays.copyOf(ss,ss.length+colCount-emptyColCount);
            }
            int ii = 0;
            for (CellHead cellHead:cellHeads){
                while (true){
                    if (ss[ii] == null){
                        cellHead.setRow(i);
                        cellHead.setCol(ii);
                        for (int ri = 0; ri<cellHead.getRowspan();ri++){
                            for (int ci = 0; ci<cellHead.getColspan(); ci++){
                                //当colspan引起行数不够时增加行
                                if (i+ri+1>=sss.length){
                                    sss = Arrays.copyOf(sss,i+ri+1);
                                }
                                //当colspan引起下边的行列数不够时增加列
                                if (sss[i+ri].length<ii+cellHead.getColspan()){
                                    sss[i+ri] = Arrays.copyOf(sss[i+ri],ii+cellHead.getColspan());
                                }
                                sss[i+ri][ii+ci] = cellHead.name;
                            }
                        }
                        ii += cellHead.getColspan();
                        break;
                    }else{ ii++;}
                }
            }
        }
        //生成表头
        for (int i = 0;i<sss.length;i++){
            //创建 表头 行
            Row headerRow = sheet.createRow(rowIndex++);
            //设置 表头 行高
            headerRow.setHeightInPoints(16.5F);
            //创建表头
            for (int ii = 0;ii<sss[i].length;ii++){
                Cell headerCell = headerRow.createCell(ii);
                headerCell.setCellStyle(headerCellStyle);
                headerCell.setCellValue(sss[i][ii]);
                int columnWidth = sheet.getColumnWidth(ii);
                int columnWidthNew = sss[i][ii].getBytes().length * 2 * 256;
                if (columnWidthNew>columnWidth){
                    sheet.setColumnWidth(ii, columnWidthNew);
                }
            }
        }
        //表头合并
        for (CellHead[] cellHeads:cellHeadss){
            for(CellHead cellHead:cellHeads){
                if (cellHead.getColspan()>1 || cellHead.getRowspan()>1){
                    sheet.addMergedRegion(new CellRangeAddress(cellHead.getRow(),cellHead.getRow()+cellHead.getRowspan()-1,cellHead.getCol(),cellHead.getCol()+cellHead.getColspan()-1));
                }
            }
        }
        //表头冻结
        sheet.createFreezePane(0, sss.length);
        buildContent(rowIndex,data,sheet,contentCellStyle,cellFields);
        return sheet;
    }

    /**
     * 创建表头样式
     * @param workbook
     * @return
     */
    private static CellStyle getHeaderCellStyle(Workbook workbook){

        //创建表头字体
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        //表头样式
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        //背景色填充模式
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //背景色
        headerCellStyle.setFillForegroundColor((short) 9);
        // 居中
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return headerCellStyle;
    }

    /**
     * 创建内容样式
     * @param workbook
     * @return
     */
    private static CellStyle getContentCellStyle(Workbook workbook){

        //内容列样式
        CellStyle contentCellStyle = workbook.createCellStyle();
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        // 居中
        contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return contentCellStyle;
    }

    private static void buildContent(int rowIndex, List<?> data, Sheet sheet, CellStyle contentCellStyle, CellField...cellFields){
        //判断 数据是否为空
        if (null != data && !data.isEmpty()) {
            //用于生成序号
            @SuppressWarnings("unused")
            int index = 1;
            //填充表数据
            for (final Object object : data) {
                //获取索引行
                Row row = sheet.getRow(rowIndex);
                //判断索引行是否已存在
                if (null == row) {
                    //创建索引行
                    row = sheet.createRow(rowIndex);
                }
                for (int i = 0; i < cellFields.length; i++) {
                    final String fieldName = cellFields[i].getFieldName();
                    final Cell contentCell = row.createCell(i);
                    contentCell.setCellStyle(contentCellStyle);
                    final Converter converter = cellFields[i].getConverter();
                    if(object instanceof Map) {
                        @SuppressWarnings("rawtypes")
                        Map map = (Map) object;
                        Object value = map.get(fieldName);
                        if (null != converter) {
                            contentCell.setCellValue(converter.convert(value));
                        } else {
                            contentCell.setCellValue(null == value ? null : value.toString());
                        }
                    }else {
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
                }
                //索引行自加 1
                rowIndex++;
                //序号自加 1
                index++;
            }
        }
    }
    public static void export(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String excelName) throws IOException {
        //设置响应上下文类型
        response.setContentType("APPLICATION/OCTET-STREAM");
        //设置响应头描述
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        boolean fireFoxOrSafari = null != userAgent && (userAgent.toLowerCase().indexOf("firefox") > 0  || userAgent.toLowerCase().indexOf("safari") > 0);
        if (fireFoxOrSafari) {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(excelName.getBytes("UTF-8"), "ISO-8859-1"));
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelName, "UTF-8"));
        }
        //向响应流写数据
        workbook.write(response.getOutputStream());
        //刷新响应流
        response.getOutputStream().flush();
    }

    /**
     * 单元格列序号值
     */
    public static final class CellValue {
        /**
         * 单元格序号
         */
        private int cellIndex;
        /**
         * 单元格值
         */
        private String value;

        public CellValue(int cellIndex, Object value) {
            this.cellIndex = cellIndex;
            if (null != value) {
                this.value = value.toString();
            }
        }

        public void setCellIndex(int cellIndex) {
            this.cellIndex = cellIndex;
        }

        public int getCellIndex() {
            return cellIndex;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final class CellHead {
        private String name;
        private int rowspan;
        private int colspan;

        private int row;
        private int col;

        public CellHead(String name) {
            this.name = name;
        }

        public CellHead(String name, int rowspan, int colspan) {
            this.name = name;
            this.rowspan = rowspan;
            this.colspan = colspan;
        }

        public String getName() {
            return name;
        }

        public int getRowspan() {
            return rowspan>0?rowspan:1;
        }

        public int getColspan() {
            return colspan>0?colspan:1;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    public static class CellField{

        private String fieldName;
        private Converter converter;

        public CellField(String fieldName) {
            this.fieldName = fieldName;
        }

        public CellField(String fieldName, Converter converter) {
            this.fieldName = fieldName;
            this.converter = converter;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Converter getConverter() {
            return converter;
        }

        public void setConverter(Converter converter) {
            this.converter = converter;
        }

    }

    public static final class CellHeadField extends CellField{

        private String headName;
        private String fieldName;
        private Converter converter;

        public CellHeadField(String headName, String fieldName) {
            super(fieldName);
            this.headName = headName;
        }

        public CellHeadField(String headName, String fieldName, Converter converter) {
            super(fieldName,converter);
            this.headName = headName;
        }

        public String getHeadName() {
            return headName;
        }

        public void setHeadName(String headName) {
            this.headName = headName;
        }

    }

    public interface Converter<T> {
        /**
         * 转换器
         * @param value
         * @return
         */
        String convert(T value);

    }

    public static class DateConverter<T extends Date> implements Converter<T>{

        private SimpleDateFormat simpleDateFormat;

        public DateConverter(){
            this("yyyy-MM-dd");
        }
        public DateConverter(String format){
            this.simpleDateFormat = new SimpleDateFormat(format);
        }
        @Override
        public String convert(T value) {
            if (null == value) {
                return "";
            }
            return simpleDateFormat.format(value);
        }
    }

    public static class MapConverter<T> implements Converter<T>{

        private Map<T,String> map;
        public MapConverter(Map<T,String> map){
            this.map = map;
        }
        @Override
        public String convert(T value) {
            return map.get(value);
        }
    }


    private static String getCellValueAsDate(HSSFCell cell){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if(cell != null){
            Date date = new Date(0) ;
            if(cell.getNumericCellValue() == 0.0d) return result;
            date = HSSFDateUtil.getJavaDate( new Double( cell.getNumericCellValue()));
            result = sdf.format(date) ;
        }
        return result;
    }

}
