package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.grab.TyGrabListQueryDto;
import com.haier.hailian.contract.entity.ZContracts;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-19
 */
public interface ZContractsService extends IService<ZContracts> {
    List<ZContracts> queryTyGrabList(TyGrabListQueryDto queryDto);
    List<ZContracts>queryTyMyGrabList(TyGrabListQueryDto queryDto);
}
