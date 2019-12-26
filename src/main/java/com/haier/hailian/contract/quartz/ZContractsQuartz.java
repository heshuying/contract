package com.haier.hailian.contract.quartz;

import com.haier.hailian.contract.service.GrabService;
import com.haier.hailian.contract.service.ZContractsService;
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
}

