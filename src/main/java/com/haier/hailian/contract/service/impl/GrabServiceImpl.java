package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dao.ZNodeTargetPercentInfoDao;
import com.haier.hailian.contract.dto.FactorDto;
import com.haier.hailian.contract.dto.R;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.grab.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.*;
import com.haier.hailian.contract.util.AmountFormat;
import com.haier.hailian.contract.util.Constant;
import com.haier.hailian.contract.util.DateFormatUtil;
import com.haier.hailian.contract.util.IHaierUtil;
import io.swagger.models.auth.In;
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

    /**
     * 已抢入列表
     * @param queryDto
     * @return
     */
    @Override
    public List<TyMasterGrabChainInfoDto> queryMyChainList(TyGrabListQueryDto queryDto) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu minBu = sysUser.getMinbu();
        List<TyMasterGrabChainInfoDto> list=new ArrayList<>();
        queryDto.setEmpSn(sysUser.getEmpSn());

        List<ZContracts> contracts= contractsService.queryTyMyGrabList(queryDto);

        for ( ZContracts contract: contracts
                ) {
            TyMasterGrabChainInfoDto grabDto=this.queryChainInfo(contract.getId());

            list.add(grabDto);
        }
        return list;
    }

    /**
     * 待抢入列表
     * @param query
     * @return
     */
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
            query.setLoginBuCode(minbu.getLittleXwCode());
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

    /**
     * 单个合约的信息和指标计算，抢单之前
     * @param queryDto
     * @return
     */
    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(TyMasterGrabQueryDto queryDto) {
        ZContracts contracts = contractsService.getById(queryDto.getContractId());
        if (contracts == null) {
            throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
        }
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        TOdsMinbu minBu = sysUser.getMinbu();

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto = new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(queryDto.getContractId());
        String chainName = contracts.getContractName();
        if(StringUtils.isNotBlank(minBu.getRegionName())) {
            chainName = chainName.replace("链群", "-" + minBu.getRegionName() + "链群");
        }
        tyMasterGrabChainInfoDto.setRegionCode(minBu.getRegionCode());
        tyMasterGrabChainInfoDto.setLittleXwName(minBu.getLittleXwName());
        ZHrChainInfo chainInfo=chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code", contracts.getChainCode()));
        if("1".equals(chainInfo.getGrabFlag()) ){
            //链群闸口
            tyMasterGrabChainInfoDto.setCanEdit(true);
        }else{
            if(!minBu.isIn42Center()){
                tyMasterGrabChainInfoDto.setCanEdit(true);
            }else {
                tyMasterGrabChainInfoDto.setCanEdit(false);
            }
        }
        tyMasterGrabChainInfoDto.setContractName(chainName);
        tyMasterGrabChainInfoDto.setContractOwner(contracts.getCreateName());
        tyMasterGrabChainInfoDto.setChainName(chainInfo.getChainName());
        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setGrabEnd(DateFormatUtil.format(contracts.getJoinTime()
                ,DateFormatUtil.DATE_TIME_PATTERN));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());
        tyMasterGrabChainInfoDto.setShareMoney(contracts.getShareMoney());
        //举单合约保存42中心数据，只取当前登录人所属中心
        List<ZContractsFactor> factors = contractsFactorService.list(
                new QueryWrapper<ZContractsFactor>().eq("contract_id", contracts.getId())
                        .eq("mesh_code", minBu.getLittleXwCode())
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
        tyMasterGrabChainInfoDto.setTargetList(targetFactor);

        //根据目标维度 设置抢单的维度
        List<FactorDto> grabFactors = new ArrayList<>();
        List<FactorDto> e2eFactors = new ArrayList<>();
        perfectQueryParam(queryDto);
        queryDto.setLoginXwCode(minBu.getXwCode());
        queryDto.setRegionCode(minBu.getRegionCode());

        //e2e收入
        BigDecimal e2eInc = calE2e(queryDto);
        if(!tyMasterGrabChainInfoDto.getCanEdit()) {
            //42中心，从OMS汇总取数

            //抢单收入
            List<MeshGrabEntity> meshGrabEntities = monthChainGroupOrderService.sumStruMeshGrabIncome(queryDto);
            BigDecimal inc = new BigDecimal(meshGrabEntities.stream().mapToDouble(m ->
                    AmountFormat.amtStr2D(m.getIncome())).sum());
            BigDecimal sumSales = new BigDecimal(meshGrabEntities.stream().mapToDouble(m ->
                    AmountFormat.amtStr2D(m.getSales())).sum());

            String tip = "";
            for (FactorDto index : targetFactor) {
                FactorDto e2eFactor = new FactorDto();
                FactorDto grabFactor = new FactorDto();
                BeanUtils.copyProperties(index, grabFactor);
                BeanUtils.copyProperties(index, e2eFactor);
                grabFactor.setFactorType(Constant.FactorType.Grab.getValue());
                e2eFactor.setFactorType(Constant.FactorType.E2E.getValue());
                if (Constant.FactorCode.Incom.getValue().equals(index.getFactorCode())) {
                    BigDecimal incBill = inc.divide(
                            new BigDecimal("10000"), 2, RoundingMode.HALF_UP
                    );
                    grabFactor.setFactorValue(incBill.toString());//格式化万
                    e2eFactor.setFactorValue(e2eInc.toString());

                } else if (Constant.FactorCode.HighPercent.getValue().equals(index.getFactorCode())) {
                    List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f ->
                            Constant.ProductStru.High.getValue().equals(f.getProductStru()))
                            .collect(Collectors.toList());
                    if (curr != null && curr.size() > 0) {
                        BigDecimal currIncom = new BigDecimal(curr.stream().mapToDouble(m ->
                                AmountFormat.amtStr2D(m.getSales())).sum());
                        grabFactor.setFactorValue(AmountFormat.amountFormat(currIncom
                                .divide(sumSales, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100")).toString(), 2)
                        );
                    } else {
                        grabFactor.setFactorValue("0");
                    }
                    e2eFactor.setFactorValue("0");
                } else if (Constant.FactorCode.LowPercent.getValue().equals(index.getFactorCode())) {
                    List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f ->
                            Constant.ProductStru.Low.getValue().equals(f.getProductStru()))
                            .collect(Collectors.toList());
                    if (curr != null && curr.size() > 0) {
                        BigDecimal currIncom = new BigDecimal(curr.stream().mapToDouble(m ->
                                AmountFormat.amtStr2D(m.getSales())).sum());
                        grabFactor.setFactorValue(AmountFormat.amountFormat(currIncom
                                .divide(sumSales, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100")).toString(), 2)
                        );
                    } else {
                        grabFactor.setFactorValue("0");
                    }
                    e2eFactor.setFactorValue("0");
                } else if (Constant.FactorCode.MiddPercent.getValue().equals(index.getFactorCode())) {
                    List<MeshGrabEntity> curr = meshGrabEntities.stream().filter(f ->
                            Constant.ProductStru.Midd.getValue().equals(f.getProductStru()))
                            .collect(Collectors.toList());
                    if (curr != null && curr.size() > 0) {
                        BigDecimal currIncom = new BigDecimal(curr.stream().mapToDouble(m ->
                                AmountFormat.amtStr2D(m.getSales())).sum());
                        grabFactor.setFactorValue(AmountFormat.amountFormat(currIncom
                                .divide(sumSales, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100")).toString(), 2)
                        );
                    } else {
                        grabFactor.setFactorValue("0");
                    }
                    e2eFactor.setFactorValue("0");
                } else {
                    grabFactor.setFactorValue("0");
                    grabFactor.setHasInput(true);
                    e2eFactor.setFactorValue("0");
                }
                grabFactor.setDirection(this.compareTarget(index.getFactorValue(),
                        grabFactor.getFactorValue(), e2eFactor.getFactorValue()));
                if (Constant.CompareResult.LT.getValue().equals(grabFactor.getDirection())) {
                    if (StringUtils.isBlank(tip)) {
                        tip = grabFactor.getFactorName();
                    } else {
                        tip = tip + "、" + grabFactor.getFactorName();
                    }
                }
                e2eFactors.add(e2eFactor);
                grabFactors.add(grabFactor);
            }
            if (StringUtils.isNoneBlank(tip)) {
                //存在抢入小于目标
                tyMasterGrabChainInfoDto.setCanSubmit(false);
                tyMasterGrabChainInfoDto.setErrorMsg("抢单" +
                        tip + "低于底线/E2E，此时抢入会没有分享酬或少分享酬");
            }
            tyMasterGrabChainInfoDto.setE2eList(e2eFactors);
            tyMasterGrabChainInfoDto.setGrabList(grabFactors);
        }else{
            //非OMS汇总取数
            for (FactorDto index : targetFactor) {
                FactorDto e2eFactor = new FactorDto();
                FactorDto grabFactor = new FactorDto();
                BeanUtils.copyProperties(index, grabFactor);
                BeanUtils.copyProperties(index, e2eFactor);
                if (Constant.FactorCode.Incom.getValue().equals(index.getFactorCode())) {
                    grabFactor.setFactorValue("0");
                    e2eFactor.setFactorValue(e2eInc.toString());
                }else {
                    grabFactor.setFactorValue("0");
                    e2eFactor.setFactorValue("0");
                }
                e2eFactors.add(e2eFactor);
                grabFactors.add(grabFactor);
            }
            tyMasterGrabChainInfoDto.setE2eList(e2eFactors);
            tyMasterGrabChainInfoDto.setGrabList(grabFactors);
        }
        return tyMasterGrabChainInfoDto;
    }

    private BigDecimal calE2e(TyMasterGrabQueryDto queryDto){
        BigDecimal e2eInc=BigDecimal.ZERO;
        //e2e收入
        List<MeshGrabEntity> meshE2EEntities = monthChainGroupOrderService.queryMeshE2EIncome(queryDto);
        if(meshE2EEntities!=null||meshE2EEntities.size()>0){
            if(meshE2EEntities.get(0)!=null&&meshE2EEntities.get(0).getIncome()!=null){
                e2eInc = new BigDecimal(meshE2EEntities.get(0).getIncome());
            }

        }
        return e2eInc.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @param target
     * @param grab
     * @param e2e
     * @return
     */
    private String compareTarget(String target, String grab, String e2e){
        BigDecimal bdTarget=BigDecimal.ZERO;
        BigDecimal bdGrab=BigDecimal.ZERO;
        BigDecimal bdE2e=BigDecimal.ZERO;
        if(StringUtils.isNoneBlank(target)){
            bdTarget=new BigDecimal(target);
        }
        if(StringUtils.isNoneBlank(grab)){
            bdGrab=new BigDecimal(grab);
        }
        if(StringUtils.isNoneBlank(e2e)){
            bdE2e=new BigDecimal(e2e);
        }

        if(bdGrab.compareTo(bdTarget)>0&&bdGrab.compareTo(bdE2e)>0){
            return Constant.CompareResult.GT.getValue();
        }else if(bdGrab.compareTo(bdTarget)==0){
            return Constant.CompareResult.EQ.getValue();
        }else{
            return  Constant.CompareResult.LT.getValue();
        }
    }

    /**
     * 计算网格抢单收入和高端占比
     * @param queryDto
     * @return
     */
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
        TOdsMinbu minbu = sysUser.getMinbu();
        queryDto.setLoginXwCode(minbu.getXwCode());
        queryDto.setRegionCode(minbu.getRegionCode());

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

    /**
     * 体验抢单保存
     * @param dto
     * @return
     */
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
        TOdsMinbu minbBu = sysUser.getMinbu();

        List<TyMasterGrabChainInfoDto> list=dto.getTyMasterGrabChainInfoDto();
        //循环待抢入的举单合约
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
            contracts.setContractName(chainInfoDto.getContractName());
            contracts.setContractType("20");
            contracts.setCreateCode(sysUser.getEmpSn());
            contracts.setCreateName(sysUser.getEmpName());
            contracts.setCreateTime(new Date());
            contracts.setStatus("1");
            contracts.setRegionCode(chainInfoDto.getRegionCode());
            contracts.setOrgCode(minbBu.getLittleXwCode());
            contracts.setOrgName(minbBu.getLittleXwName());
            contracts.setOrgType(minbBu.getXwType3Code());
            contracts.setXiaoweiCode(minbBu.getXwCode());
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
                                return factor;
                            }
                    ).collect(Collectors.toList())
            );
            factors.addAll(
                    chainInfoDto.getE2eList().stream().map(
                            m->{
                                ZContractsFactor factor=new ZContractsFactor();
                                factor.setContractId(contracts.getId());
                                factor.setFactorCode(m.getFactorCode());
                                factor.setFactorName(m.getFactorName());
                                factor.setFactorValue(m.getFactorValue());
                                factor.setFactorUnit(m.getFactorUnit());
                                factor.setFactorType(Constant.FactorType.E2E.getValue());
                                return factor;
                            }
                    ).collect(Collectors.toList())
            );
            contractsFactorService.saveBatch(factors);

            //加入群组
            ZHrChainInfo chainInfo=chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                    .eq("chain_code", contracts.getChainCode()));
            if(chainInfo!=null&&StringUtils.isNoneBlank(chainInfo.getGroupId())){
                String groupId=chainInfo.getGroupId();
                String[] users=new String[]{sysUser.getEmpSn()};
                IHaierUtil.joinGroup(groupId, users);
            }
            //异步上链
            new Thread(new Runnable(){
                public void run(){
                    chainCommonService.uploadBigContract(contracts.getId());
                }
            }).start();

        }

        return R.ok();
    }

    /**
     * 合约失效
     */
    @Override
    public void refreshContractStatusJob() {
        //截止抢入时间 仍然处于抢入中的合约
        List<ZContracts> list=contractsService.list(new QueryWrapper<ZContracts>()
                .eq("contract_type","10")
                .eq("status","0")
                .lt("join_time",new Date())
        );

        List<ZContracts> updateList=new ArrayList<>();
        List<Integer> mainContracts=new ArrayList<>();
        for (ZContracts contract:list) {
            if(contract.getParentId()>0){
                mainContracts.add(contract.getParentId());
            }
            ZContracts updateContract=new ZContracts();
            updateContract.setId(contract.getId());
            List<ZContracts> sub=contractsService.list(new QueryWrapper<ZContracts>()
                    .eq("parent_id", contract.getId())
                    .in("contract_type", new String[]{"20","30"})
                    .in("status", new Integer[]{1,8})
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
        if(updateList!=null&&updateList.size()>0) {
            contractsService.updateBatchById(updateList);
        }

        if(mainContracts.size()>0){
            //判断子合约的状态
            List<Integer> ids=mainContracts.stream().distinct().collect(Collectors.toList());
            List<ZContracts> mainUpdates=new ArrayList<>();
            for (Integer id :ids){
                List<ZContracts> sub=contractsService.list(new QueryWrapper<ZContracts>()
                        .eq("parent_id", id)
                        .eq("contract_type", "10")
                        .eq("status", "1")
                        .last("limit 1")
                );
                if(sub.size()>0){
                    ZContracts willUpdate=new ZContracts();
                    willUpdate.setStatus("1");
                    willUpdate.setId(id);
                    mainUpdates.add(willUpdate);
                }
            }
            contractsService.updateBatchById(mainUpdates);
        }
        expiredContract();
    }
    
    /**
     * 链群主复核截止时间一过，抢单更新为8 抢入成功
     */
    public void refreshGrabStatus() {
        //链群主复核时间过了，状态为1的抢单
        List<ZContracts> list=contractsService.list(new QueryWrapper<ZContracts>()
                .in("contract_type",new String[]{"20","30"})
                .eq("status","1")
                .lt("check_time",new Date())
        );
        List<ZContracts> updateList=new ArrayList<>();
        for (ZContracts contract:list) {

            ZContracts updateContract=new ZContracts();
            updateContract.setId(contract.getId());
            updateContract.setStatus("8");
            updateList.add(updateContract);
        }
        log.info("========刷新抢单状态为抢入成功=====");
        log.info("数据：{}", updateList);
        if(updateList!=null&&updateList.size()>0) {
            contractsService.updateBatchById(updateList);
        }

    }

    /**
     * 合约过期
     */
    private void expiredContract(){
        //截止抢入时间 仍然处于抢入中的合约
        List<ZContracts> list=contractsService.list(new QueryWrapper<ZContracts>()
                .eq("contract_type","10")
                .eq("status","1")
                .lt("end_date",new Date())
        );
        List<ZContracts> updateList=new ArrayList<>();

        for (ZContracts contract:list) {
            ZContracts updateContract=new ZContracts();
            updateContract.setId(contract.getId());
            updateContract.setStatus("7");

            updateList.add(updateContract);
        }
        log.info("========刷新合约过期状态=====");
        log.info("数据：{}", updateList);
        if(updateList!=null&&updateList.size()>0) {
            contractsService.updateBatchById(updateList);
        }


    }

    /**
     * 根据合约id，查询合约信息和合约指标
     * @param contractId
     * @return
     */
    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(Integer contractId) {
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        TOdsMinbu minBu = sysUser.getMinbu();

        ZContracts contracts = contractsService.getById(contractId);
        if (contracts == null) {
            throw new RException("合约" + Constant.MSG_DATA_NOTFOUND, Constant.CODE_DATA_NOTFOUND);
        }

        TyMasterGrabChainInfoDto tyMasterGrabChainInfoDto = new TyMasterGrabChainInfoDto();
        tyMasterGrabChainInfoDto.setContractId(contracts.getId());

        tyMasterGrabChainInfoDto.setContractName(contracts.getContractName());
        ZHrChainInfo chainInfo=chainInfoDao.selectOne(new QueryWrapper<ZHrChainInfo>()
                .eq("chain_code", contracts.getChainCode()));
        if("1".equals(chainInfo.getGrabFlag()) ){
            //链群闸口
            tyMasterGrabChainInfoDto.setCanEdit(true);
        }else{
            if(!minBu.isIn42Center()){
                tyMasterGrabChainInfoDto.setCanEdit(true);
            }else {
                tyMasterGrabChainInfoDto.setCanEdit(false);
            }
        }
        tyMasterGrabChainInfoDto.setChainName(chainInfo.getChainName());
        tyMasterGrabChainInfoDto.setContractOwner(chainInfo.getMasterName());
        tyMasterGrabChainInfoDto.setLittleXwName(minBu.getLittleXwName());

        tyMasterGrabChainInfoDto.setStart(
                DateFormatUtil.format(contracts.getStartDate()));
        tyMasterGrabChainInfoDto.setEnd(
                DateFormatUtil.format(contracts.getEndDate()));
        tyMasterGrabChainInfoDto.setGrabEnd(DateFormatUtil.format(contracts.getJoinTime()
                ,DateFormatUtil.DATE_TIME_PATTERN));
        tyMasterGrabChainInfoDto.setShareQuota(contracts.getShareSpace());
        tyMasterGrabChainInfoDto.setShareMoney(contracts.getShareMoney());
        List<ZContractsFactor> factors = contractsFactorService.list(
                new QueryWrapper<ZContractsFactor>()
                        .eq("contract_id", contracts.getId())
        );
        List<FactorDto> targetFactor = factors.stream().filter(m->Constant.FactorType.Bottom
                .getValue().equals(m.getFactorType())).map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            return dto;
        }).collect(Collectors.toList());
        //网格E2E抢单汇总
        List<FactorDto> e2eFactors = factors.stream().filter(m->Constant.FactorType.E2E
                .getValue().equals(m.getFactorType())).map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            return dto;
        }).collect(Collectors.toList());
        //网格抢单汇总
        List<FactorDto> grabFactors = factors.stream().filter(m->Constant.FactorType.Grab
                .getValue().equals(m.getFactorType())).map(m -> {
            FactorDto dto = new FactorDto();
            dto.setFactorCode(m.getFactorCode());
            dto.setFactorName(m.getFactorName());
            dto.setFactorValue(m.getFactorValue());
            dto.setFactorUnit(m.getFactorUnit());
            return dto;
        }).collect(Collectors.toList());

        for(FactorDto currGrab : grabFactors){
            List<FactorDto> currBottom=targetFactor.stream()
                    .filter(m->m.getFactorCode().equals(currGrab.getFactorCode())).collect(Collectors.toList());
            List<FactorDto> currE2e=e2eFactors.stream()
                    .filter(m->m.getFactorCode().equals(currGrab.getFactorCode())).collect(Collectors.toList());
            String bottomVal="0";
            if(currBottom!=null&&currBottom.size()>0){
                bottomVal=currBottom.get(0).getFactorValue();
            }
            String e2eVal="0";
            if(currE2e!=null&&currE2e.size()>0){
                e2eVal=currE2e.get(0).getFactorValue();
            }

            currGrab.setDirection(this.compareTarget(bottomVal,currGrab.getFactorValue(), e2eVal));
        }
        tyMasterGrabChainInfoDto.setTargetList(targetFactor);
        tyMasterGrabChainInfoDto.setGrabList(grabFactors);
        tyMasterGrabChainInfoDto.setE2eList(e2eFactors);
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
        queryDTO.setChainCode(contracts.getChainCode());
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
