package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.ZNetBottom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-17
 */
public interface ZNetBottomService extends IService<ZNetBottom> {
    List<MeshGrabEntity> queryMeshBottomIncome(MeshStatisticQueryDto queryDto);
}
