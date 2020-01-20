package com.haier.hailian.contract.service;

import com.haier.hailian.contract.entity.ZWaringPeriodConfig;

import java.util.List;

/**
 * 链群抢单举单预警配置表(ZWaringPeriodConfig)表服务接口
 *
 * @author makejava
 * @since 2020-01-17 09:22:04
 */
public interface ZWaringPeriodConfigService {


    //     * 举单预警（开启前一次/天）
    void jdWarning();

    //     * 抢单预警（开启前一次/天）
    void qdWarning();

}