package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dao.ZNodeTargetPercentInfoDao;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.FactorDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.dto.grab.MessGambSubmitDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysXwRegion;
import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.entity.ZContractsFactor;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.entity.ZNodeTargetPercentInfo;
import com.haier.hailian.contract.service.ChainCommonService;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.MonthChainGroupOrderService;
import com.haier.hailian.contract.service.SysNetService;
import com.haier.hailian.contract.service.SysXwRegionService;
import com.haier.hailian.contract.service.ZContractsFactorService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.service.ZHrChainInfoService;
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
    private ChainCommonService chainCommonService; //上链
    @Autowired
    private ZNodeTargetPercentInfoDao nodeInfoDao;

    @Autowired
    private SysXwRegionService xwRegionService;
    @Autowired
    private ZHrChainInfoDao chainInfoDao;


    @Override
    public List<TyMasterGrabChainInfoDto> queryChainList(TyGrabListQueryDto query) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu minbu = sysUser.getMinbu();
        if(minbu==null||StringUtils.isBlank(minbu.getXwType5Code())||
                !minbu.getXwType5Code().equals("2")){
            throw new RException(Constant.MSG_NO_MINBU,Constant.CODE_NO_MINBU);
        }
        //根据minBu查看链群的编码
        List<String> chainCodes=nodeInfoDao.selectList(
                new QueryWrapper<ZNodeTargetPercentInfo>().eq("node_code", minbu.getLittleXwCode())
        ).stream().map(m->m.getLqCode()).distinct().collect(Collectors.toList());
        List<TyMasterGrabChainInfoDto> list=new ArrayList<>();
        if(chainCodes!=null&&chainCodes.size()>0){
            query.setChainCodes(chainCodes);
            query.setEmpSn(sysUser.getEmpSn());

            List<ZContracts> contractses=contractsService.queryTyGrabList(query);

            for ( ZContracts contract: contractses
                 ) {
                TyMasterGrabQueryDto queryDto=new TyMasterGrabQueryDto();
                queryDto.setContractId(contract.getId());
                TyMasterGrabChainInfoDto grabDto=this.queryChainInfo(queryDto);
                list.add(grabDto);
            }
        }

        return list;
    }

    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(TyMasterGrabQueryDto queryDto) {
        ZContracts contracts = contractsService.getById(queryDto.getContractId());
        if (contracts == null) {
            throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();

        List<SysXwRegion> xwRegion = xwRegionService.list(new QueryWrapper<SysXwRegion>()
                .eq("xw_code", currentUser.getXwCode()));

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto = new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(queryDto.getContractId());
        String chainName = contracts.getContractName();
        if (xwRegion != null && xwRegion.size() > 0) {
            chainName = chainName.replace("链群", "-" + xwRegion.get(0).getRegionName() + "链群");
            tyMasterGrabChainInfoDto.setRegionCode(xwRegion.get(0).getRegionCode());
        }
        ZHrChainInfo chainInfo=chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code", contracts.getChainCode()));
        tyMasterGrabChainInfoDto.setContractName(chainName);
        tyMasterGrabChainInfoDto.setContractOwner(contracts.getCreateName());
        tyMasterGrabChainInfoDto.setChainName(chainInfo==null?"":chainInfo.getChainName());
        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setGrabEnd(DateFormatUtil.format(contracts.getJoinTime()
                ,DateFormatUtil.DATE_TIME_PATTERN));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());
        List<ZContractsFactor> factors = contractsFactorService.list(
                new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId())
                        .eq("region_code", currentUser.getXwCode())
        );
        List<FactorDto> targetFactor = factors.stream().map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            dto.setFactorType(m.getFactorType());
            dto.setHasInput(false);
            return dto;
        }).collect(Collectors.toList());

        //网格抢单汇总
        perfectQueryParam(queryDto);
        queryDto.setLoginXwCode(currentUser.getXwCode());
        List<MeshGrabEntity> meshGrabEntities=monthChainGroupOrderService.sumStruMeshGrabIncome(queryDto);
        BigDecimal inc=new BigDecimal(meshGrabEntities.stream().mapToDouble(m->
                AmountFormat.amtStr2D(m.getIncome())).sum());

        //根据目标维度 设置抢单的维度
        List<FactorDto> grabFactors=new ArrayList<>();
        for (FactorDto index : targetFactor) {
            FactorDto grabFactor=new FactorDto();
            BeanUtils.copyProperties(index, grabFactor);
            grabFactor.setFactorType(Constant.FactorType.Grab.getValue());
            if (Constant.FactorCode.Incom.getValue().equals(index.getFactorCode())) {
                grabFactor.setFactorValue(inc.divide(
                        new BigDecimal("10000"),2, RoundingMode.HALF_UP
                ).toString());//格式化万
            } else if (Constant.FactorCode.HighPercent.getValue().equals(index.getFactorCode())) {
                List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f->
                        Constant.ProductStru.High.getValue().equals(f.getProductStru()))
                        .collect(Collectors.toList());
                if(curr !=null && curr.size()>0){
                    BigDecimal currIncom=new BigDecimal(curr.stream().mapToDouble(m->
                            AmountFormat.amtStr2D(m.getIncome())).sum());
                    grabFactor.setFactorValue(currIncom
                            .divide(inc,4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100")).toString()
                    );
                }else{
                    grabFactor.setFactorValue("0");
                }
            } else if (Constant.FactorCode.LowPercent.getValue().equals(index.getFactorCode())) {
                List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f->
                        Constant.ProductStru.Low.getValue().equals(f.getProductStru()))
                        .collect(Collectors.toList());
                if(curr !=null && curr.size()>0){
                    BigDecimal currIncom=new BigDecimal(curr.stream().mapToDouble(m->
                            AmountFormat.amtStr2D(m.getIncome())).sum());
                    grabFactor.setFactorValue(currIncom
                            .divide(inc,4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100")).toString()
                    );
                }else{
                    grabFactor.setFactorValue("0");
                }
            } else if (Constant.FactorCode.MiddPercent.getValue().equals(index.getFactorCode())) {
                List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f->
                        Constant.ProductStru.Midd.getValue().equals(f.getProductStru()))
                        .collect(Collectors.toList());
                if(curr !=null && curr.size()>0){
                    BigDecimal currIncom=new BigDecimal(curr.stream().mapToDouble(m->
                            AmountFormat.amtStr2D(m.getIncome())).sum());
                    grabFactor.setFactorValue(currIncom
                            .divide(inc,4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100")).toString()
                    );
                }else{
                    grabFactor.setFactorValue("0");
                }
            } else {
                grabFactor.setFactorValue("");
                grabFactor.setHasInput(true);
            }
            grabFactors.add(grabFactor);
        }
        tyMasterGrabChainInfoDto.setTargetList(targetFactor);
        tyMasterGrabChainInfoDto.setGrabList(grabFactors);

        return tyMasterGrabChainInfoDto;
    }


    @Override
    public List<MeshGrabInfoDto> queryMeshGrabDetail(TyMasterGrabQueryDto queryDto) {
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
            //中端
            List<MeshGrabEntity> midd=current.stream().filter(
                    f->Constant.ProductStru.Midd.getValue().equals(f.getProductStru()))
                    .collect(Collectors.toList());
            if(midd !=null && midd.size()>0){
                BigDecimal middIncome=new BigDecimal(midd.stream().mapToDouble(m->
                        AmountFormat.amtStr2D(m.getIncome())).sum());
                meshGrabDto.setStruMidPercent(middIncome
                        .divide(meshGrabDto.getIncome(),4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                );
            }else{
                meshGrabDto.setStruMidPercent(BigDecimal.ZERO);
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
        return  meshGrabInfoDtos;

    }

    @Override
    public R doGrab(MessGambSubmitDto dto) {
        //业务数据校验
        if(dto==null||dto.getTyMasterGrabChainInfoDto()==null
                ||dto.getTyMasterGrabChainInfoDto().size()==0){
            throw new RException("缺少必填参数",Constant.CODE_VALIDFAIL);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();

        List<TyMasterGrabChainInfoDto> list=dto.getTyMasterGrabChainInfoDto();
        for ( TyMasterGrabChainInfoDto chainInfoDto:list
             ) {
            //单个合约保存
            /*
            //根据小微code 和合约判断是否已抢单
            ZContracts existsContract=contractsService.getOne(new QueryWrapper<ZContracts>()
                    .eq("parent_id",chainInfoDto.getContractId())
                    .eq("contract_type","20")
                    .eq("xiaowei_code",currentUser.getXwCode())
            );
            if(existsContract!=null){
                throw new RException("该用户已抢单，请勿重复抢单",Constant.CODE_VALIDFAIL);
            }


            if(contracts==null){
                throw new RException("合约"+Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
            }
            */
            ZContracts contracts=contractsService.getById(chainInfoDto.getContractId());
            contracts.setParentId(chainInfoDto.getContractId());
            contracts.setId(0);
            contracts.setContractName(chainInfoDto.getChainName());
            contracts.setContractType("20");
            contracts.setCreateCode(currentUser.getEmpsn());
            contracts.setCreateName(currentUser.getEmpname());
            contracts.setCreateTime(new Date());
            contracts.setStatus("1");
            contracts.setRegionCode(chainInfoDto.getRegionCode());
            contracts.setOrgCode(currentUser.getOrgNum());
            contracts.setOrgName(currentUser.getOrgName());
            contracts.setOrgType(currentUser.getOrgType());
            contracts.setXiaoweiCode(currentUser.getXwCode());
            contractsService.save(contracts);

            List<ZContractsFactor> factors=new ArrayList<>();
            factors=chainInfoDto.getTargetList().stream().map(
                    m->{
                        ZContractsFactor factor=new ZContractsFactor();
                        factor.setContractId(contracts.getId());
                        factor.setFactorCode(m.getFactorCode());
                        factor.setFactorName(m.getFactorName());
                        factor.setFactorValue(m.getFactorValue());
                        factor.setFactorUnit(m.getFactorUnit());
                        factor.setFactorType(Constant.FactorType.Bottom.getValue());
                        factor.setMeshCode(contracts.getId().toString());//汇总
                        return factor;
                    }
            ).collect(Collectors.toList());

            factors.addAll(
                    chainInfoDto.getGrabList().stream().map(
                            m->{
                                ZContractsFactor factor=new ZContractsFactor();
                                factor.setContractId(contracts.getId());
                                factor.setFactorCode(m.getFactorCode());
                                factor.setFactorName(m.getFactorName());
                                factor.setFactorValue(m.getFactorValue());
                                factor.setFactorUnit(m.getFactorUnit());
                                factor.setFactorType(Constant.FactorType.Grab.getValue());
                                factor.setMeshCode(contracts.getId().toString());//汇总
                                return factor;
                            }
                    ).collect(Collectors.toList())
            );
            contractsFactorService.saveBatch(factors);

        }

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

    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(Integer contractId) {
        ZContracts contracts = contractsService.getById(contractId);
        if (contracts == null) {
            throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
        }

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto = new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(contracts.getId());

        tyMasterGrabChainInfoDto.setContractName(contracts.getContractName());
        tyMasterGrabChainInfoDto.setContractOwner(contracts.getCreateName());
        tyMasterGrabChainInfoDto.setChainName(contracts.getContractName());
        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setGrabEnd(DateFormatUtil.format(contracts.getJoinTime()
                ,DateFormatUtil.DATE_TIME_PATTERN));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());
        List<ZContractsFactor> factors = contractsFactorService.list(
                new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId())

        );
        List<FactorDto> targetFactor = factors.stream().filter(m->Constant.FactorType.Bottom
                .getValue().equals(m.getMeshCode())).map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            return dto;
        }).collect(Collectors.toList());

        //网格抢单汇总
        List<FactorDto> grabFactors = factors.stream().filter(m->Constant.FactorType.Grab
                .getValue().equals(m.getMeshCode())).map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            return dto;
        }).collect(Collectors.toList());
        tyMasterGrabChainInfoDto.setTargetList(targetFactor);
        tyMasterGrabChainInfoDto.setGrabList(grabFactors);

        return tyMasterGrabChainInfoDto;
    }

    @Override
    public List<MeshGrabInfoDto> queryMeshGrabDetail(Integer contractId) {
        ZContracts contracts = contractsService.getById(contractId);
        if (contracts == null) {
            throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
        }
        TyMasterGrabQueryDto queryDto=new TyMasterGrabQueryDto();

        queryDto.setContractId(contracts.getParentId());
        perfectQueryParam(queryDto);
        queryDto.setLoginXwCode(contracts.getXiaoweiCode());
        return this.queryMeshGrabDetail(queryDto);
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
