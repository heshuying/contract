package com.haier.hailian.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.dto.TargetBasicInfo;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.XiaoweiEhr;

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

    List<TargetBasic> selectContractsFirstTarget(QueryBottomDTO dto);

    List<TargetBasic> selectContractsSecondTarget(QueryBottomDTO dto);

    int updateContractsTarget(List<TargetBasic> targetBasicList);

    int insertContractsTarget(List<TargetBasic> targetBasicList);

    List<TargetBasicInfo> selectContractsTarget(QueryBottomDTO dto);

    int deleteContractsTarget(Integer id);

    List<SysXiaoweiEhr> selectXwAll(XiaoweiEhr xiaoweiEhr);

    int getNum();
}
