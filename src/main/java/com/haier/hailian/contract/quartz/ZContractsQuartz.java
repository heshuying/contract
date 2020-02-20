package com.haier.hailian.contract.quartz;

import com.haier.hailian.contract.entity.ZReservePlanTeamwork;
import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.ZContractsService;
import com.haier.hailian.contract.service.ZReservePlanTeamworkService;
import com.haier.hailian.contract.service.ZWaringPeriodConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by 19012964 on 2019/12/26.
 */
@Component
public class ZContractsQuartz {
    private final Logger log =  LoggerFactory.getLogger(ZContractsQuartz.class);
    @Autowired
    private GrabService grabService;
    @Autowired
    private ZReservePlanTeamworkService zReservePlanTeamworkService;
    @Autowired
    private ZWaringPeriodConfigService zWaringPeriodConfigService;

    /**
     * 每天零点刷新合约状态
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void doRefreshContractStatus(){
        log.info("【凌晨1点刷新合约状态任务开始】");
        grabService.refreshContractStatusJob();
        log.info("【凌晨1点刷新合约状态任务结束】");
    }


    /**
     * 每天14点计算用户场景数据
     */
    @Scheduled(cron="0 0 14 * * ?")
    public void doRefresh690(){
        //log.info("【定时任务开始-刷新690数据】");
        //xCalculateLogical.doRefresh690();
        //log.info("【定时任务结束--刷新690数据】");
    }
    @Scheduled(cron="0 0/10 * * * ?")
    public void createGroup(){
        log.info("【创建合约的群组开始】");
        zReservePlanTeamworkService.createGroup();
        log.info("【创建合约的群组结束】");
    }

    /**
     * 合约达成变更合约状态
     */
//    @Scheduled(cron="0 0/10 * * * ?")
    public void createContracts(){
        log.info("【合约达成开始】");
        zReservePlanTeamworkService.createContracts();
        log.info("【合约达成结束】");
    }

    /**
     * 举单预警（开启前一次/天）
     */
    //    @Scheduled(cron="0 0/10 * * * ?")
    public void jdWarning(){
        log.info("【合约达成开始】");
        zWaringPeriodConfigService.jdWarning();
        log.info("【合约达成结束】");
    }


    /**
     * 抢单预警（开启前一次/天）
     */
    //    @Scheduled(cron="0 0/10 * * * ?")
    public void qdWarning(){
        log.info("【合约达成开始】");
        zWaringPeriodConfigService.qdWarning();
        log.info("【合约达成结束】");
    }
}

