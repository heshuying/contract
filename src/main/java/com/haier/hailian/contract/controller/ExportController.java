package com.haier.hailian.contract.controller;


import com.haier.hailian.contract.dto.ExportChainUnitInfo;
import com.haier.hailian.contract.dto.ExportInfo;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.service.ExportService;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import com.haier.hailian.contract.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * 导入导出
 * @author liuyq
 * @since 2020年1月15日 14:22:46
 */
@RestController
@RequestMapping("export")
@Slf4j
@Api(value = "导入导出", tags = {"导入导出"})
public class ExportController {

    @Autowired
    private ZHrChainInfoService zHrChainInfoService;
    @Autowired
    private GrabService grabService;
    @Autowired
    private ExportService exportService;



    /**
     * 导出模板
     * params 参数类型
     * @return
     * @author liuyq 2020年1月13日 15:36:15
     */
    @ApiOperation(value = "GET导出", notes = "GET导出")
    @GetMapping(value = "/export" , headers="Accept=application/octet-stream")
    public void export(@RequestParam String type , @RequestParam Integer contractId , HttpServletRequest request, HttpServletResponse response){
        List rows = new ArrayList();
        // 文件名
        String fileName = "";
        String sheetName = "";
        ExcelUtil.CellHeadField[] headFields = new ExcelUtil.CellHeadField[0];
        switch (type){
            case "chain":
                rows = zHrChainInfoService.getPartMinbuList();
                sheetName = "链群注册";
                fileName = "链群注册模板.xls";
                headFields = CHAIN_EXCEL_TITLE;
                break;
            case "grabGrid": // 导出网格信息
                TyMasterGrabQueryDto queryDTO = new TyMasterGrabQueryDto();
                queryDTO.setContractId(contractId);
                rows = grabService.queryMeshGrabDetail(queryDTO);
                sheetName = "网格信息";
                fileName = "网格信息.xls";
                headFields = GRID_EXCEL_TITLE;
                break;
            case "masterGrab":
                System.out.println("");
                break;
            default:
                System.out.println("暂无匹配类型");
        }
        // 导出
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            exportService.export(fileName , sheetName , headFields , rows ,  request , response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }






    /**
     * 导出模板
     * params 参数类型
     * @return
     * @author liuyq 2020年1月13日 15:36:15
     */
    @ApiOperation(value = "POST导出", notes = "POST导出")
    @PostMapping(value = "/exportTemplate" , headers="Accept=application/octet-stream")
    public void exportTemplate(@RequestBody ExportInfo exportInfo, HttpServletRequest request, HttpServletResponse response){
        List rows = new ArrayList();
        // 文件名
        String fileName = "";
        String sheetName = "";
        ExcelUtil.CellHeadField[] headFields = new ExcelUtil.CellHeadField[0];
        switch (exportInfo.getType()){
            case "chain":
                rows = zHrChainInfoService.getPartMinbuList();
                sheetName = "链群注册";
                fileName = "链群注册模板.xls";
                headFields = CHAIN_EXCEL_TITLE;
                break;
            case "grabGrid": // 导出网格信息
                TyMasterGrabQueryDto queryDTO = new TyMasterGrabQueryDto();
                queryDTO.setContractId(exportInfo.getContractId());
                rows = grabService.queryMeshGrabDetail(queryDTO);
                sheetName = "网格信息";
                fileName = "网格信息.xls";
                headFields = GRID_EXCEL_TITLE;
                break;
            case "masterGrab":
                System.out.println("");
                break;
            default:
                System.out.println("暂无匹配类型");
        }
        // 导出
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            exportService.export(fileName , sheetName , headFields , rows ,  request , response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }








    @PostMapping(value = {"/importChainInfo"})
    @ApiOperation(value = "导入链群注册信息")
    public R importChainInfo(MultipartFile file) {
        if (file.isEmpty()) {
            return R.error().put("msg","文件为空");
        }
        try {
            InputStream inputStream = file.getInputStream();
            List<ExportChainUnitInfo> list  = exportService.importExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            return R.ok().put("data",list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().put("msg","excel格式错误，请使用系统模板上传");
        }

    }




    private static final ExcelUtil.CellHeadField[] CHAIN_EXCEL_TITLE = {
            new ExcelUtil.CellHeadField("单元", "littleXwCode"),
            new ExcelUtil.CellHeadField("小微", "littleXwName"),
            new ExcelUtil.CellHeadField("分享比例（100%）", "sharePercent"),
    };


    private static final ExcelUtil.CellHeadField[] GRID_EXCEL_TITLE = {
            new ExcelUtil.CellHeadField("网格", "meshName"),
            new ExcelUtil.CellHeadField("收入（万元）", "income"),
            new ExcelUtil.CellHeadField("高端占比（%）", "struHighPercent"),
    };


}
