package com.haier.hailian.contract.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZChainShareDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.TargetBasicService;
import com.haier.hailian.contract.service.ZGamblingContractsService;
import com.haier.hailian.contract.util.Constant;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

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
    @Autowired
    private TargetBasicService targetBasicService;
    @Autowired
    private ZHrChainInfoDao hrChainInfoDao;
    @Autowired
    private ZChainShareDao chainShareDao;

//    @PostMapping(value = {"/saveGambling"})
//    @ApiOperation(value = "链群主抢单（举单）信息保存")
//    public R saveGambling(@RequestBody GamblingContractDTO dto) {
//        try {
//            gamblingContractsService.saveGambling(dto);
//            return R.ok();
//        }catch (Exception e){
//            e.printStackTrace();
//            return R.error("保存失败，请稍后重试");
//        }
//
//    }

    @PostMapping(value = {"/saveGamblingNew"})
    @ApiOperation(value = "新版链群主抢单（举单）信息保存")
    public R saveGamblingNew(@RequestBody SaveGamblingContractDTO dto) {
        try {
            gamblingContractsService.saveGamblingNew(dto);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error("保存失败，请稍后重试");
        }

    }

//    @GetMapping(value = {"/selectMarket"})
//    @ApiOperation(value = "查询42市场小微的名字和商圈目标名称")
//    public R selectMarket(@RequestParam String chainCode) {
//        MarketReturnDTO dto = gamblingContractsService.selectMarket(chainCode);
//        return R.ok().put("data",dto);
//    }

    @PostMapping(value = {"/selectTargetAll"})
    @ApiOperation(value = "查询主链群和子链群的所有链群目标、42中心和爆款")
    public R selectTargetAll(@RequestBody QueryBottomDTO dto) {

        ReturnTargetAllDTO targetAllDTO = new ReturnTargetAllDTO();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMM");
        String month = "";
        try {
            month = sf2.format(sf.parse(dto.getStartDate()));
        } catch (ParseException e) {
            throw new RException("日期类型不正确", Constant.CODE_VALIDFAIL);
        }
        //1.查询主链群目标
        dto.setMonth(month);
        List<TargetBasic> targetBasicList = targetBasicService.selectBottom(dto);
        targetAllDTO.setParentTarget(targetBasicList);
        String parentChain = dto.getChainCode();
        //2.查询子链群
        List<ZHrChainInfo> chainList = hrChainInfoDao.selectList(new QueryWrapper<ZHrChainInfo>().eq("parent_code",parentChain).eq("deleted","0"));
        if(null != chainList && chainList.size()>0){
            List<ReturnTargetDTO> children = new ArrayList<>();
            for(ZHrChainInfo chain:chainList){
                ReturnTargetDTO targetDTO = new ReturnTargetDTO();
                //3.查询子链群的链群目标
                String chainCode = chain.getChainCode();
                dto.setChainCode(chainCode);
                List<TargetBasic> childTarget = targetBasicService.selectBottom(dto);
                targetDTO.setChildChainCode(chainCode);
                targetDTO.setChildChainName(chain.getChainName());
                targetDTO.setChildTarget(childTarget);
                //4.查询子链群的42中心
                List<ZNodeTargetPercentInfo> childCenter = gamblingContractsService.selectMarket(chainCode).getMarket();
                targetDTO.setChildCenter(childCenter);
                //5.查询子链群的爆款目标
                QueryProductChainDTO dto1 = new QueryProductChainDTO();
                dto1.setChainCode(chainCode);
                dto1.setMonth(month);
                List<ContractProductDTO> childProduct = gamblingContractsService.selectProductSeries(dto1);
                targetDTO.setChildProduct(childProduct);
                //6.查询子链群的xwType3资源类型和最大数量
                List<ContractXwType3DTO> childXwType3 = gamblingContractsService.selectXwType3(chainCode);
                targetDTO.setChildXwType3(childXwType3);
                children.add(targetDTO);
            }
            targetAllDTO.setChildren(children);
        }else{
            //7.查询主链群的42中心
            MarketReturnDTO marketReturnDTO = gamblingContractsService.selectMarket(parentChain);
            List<ZNodeTargetPercentInfo> parentCenter = marketReturnDTO.getMarket();
            targetAllDTO.setParentCenter(parentCenter);
            //8.查询主链群的爆款目标
            QueryProductChainDTO dto1 = new QueryProductChainDTO();
            dto1.setChainCode(parentChain);
            dto1.setMonth(month);
            List<ContractProductDTO> parentProduct = gamblingContractsService.selectProductSeries(dto1);
            targetAllDTO.setParentProduct(parentProduct);
            //9.查询主链群的xwType3资源类型和最大数量
            List<ContractXwType3DTO> childXwType3 = gamblingContractsService.selectXwType3(parentChain);
            targetAllDTO.setParentXwType3(childXwType3);
        }
        return R.ok().put("data",targetAllDTO);
    }

    @PostMapping(value = {"/calculateSharing"})
    @ApiOperation(value = "计算分享空间")
    public R calculateSharing(@RequestBody CalculateSharingDTO dto) {
        String chainCode = dto.getChainCode();
        BigDecimal share = ZERO;
        String unit = "元";
        List<ChainGroupTargetDTO2> targetList = dto.getTargetList();
        if(null == targetList || targetList.size()==0) return R.error().put("msg","参数有误，无法计算");
        List<ZChainShare> chainShares = chainShareDao.selectList(new QueryWrapper<ZChainShare>().eq("chain_code",chainCode));
        if(null != chainShares && chainShares.size()>0 && "T02006*T01002".equals(chainShares.get(0).getShareExpression())){
            BigDecimal qtyQD = ZERO;
            BigDecimal singleIncomeQD = ZERO;
            BigDecimal qtyDX = ZERO;
            BigDecimal singleIncomeDX = ZERO;
           for(ChainGroupTargetDTO2 target:targetList){
               if(target.getTargetCode().equals("T02006")){
                   singleIncomeDX = target.getBottom();
                   singleIncomeQD = target.getGrab();
                   unit = target.getTargetUnit();
               }
               if(target.getTargetCode().equals("T01002")){
                   qtyDX = target.getBottom();
                   qtyQD = target.getGrab();
               }
           }
           share = singleIncomeQD.multiply(qtyQD).subtract(singleIncomeDX.multiply(qtyDX));
        }else{
            BigDecimal incomeQD = ZERO;
            BigDecimal incomeDX = ZERO;
            BigDecimal incomeE2E = ZERO;
            for(ChainGroupTargetDTO2 target:targetList) {
                if (target.getTargetCode().equals("T01017")) {
                    incomeDX = target.getBottom();
                    incomeQD = target.getGrab();
                    incomeE2E = target.getE2E();
                    unit = target.getTargetUnit();
                }
            }
            if(null != chainShares && chainShares.size()>0 && "T01017".equals(chainShares.get(0).getShareExpression())){
                Double sharePercent1 = chainShares.get(0).getSharePercent1();
                Double sharePercent2 = chainShares.get(0).getSharePercent2();

                if(incomeQD.compareTo(incomeE2E) == 1){
                    share = incomeQD.subtract(incomeE2E).multiply(BigDecimal.valueOf(sharePercent2).divide(BigDecimal.valueOf(100))).add(incomeE2E.subtract(incomeDX).multiply(BigDecimal.valueOf(sharePercent1).divide(BigDecimal.valueOf(100))));
                }else if(incomeQD.compareTo(incomeDX)==1){
                    share = incomeQD.subtract(incomeDX).multiply(BigDecimal.valueOf(sharePercent1).divide(BigDecimal.valueOf(100)));
                }
            }else{
                if(incomeQD.compareTo(incomeE2E) == 1){
                    share = incomeQD.subtract(incomeE2E).multiply(BigDecimal.valueOf(50).divide(BigDecimal.valueOf(100))).add(incomeE2E.subtract(incomeDX).multiply(BigDecimal.valueOf(20).divide(BigDecimal.valueOf(100))));
                }else if(incomeQD.compareTo(incomeDX)==1){
                    share = incomeQD.subtract(incomeDX).multiply(BigDecimal.valueOf(20).divide(BigDecimal.valueOf(100)));
                }
            }
        }
        return R.ok().put("share",share.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()).put("unit",unit);
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
        List<ContractProductDTO> productList = gamblingContractsService.selectProductSeries(dto);
        return R.ok().put("data",productList);
    }


    @GetMapping(value = {"/selectContractById/{contractId}"})
    @ApiOperation(value = "根据合约ID查询链群主举单详情")
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

    @GetMapping(value = "/exportGamblingContract",headers="Accept=application/octet-stream")
    @ApiOperation(value = "举单详情导出")
    public void exportGamblingContract(@RequestParam int contractId,HttpServletRequest request, HttpServletResponse response) throws IOException{
        gamblingContractsService.exportGamblingContract(contractId,request,response);
    }


    @GetMapping(value = "/exportChainProduct",headers="Accept=application/octet-stream")
    @ApiOperation(value = "下载爆款模板")
    public void exportChainProduct(@RequestParam String chainCode,@RequestParam String month,HttpServletRequest request, HttpServletResponse response) throws IOException{
        gamblingContractsService.exportChainProduct(chainCode,month,request,response);
    }

    @PostMapping(value = {"/importChainProduct"})
    @ApiOperation(value = "导入链群爆款")
    public R importChainProduct(MultipartFile file) throws Exception{
        if (file.isEmpty()) {
            return R.error().put("msg","文件为空");
        }
        InputStream inputStream = file.getInputStream();
        List<ChainProductDTO> list = gamblingContractsService.getChainProductListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();
        return R.ok().put("data",list);
    }

    @PostMapping(value = {"/saveChainProduct"})
    @ApiOperation(value = "链群爆款保存")
    public R saveChainProduct(@RequestBody List<ChainProductDTO> list) {
        try {
            gamblingContractsService.saveChainProduct(list);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.error("保存失败，请稍后重试");
        }

    }


}

