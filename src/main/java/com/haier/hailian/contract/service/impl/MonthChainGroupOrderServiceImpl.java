package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.MonthChainGroupOrder;
import com.haier.hailian.contract.dao.MonthChainGroupOrderDao;
import com.haier.hailian.contract.service.MonthChainGroupOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-17
 */
@Service
public class MonthChainGroupOrderServiceImpl extends ServiceImpl<MonthChainGroupOrderDao, MonthChainGroupOrder> implements MonthChainGroupOrderService {
    @Override
    public List<String> getProductByContract(Integer contractId, String productStru, List<String> yearMonth) {
        return baseMapper.getProductByContract(contractId,productStru,yearMonth);
    }

    @Override
    public List<MeshGrabEntity> queryMeshGrabIncome(TyMasterGrabQueryDto queryDto) {
        return baseMapper.queryMeshGrabIncome(queryDto);
    }

    @Override
    public List<MeshGrabEntity> sumStruMeshGrabIncome(TyMasterGrabQueryDto queryDto) {
        return baseMapper.sumStruMeshGrabIncome(queryDto);
    }

    @Override
    public List<MeshGrabEntity> queryMeshE2EIncome(TyMasterGrabQueryDto queryDto) {
        return baseMapper.queryMeshE2EIncome(queryDto);
    }
}
