package com.haier.hailian.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import com.haier.hailian.contract.entity.XiaoweiEhr;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 01431594
 * @since 2019-12-20
 */
public interface SysXiaoweiEhrDao extends BaseMapper<SysXiaoweiEhr> {

    List<XiaoweiEhr> selectMarket();

    List<SysXiaoweiEhr> queryAll(XiaoweiEhr xiaoweiEhr);

}
