package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysXwRegion;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.MonthChainGroupOrderService;
import com.haier.hailian.contract.service.SysNetService;
import com.haier.hailian.contract.service.SysXwRegionService;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.service.ZNetBottomService;
import com.haier.hailian.contract.util.AmountFormat;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 19012964 on 2019/12/17.
 */
@Slf4j
@Service
public class GrabServiceImpl implements GrabService {
    @Autowired
    private SysNetService sysNetService;
    @Autowired
    private MonthChainGroupOrderService monthChainGroupOrderService;
    @Autowired
    private ZNetBottomService netBottomService;
    @Autowired
    private ZContractsService contractsService;
    @Autowired
    private ZContractsFactorService contractsFactorService;
    @Autowired
    private ChainCommonService chainCommonService;

    @Autowired
    private SysXwRegionService xwRegionService;

    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(TyMasterGrabQueryDto queryDto) {
        ZContracts contracts =contractsService.getById(queryDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();

        List<SysXwRegion> xwRegion=xwRegionService.list(new QueryWrapper<SysXwRegion>()
                .eq("xw_code", currentUser.getXwCode()));

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto=new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(queryDto.getContractId());
        String chainName=contracts.getContractName();
        if(xwRegion!=null&&xwRegion.size()>0){
            chainName=chainName.replace("链群","-"+xwRegion.get(0).getRegionName()+"链群");
            tyMasterGrabChainInfoDto.setRegionCode(xwRegion.get(0).getRegionCode());
        }
        tyMasterGrabChainInfoDto.setChainName(chainName);
        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());
       List<ZContractsFactor> factors=contractsFactorService.list(
               new QueryWrapper<ZContractsFactor>().eq("contract_id",contracts.getId())
               .eq("region_code",currentUser.getXwCode())
       );
        List<ZContractsFactor> incomeFact= factors.stream().filter(f->Constant.FactorCode.Incom.getValue()
                .equals(f.getFactorCode())).collect(Collectors.toList());
        if(incomeFact!=null&&incomeFact.size()>0){
            tyMasterGrabChainInfoDto.setTargetIncome(AmountFormat.amtStr2BD(incomeFact.get(0).getFactorValue()));
        }else{
            tyMasterGrabChainInfoDto.setTargetIncome(BigDecimal.ZERO);
        }
        List<ZContractsFactor> highFact= factors.stream().filter(f->Constant.FactorCode.HighPercent.getValue()
                .equals(f.getFactorCode())).collect(Collectors.toList());
        if(highFact!=null&&highFact.size()>0) {
            tyMasterGrabChainInfoDto.setTargetHighPercent(AmountFormat.amtStr2BD(highFact.get(0).getFactorValue()));
        }else{
            tyMasterGrabChainInfoDto.setTargetHighPercent(BigDecimal.ZERO);
        }
        List<ZContractsFactor> lowFact= factors.stream().filter(f->Constant.FactorCode.LowPercent.getValue()
                .equals(f.getFactorCode())).collect(Collectors.toList());
        if(lowFact!=null&&lowFact.size()>0){
            tyMasterGrabChainInfoDto.setTargetLowPercent(AmountFormat.amtStr2BD(lowFact.get(0).getFactorValue()));
        }else{
            tyMasterGrabChainInfoDto.setTargetLowPercent(BigDecimal.ZERO);
        }

        return tyMasterGrabChainInfoDto;
    }

    @Override
    public MeshSummaryDto queryMeshGrabDetail(TyMasterGrabQueryDto queryDto) {
        if(queryDto==null){
            throw new RException("缺少必填项",Constant.CODE_VALIDFAIL);
        }
        perfectQueryParam(queryDto);

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        queryDto.setLoginXwCode(currentUser.getXwCode());


        List<MeshGrabEntity> meshGrabEntities=monthChainGroupOrderService.queryMeshGrabIncome(queryDto);
        List<String> wgCodes=meshGrabEntities.stream().map(m->m.getMeshCode())
                .distinct().collect(Collectors.toList());
        List<MeshGrabInfoDto> meshGrabInfoDtos=new ArrayList<>();
        for (String wg:wgCodes) {
            MeshGrabInfoDto meshGrabDto=new MeshGrabInfoDto();
            List<MeshGrabEntity> current=meshGrabEntities.stream().filter(
                    f->wg.equals(f.getMeshCode())
            ).collect(Collectors.toList());
            meshGrabDto.setMeshCode(wg);
            meshGrabDto.setMeshName(current.get(0).getMeshName());
            BigDecimal totalIncome=new BigDecimal(current.stream().mapToDouble(m->
                    AmountFormat.amtStr2D(m.getIncome())).sum());
            meshGrabDto.setIncome(totalIncome);//元
            //高端
            List<MeshGrabEntity> high=current.stream().filter(
                    f->Constant.ProductStru.High.getValue().equals(f.getProductStru()))
                    .collect(Collectors.toList());
            if(high !=null && high.size()>0){
                BigDecimal highIncome=new BigDecimal(high.stream().mapToDouble(m->
                        AmountFormat.amtStr2D(m.getIncome())).sum());
                meshGrabDto.setStruHighPercent(highIncome
                        .divide(meshGrabDto.getIncome(),4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                );
            }else{
                meshGrabDto.setStruHighPercent(BigDecimal.ZERO);
            }
            //低端
            List<MeshGrabEntity> low=current.stream().filter(
                    f->Constant.ProductStru.Low.getValue().equals(f.getProductStru()))
                    .collect(Collectors.toList());
            if(low !=null && low.size()>0){
                BigDecimal lowIncome=new BigDecimal(low.stream().mapToDouble(m->
                        AmountFormat.amtStr2D(m.getIncome())).sum());
                meshGrabDto.setStruLowPercent(lowIncome
                        .divide(meshGrabDto.getIncome(),4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                );
            }else{
                meshGrabDto.setStruLowPercent(BigDecimal.ZERO);
            }
            meshGrabDto.setIncome(meshGrabDto.getIncome().divide(
                    new BigDecimal("10000"),2, RoundingMode.HALF_UP
            ));//格式化万元
            meshGrabInfoDtos.add(meshGrabDto);

        }
        MeshSummaryDto summaryDto=new MeshSummaryDto();
        summaryDto.setMeshDetail(meshGrabInfoDtos);
        BigDecimal inc=BigDecimal.ZERO,highPercent=BigDecimal.ZERO ,lowPercent=BigDecimal.ZERO;
        for (MeshGrabInfoDto tem:meshGrabInfoDtos) {
            inc=inc.add(tem.getIncome());
            highPercent=highPercent.add(tem.getStruHighPercent());
            lowPercent=lowPercent.add(tem.getStruLowPercent());
        }
        summaryDto.setIncome(inc);
        if(meshGrabInfoDtos!=null&&meshGrabInfoDtos.size()>0) {
            summaryDto.setStruHighPercent(highPercent
                    .divide(new BigDecimal(meshGrabInfoDtos.size()),2, RoundingMode.HALF_UP)

            );
            summaryDto.setStruLowPercent(lowPercent
                    .divide(new BigDecimal(meshGrabInfoDtos.size()),2, RoundingMode.HALF_UP)

            );
        }
        return summaryDto;
    }

    @Override
    public R doGrab(MessGambSubmitDto dto) {
        //业务数据校验
        if(dto==null||dto.getTyMasterGrabChainInfoDto()==null||dto.getMeshSummaryDto()==null){
            throw new RException("缺少必填参数",Constant.CODE_VALIDFAIL);
        }
        TyMasterGrabChainInfoDto chainInfoDto =dto.getTyMasterGrabChainInfoDto();
        MeshSummaryDto meshSummaryDto=dto.getMeshSummaryDto();
        if(meshSummaryDto.getIncome().compareTo(chainInfoDto.getTargetIncome())<0){
            throw new RException("抢单收入必须大于目标收入",Constant.CODE_VALIDFAIL);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();

        //根据小微code 和合约判断是否已抢单
        ZContracts existsContract=contractsService.getOne(new QueryWrapper<ZContracts>()
        .eq("parent_id",chainInfoDto.getContractId())
        .eq("xiaowei_code",currentUser.getXwCode()));
        if(existsContract!=null){
            throw new RException("该用户已抢单，请勿重复抢单",Constant.CODE_VALIDFAIL);
        }

        ZContracts contracts=contractsService.getById(chainInfoDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }

        contracts.setParentId(chainInfoDto.getContractId());
        contracts.setId(0);
        contracts.setContractName(chainInfoDto.getChainName());
        contracts.setContractType("20");
        contracts.setCreateCode(currentUser.getEmpsn());
        contracts.setCreateName(currentUser.getEmpname());
        contracts.setCreateTime(new Date());
        contracts.setJoinTime(new Date());
        contracts.setStatus("1");
        contracts.setRegionCode(chainInfoDto.getRegionCode());
        contracts.setOrgCode(currentUser.getOrgNum());
        contracts.setOrgName(currentUser.getOrgName());
        contracts.setOrgType(currentUser.getOrgType());
        contracts.setXiaoweiCode(currentUser.getXwCode());
        contractsService.save(contracts);

        List<ZContractsFactor> factors=new ArrayList<>();
        ZContractsFactor incomeFact=new ZContractsFactor();
        incomeFact.setContractId(contracts.getId());
        incomeFact.setFactorCode(Constant.FactorCode.Incom.getValue());
        incomeFact.setFactorName(Constant.FactorCode.Incom.getName());
        incomeFact.setFactorType(Constant.FactorType.Bottom.getValue());
        incomeFact.setFactorUnit(Constant.RMB_MIL);
        incomeFact.setFactorValue( chainInfoDto.getTargetIncome().toString());
        factors.add(incomeFact);
        //高端占比
        ZContractsFactor highFact=new ZContractsFactor();
        highFact.setContractId(contracts.getId());
        highFact.setFactorCode(Constant.FactorCode.HighPercent.getValue());
        highFact.setFactorName(Constant.FactorCode.HighPercent.getName());
        highFact.setFactorType(Constant.FactorType.Bottom.getValue());
        highFact.setFactorUnit("%");
        highFact.setFactorValue( chainInfoDto.getTargetHighPercent().toString());
        factors.add(highFact);

        //低端占比
        ZContractsFactor lowFact=new ZContractsFactor();
        lowFact.setContractId(contracts.getId());
        lowFact.setFactorCode(Constant.FactorCode.LowPercent.getValue());
        lowFact.setFactorName(Constant.FactorCode.LowPercent.getName());
        lowFact.setFactorType(Constant.FactorType.Bottom.getValue());
        lowFact.setFactorUnit("%");
        lowFact.setFactorValue( chainInfoDto.getTargetLowPercent().toString());
        factors.add(lowFact);

        //复制到商圈汇总
        ZContractsFactor grabIncomeFact=new ZContractsFactor();
        ZContractsFactor grabHighFact=new ZContractsFactor();
        ZContractsFactor grabLowFact=new ZContractsFactor();
        BeanUtils.copyProperties(incomeFact,grabIncomeFact);
        BeanUtils.copyProperties(highFact,grabHighFact);
        BeanUtils.copyProperties(lowFact,grabLowFact);
        grabIncomeFact.setFactorType(Constant.FactorType.Grab.getValue());
        grabIncomeFact.setFactorValue(meshSummaryDto.getIncome().toString());
        factors.add(grabIncomeFact);

        grabHighFact.setFactorType(Constant.FactorType.Grab.getValue());
        grabHighFact.setFactorValue(meshSummaryDto.getStruHighPercent().toString());
        factors.add(grabHighFact);

        grabLowFact.setFactorType(Constant.FactorType.Grab.getValue());
        grabLowFact.setFactorValue(meshSummaryDto.getStruLowPercent().toString());
        factors.add(grabLowFact);

        for (MeshGrabInfoDto meshGrabInfo: meshSummaryDto.getMeshDetail()) {
            ZContractsFactor meshGrabIncome=new ZContractsFactor();
            ZContractsFactor meshGrabHigh=new ZContractsFactor();
            ZContractsFactor meshGrabLow=new ZContractsFactor();
            BeanUtils.copyProperties(grabIncomeFact,meshGrabIncome);
            BeanUtils.copyProperties(grabHighFact,meshGrabHigh);
            BeanUtils.copyProperties(grabLowFact,meshGrabLow);

            grabIncomeFact.setFactorType(Constant.FactorType.Grab.getValue());
            grabIncomeFact.setFactorValue(meshGrabInfo.getIncome().toString());
            grabIncomeFact.setMeshCode(meshGrabInfo.getMeshCode());
            grabIncomeFact.setMeshName(meshGrabInfo.getMeshName());
            factors.add(grabIncomeFact);

            grabHighFact.setFactorType(Constant.FactorType.Grab.getValue());
            grabHighFact.setFactorValue(meshSummaryDto.getStruHighPercent().toString());
            grabHighFact.setMeshCode(meshGrabInfo.getMeshCode());
            grabHighFact.setMeshName(meshGrabInfo.getMeshName());
            factors.add(grabHighFact);

            grabLowFact.setFactorType(Constant.FactorType.Grab.getValue());
            grabLowFact.setFactorValue(meshSummaryDto.getStruLowPercent().toString());
            grabLowFact.setMeshCode(meshGrabInfo.getMeshCode());
            grabLowFact.setMeshName(meshGrabInfo.getMeshName());
            factors.add(grabLowFact);
        }
        contractsFactorService.saveBatch(factors);

        // chainCommonService.buildContractChain(contracts.getId());

        return R.ok();
    }

    @Override
    public void refreshContractStatusJob() {
        //截止抢入时间 仍然处于抢入中的合约
        List<ZContracts> list=contractsService.list(new QueryWrapper<ZContracts>()
                .eq("contract_type","10")
                .eq("status","0")
                .lt("join_time",new Date())
        );
        List<ZContracts> updateList=new ArrayList<>();
        for (ZContracts contract:list) {
            //是否存在抢单记录
            ZContracts updateContract=new ZContracts();
            updateContract.setId(contract.getId());
            List<ZContracts> sub=contractsService.list(new QueryWrapper<ZContracts>()
                    .eq("parent_id", contract.getId())
                    .last("limit 1")
            );
            if(sub==null||sub.size()==0){
                updateContract.setStatus("4");
            }else{
                updateContract.setStatus("1");
            }
            updateList.add(updateContract);
        }
        log.info("========刷新合约状态=====");
        log.info("数据：{}", updateList);
        contractsService.updateBatchById(updateList);

    }

    private void perfectQueryParam(TyMasterGrabQueryDto queryDTO){
        if(StringUtils.isBlank(queryDTO.getYear())){
            queryDTO.setYear(String.valueOf(DateFormatUtil.getYearOfDate(new Date())));
        }
        ZContracts contracts=contractsService.getById(queryDTO.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }
        queryDTO.setContractOwner(contracts.getCreateCode());
        int mounth = DateFormatUtil.getMonthOfDate(contracts.getStartDate());
        int endMounth=DateFormatUtil.getMonthOfDate(contracts.getEndDate());
        if(queryDTO.getMonth()==null||queryDTO.getMonth().isEmpty()){
            List<String> mounths=new ArrayList<>();
            List<String> yearMounths=new ArrayList<>();

            for(int start=mounth;start<=endMounth;start++){
                mounths.add(String.valueOf(start));
                String strMounth = start < 10 ? "0" + start : String.valueOf(start);
                yearMounths.add(queryDTO.getYear()+strMounth);
            }
            queryDTO.setMonth(mounths);
            queryDTO.setYearMonth(yearMounths);
        }
    }
}
