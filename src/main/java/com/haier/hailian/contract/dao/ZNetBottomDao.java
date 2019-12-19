package com.haier.hailian.contract.dao;

import com.haier.hailian.contract.dto.grab.MeshStatisticQueryDto;
import com.haier.hailian.contract.entity.MeshGrabEntity;
import com.haier.hailian.contract.entity.ZNetBottom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 19012964
 * @since 2019-12-17
 */
public interface ZNetBottomDao extends BaseMapper<ZNetBottom> {

    /**
     * 查询网格目标收入
     * @param queryDto
     * @return
     */
    List<MeshGrabEntity> queryMeshBottomIncome(MeshStatisticQueryDto queryDto);
}
