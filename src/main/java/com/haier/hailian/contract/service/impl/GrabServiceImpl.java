package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.grab.MeshGrabInfoDto;
import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.dto.grab.MeshSummaryDto;
import com.haier.hailian.contract.dto.grab.TyMasterGrabChainInfoDto;
import com.haier.hailian.contract.entity.MonthChainGroupOrder;
import com.haier.hailian.contract.entity.SysNet;
import com.haier.hailian.contract.entity.ZNetBottom;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.MonthChainGroupOrderService;
import com.haier.hailian.contract.service.SysNetService;
import com.haier.hailian.contract.service.ZNetBottomService;
import com.haier.hailian.contract.util.AmountFormat;
import com.haier.hailian.contract.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Override
    public TyMasterGrabChainInfoDto queryChainInfo(MeshStatisticQueryDto queryDto) {
        return null;
    }

    @Override
    public MeshSummaryDto queryMeshGrabDetail(MeshStatisticQueryDto queryDto) {
        //小微下所有的网格
        List<SysNet> sysNets=sysNetService.queryByXwcode(queryDto.getXwCode());
        List<String> wgCodes=sysNets.stream().map(code->code.getJytCode())
                .distinct().collect(Collectors.toList());
        //网格对应年月抢单数据
        List<MonthChainGroupOrder> chainGroupOrders=monthChainGroupOrderService.list(
                new QueryWrapper<MonthChainGroupOrder>().eq("xw_code",queryDto.getXwCode())
                .eq("year",queryDto.getYear())
                .in("month", queryDto.getMonth())
                .in("wg_code",wgCodes)
        );
        //网格对应年月目标数据
        List<ZNetBottom> bottoms=netBottomService.list(
                new QueryWrapper<ZNetBottom>().in("period_code",queryDto.getYearMonth())
                .in("pmai_area_code", wgCodes)
        );
        //某个合约的高端产品
        List<String> highProds=monthChainGroupOrderService.getProductByContract(
                queryDto.getContractId(), Constant.ProductStru.High.getValue(),
                queryDto.getYearMonth()
        );
        List<String> lowProds=monthChainGroupOrderService.getProductByContract(
                queryDto.getContractId(), Constant.ProductStru.Low.getValue(),
                queryDto.getYearMonth()
        );
        //某个合约的低端产品
        List<MeshGrabInfoDto> meshGrabInfoDtos=new ArrayList<>();
        for ( String wg:wgCodes) {
            MeshGrabInfoDto meshGrabDto=new MeshGrabInfoDto();

            SysNet curSysNet=sysNets.stream().filter(
                    f->wg.equals(f.getJytCode())
            ).findFirst().get();
            meshGrabDto.setMeshCode(curSysNet.getJytCode());
            meshGrabDto.setMeshCode(curSysNet.getJytName());

            List<MonthChainGroupOrder> curChainOrder=chainGroupOrders.stream()
                    .filter((f->wg.equals(f.getWgCode()))).collect(Collectors.toList());
            double grabIncome=0d ,highIncome=0d, lowIncome=0d;
            grabIncome=curChainOrder.stream().mapToDouble(
                    m-> AmountFormat.amtStr2D(m.getOrderAmt())).sum();
            meshGrabDto.setIncome(new BigDecimal(grabIncome));

            //高、低端抢单收入
            highIncome=curChainOrder.stream().filter(f->highProds.contains(f.getProductCode()))
                    .mapToDouble(m-> AmountFormat.amtStr2D(m.getOrderAmt())).sum();
            lowIncome=curChainOrder.stream().filter(f->lowProds.contains(f.getProductCode()))
                    .mapToDouble(m-> AmountFormat.amtStr2D(m.getOrderAmt())).sum();

            meshGrabDto.setStruHighPercent(new BigDecimal(highIncome).divide(
                    meshGrabDto.getIncome() ));
            meshGrabDto.setStruLowPercent(new BigDecimal(lowIncome).divide(
                    meshGrabDto.getIncome() ));
            meshGrabInfoDtos.add(meshGrabDto);
        }
        return null;
    }
}
