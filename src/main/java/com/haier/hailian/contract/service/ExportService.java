package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.ExportChainUnitInfo;
import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.util.ExcelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExportService {

    /**
     * 导出
     * @param rows
     * @param request
     * @param response
     */
    void export(String fileName , String SheetName , ExcelUtil.CellHeadField[] headFields , List rows , HttpServletRequest request, HttpServletResponse response) throws IOException;


    /**
     * 导入
     * @param inputStream
     * @param fileName
     * @return
     */
    List<ExportChainUnitInfo> importExcel(InputStream inputStream, String fileName) throws Exception;
}
