package com.haier.hailian.contract.service;

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

    // 同步ods_minbu最新数据到node表  一小时一次
    void quartzMinbuListByXwType3();

    void checkWarning();

}