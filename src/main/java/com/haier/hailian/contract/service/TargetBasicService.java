package com.haier.hailian.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.entity.TargetBasic;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
public interface TargetBasicService extends IService<TargetBasic> {

    List<TargetBasic> selectBottom(QueryBottomDTO dto);
}
