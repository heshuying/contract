package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.grab.TyMasterGrabQueryDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.ZNetBottom;
import com.haier.hailian.contract.dao.ZNetBottomDao;
import com.haier.hailian.contract.service.ZNetBottomService;
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
public class ZNetBottomServiceImpl extends ServiceImpl<ZNetBottomDao, ZNetBottom> implements ZNetBottomService {
    @Override
    public List<MeshGrabEntity> queryMeshBottomIncome(TyMasterGrabQueryDto queryDto) {
        return baseMapper.queryMeshBottomIncome(queryDto);
    }
}
