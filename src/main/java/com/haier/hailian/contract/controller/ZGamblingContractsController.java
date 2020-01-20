package com.haier.hailian.contract.controller;


import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZProductChain;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 01431594
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/zGamblingContracts")
@Api(value = "链群主抢单（举单）接口", tags = "链群主抢单（举单）接口")
public class ZGamblingContractsController {

    @Autowired
    private ZGamblingContractsService gamblingContractsService;

    @PostMapping(value = {"/saveGambling"})
    @ApiOperation(value = "链群主抢单（举单）信息保存")
    public R selectBottom(@RequestBody GamblingContractDTO dto) {
        try {
            gamblingContractsService.saveGambling(dto);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error("保存失败，请联系管理员");
        }

    }

    @GetMapping(value = {"/selectMarket"})
    @ApiOperation(value = "查询42市场小微的名字和商圈目标名称")
    public R selectMarket(@RequestParam String chainCode) {
        MarketReturnDTO dto = gamblingContractsService.selectMarket(chainCode);
        return R.ok().put("data",dto);
    }

    @PostMapping(value = {"/calculateSharing"})
    @ApiOperation(value = "计算分享空间")
    public R calculateSharing(@RequestBody CalculateSharingDTO dto) {
        if(dto.getBottom() != null && dto.getGrab() != null ){
            BigDecimal share = dto.getGrab().subtract(dto.getBottom());
            return R.ok().put("share",share);
        }
        if(dto.getBottom() != null && dto.getShare() != null){
            BigDecimal grab = dto.getBottom().add(dto.getShare());
            return R.ok().put("grab",grab);
        }
        return R.error().put("msg","参数有误，无法计算");
    }

    @PostMapping(value = {"/selectContractList"})
    @ApiOperation(value = "查询合约列表")
    public R selectContractList(@RequestBody QueryContractListDTO queryDTO) {
        List<ZContracts> contractsList = gamblingContractsService.selectContractList(queryDTO);
        return R.ok().put("data",contractsList);
    }

    @PostMapping(value = {"/selectProductSeries"})
    @ApiOperation(value = "根据链群编码查询产品系列")
    public R selectProductSeries(@RequestBody QueryProductChainDTO dto) {
        List<ZProductChain> list = gamblingContractsService.selectProductSeries(dto);
        List<ContractProductDTO> productList = new ArrayList<>();
        for(ZProductChain productChain : list){
            ContractProductDTO productDTO = new ContractProductDTO();
            productDTO.setProductSeries(productChain.getProductSeries());
            productDTO.setQtyMonth(null);
            productDTO.setQtyYear(null);
            productList.add(productDTO);
        }
        return R.ok().put("data",productList);
    }


    @GetMapping(value = {"/selectContractById/{contractId}"})
    @ApiOperation(value = "根据合约ID查询链群主抢单详情")
    public R selectContractById(@PathVariable(value = "contractId") Integer contractId) {
        GamblingContractDTO dto = gamblingContractsService.selectContractById(contractId);
        return R.ok().put("data",dto);
    }


    @GetMapping(value = "/exportMarket",headers="Accept=application/octet-stream")
    @ApiOperation(value = "查询并导出42市场小微的名字和商圈目标名称")
    public void exportMarket(@RequestParam String chainCode,HttpServletRequest request, HttpServletResponse response) throws IOException{
        gamblingContractsService.exportMarket(chainCode,request,response);
    }

    @PostMapping(value = {"/importMarket"})
    @ApiOperation(value = "导入42市场小微的名字和商圈目标名称")
    public R importMarket(MultipartFile file) throws Exception{
        if (file.isEmpty()) {
            return R.error().put("msg","文件为空");
        }
        InputStream inputStream = file.getInputStream();
        List<MarketTargetDTO> list = gamblingContractsService.getMarketTargetListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();
        return R.ok().put("data",list);
    }

    @GetMapping(value = "/exportProductSeries",headers="Accept=application/octet-stream")
    @ApiOperation(value = "导出链群下的产品系列")
    public void exportProductSeries(@RequestParam String chainCode,HttpServletRequest request, HttpServletResponse response) throws IOException{
        gamblingContractsService.exportProductSeries(chainCode,request,response);
    }

    @PostMapping(value = {"/importProductSeries"})
    @ApiOperation(value = "导入产品系列和销量")
    public R importProductSeries(MultipartFile file) throws Exception{
        if (file.isEmpty()) {
            return R.error().put("msg","文件为空");
        }
        InputStream inputStream = file.getInputStream();
        List<ContractProductDTO> list = gamblingContractsService.getProductSeriesListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();
        return R.ok().put("data",list);
    }


}

