package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.entity.ZContracts;
import com.haier.hailian.contract.dao.ZContractsDao;
import com.haier.hailian.contract.service.ZContractsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
@Service
public class ZContractsServiceImpl extends ServiceImpl<ZContractsDao, ZContracts> implements ZContractsService {
    @Override
    public List<ZContracts> queryTyGrabList(TyGrabListQueryDto queryDto) {
        return baseMapper.queryTyGrabList(queryDto);
    }

    @Override
    public List<ZContracts> queryTyMyGrabList(TyGrabListQueryDto queryDto) {
        return baseMapper.queryTyMyGrabList(queryDto);
    }
}
