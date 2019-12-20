package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.MonthChainGroupOrder;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysNet;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZNetBottom;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.MonthChainGroupOrderService;
import com.haier.hailian.contract.service.SysNetService;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.service.ZNetBottomService;
import com.haier.hailian.contract.util.AmountFormat;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 19012964 on 2019/12/17.
 */
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

    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(MeshStatisticQueryDto queryDto) {
        ZContracts contracts =contractsService.getById(queryDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto=new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(queryDto.getContractId());
        tyMasterGrabChainInfoDto.setChainName(contracts.getContractName());
        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());

       List<ZContractsFactor> factors=contractsFactorService.list(
               new QueryWrapper<ZContractsFactor>().eq("contract_id",contracts.getId())
               .eq("region_code",queryDto.getXwCode())
       );
        ZContractsFactor incomeFact= factors.stream().filter(f->Constant.FactorCode.Incom.getValue()
                .equals(f.getFactorCode())).findAny().get();
        if(incomeFact!=null){
            tyMasterGrabChainInfoDto.setTargetIncome(AmountFormat.amtStr2BD(incomeFact.getFactorValue()));
        }
        ZContractsFactor highFact= factors.stream().filter(f->Constant.FactorCode.HighPercent.getValue()
                .equals(f.getFactorCode())).findAny().get();
        if(highFact!=null) {
            tyMasterGrabChainInfoDto.setTargetHighPercent(AmountFormat.amtStr2BD(highFact.getFactorValue()));
        }
        ZContractsFactor lowFact= factors.stream().filter(f->Constant.FactorCode.LowPercent.getValue()
                .equals(f.getFactorCode())).findAny().get();
        if(lowFact!=null){
            tyMasterGrabChainInfoDto.setTargetLowPercent(AmountFormat.amtStr2BD(lowFact.getFactorValue()));
        }

        return tyMasterGrabChainInfoDto;
    }

    @Override
    public MeshSummaryDto queryMeshGrabDetail(MeshStatisticQueryDto queryDto) {
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
            meshGrabDto.setIncome(
                    new BigDecimal(current.stream().mapToDouble(m->
                            AmountFormat.amtStr2D(m.getIncome())).sum())
            );
            //高端
            List<MeshGrabEntity> high=current.stream().filter(
                    f->Constant.ProductStru.High.toString().equals(f.getProductStru()))
                    .collect(Collectors.toList());
            if(high !=null && high.size()>0){
                BigDecimal highIncome=new BigDecimal(high.stream().mapToDouble(m->
                        AmountFormat.amtStr2D(m.getIncome())).sum());
                meshGrabDto.setStruHighPercent(highIncome.divide(meshGrabDto.getIncome()));
            }else{
                meshGrabDto.setStruHighPercent(BigDecimal.ZERO);
            }
            //低端
            List<MeshGrabEntity> low=current.stream().filter(
                    f->Constant.ProductStru.Low.toString().equals(f.getProductStru()))
                    .collect(Collectors.toList());
            if(low !=null && low.size()>0){
                BigDecimal lowIncome=new BigDecimal(high.stream().mapToDouble(m->
                        AmountFormat.amtStr2D(m.getIncome())).sum());
                meshGrabDto.setStruHighPercent(lowIncome.divide(meshGrabDto.getIncome()));
            }else{
                meshGrabDto.setStruHighPercent(BigDecimal.ZERO);
            }
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
        summaryDto.setStruHighPercent(highPercent.divide(new BigDecimal(meshGrabInfoDtos.size())));
        summaryDto.setStruLowPercent(lowPercent.divide(new BigDecimal(meshGrabInfoDtos.size())));

        return summaryDto;
    }

    @Override
    public R doGrab(MessGambSubmitDto dto) {
        //校验是否已抢过单
        TyMasterGrabChainInfoDto chainInfoDto =dto.getTyMasterGrabChainInfoDto();
        MeshSummaryDto meshSummaryDto=dto.getMeshSummaryDto();
        if(meshSummaryDto.getIncome().compareTo(chainInfoDto.getTargetIncome())<0){
            throw new RException("抢单收入必须大于目标收入",Constant.CODE_VALIDFAIL);
        }
        ZContracts contracts=contractsService.getById(chainInfoDto.getContractId());
        if(contracts==null){
            throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        contracts.setParentId(chainInfoDto.getContractId());
        contracts.setId(0);
        contracts.setContractName(chainInfoDto.getChainName());
        contracts.setContractType("20");
        contracts.setCreateCode(currentUser.getEmpsn());
        contracts.setCreateName(currentUser.getEmpname());
        contracts.setCreateTime(new Date());
        contracts.setJoinTime(new Date());
        contracts.setStatus("1");
        contracts.setOrgCode(currentUser.getOrgNum());
        contracts.setOrgName(currentUser.getOrgName());
        contractsService.save(contracts);

        List<ZContractsFactor> factors=new ArrayList<>();
        ZContractsFactor factor=new ZContractsFactor();
        factor.setContractId(contracts.getId());
        factor.setFactorCode(Constant.FactorCode.Incom.getValue());
        factor.setFactorName(Constant.FactorCode.Incom.getName());
        factor.setFactorType(Constant.FactorType.Bottom.getValue());
        factor.setFactorUnit("");
        factor.setFactorValue( chainInfoDto.getTargetIncome().toString());

        //业务参数校验
        return R.ok();
    }
}
