package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZWaringPeriodConfigService;
import com.haier.hailian.contract.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 链群抢单举单预警配置表(ZWaringPeriodConfig)表服务实现类
 *
 * @author makejava
 * @since 2020-01-17 09:22:04
 */
@Service("zWaringPeriodConfigService")
@Slf4j
public class ZWaringPeriodConfigServiceImpl implements ZWaringPeriodConfigService {
    @Resource
    private ZWaringPeriodConfigDao zWaringPeriodConfigDao;
    @Resource
    private ZContractsDao zContractsDao;
    @Resource
    private ZHrChainInfoDao zHrChainInfoDao;
    @Resource
    private SysEmployeeEhrDao sysEmployeeEhrDao;
    @Resource
    private ZNodeTargetPercentInfoDao zNodeTargetPercentInfoDao;
    @Resource
    private TOdsMinbuDao tOdsMinbuDao;
    @Resource
    private SysXiaoweiEhrDao sysXiaoweiEhrDao;

    @Override
    public void jdWarning() {
        //1.查询预警列表
        ZWaringPeriodConfig zWaringPeriodConfig = new ZWaringPeriodConfig();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(new Date());
        zWaringPeriodConfig.setOpDate(dateStr);
        zWaringPeriodConfig.setType(0);//举单的类型
        List<ZWaringPeriodConfig> zWaringPeriodConfigList = zWaringPeriodConfigDao.queryAll(zWaringPeriodConfig);
        for (ZWaringPeriodConfig z : zWaringPeriodConfigList) {
            //是否有合约
            //获取本月第一天：
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 0);
            Date tempDate = calendar.getTime();
            String oneTime = dateFormat2.format(tempDate);
            ZContracts zContracts = zContractsDao.selectByChainCode(z.getChainCode(), oneTime);
            if (zContracts == null) {
                //2.查询链群
                ZHrChainInfo zHrChainInfo = new ZHrChainInfo();
                zHrChainInfo.setChainCode(z.getChainCode());
                List<ZHrChainInfo> zHrChainInfos = zHrChainInfoDao.queryAll(zHrChainInfo);
                if (zHrChainInfos.size() == 0) {
                    log.error("配置的预警链群(" + z.getChainCode() + ")不存在，请你看清楚了在配置一下好吗？别找我的麻烦！OK？");
                    break;
                }
                //3.发送邮件提醒

                Date startTime = z.getStartDate();
                Date endTime = z.getEndDate();
                long nowTime = (new Date()).getTime();
                //4.开始时间对比
                if (nowTime >= startTime.getTime() && nowTime < (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000)) && z.getIsSend() == 0) {//第一次执行
                    //发邮件给链群主
                    SysEmployeeEhr sysEmployeeEhr = sysEmployeeEhrDao.selectInfo(zHrChainInfos.get(0).getMasterCode());
                    // 发邮件
                    EmailUtil.transmitMsg(sysEmployeeEhr.getNotesmail(),"举单预警","您好，请尽快进行举单操作！");
                    System.out.println(sysEmployeeEhr.getEmpSn()+":"+sysEmployeeEhr.getNotesmail());
                    //更新状态为1
                    z.setIsSend(1);
                    zWaringPeriodConfigDao.update(z);

                } else if (nowTime >= startTime.getTime() && nowTime < (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000)) && z.getIsSend() == 1) {//第二次执行
                    //不发邮件更新状态为0
                    z.setIsSend(0);
                    zWaringPeriodConfigDao.update(z);
                } else
                    //5.结束时间对比
                    //两天
                    if (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000) <= nowTime && z.getIsSend() == 0) {
                        SysXiaoweiEhr sysXiaoweiEhr = sysXiaoweiEhrDao.selectOne(new QueryWrapper<SysXiaoweiEhr>()
                                .eq("xwCode",zHrChainInfos.get(0).getXwCode()));
                        //发邮件给平台主
                        SysEmployeeEhr sysEmployeeEhr = sysEmployeeEhrDao.selectInfo(sysXiaoweiEhr.getXwmastercode());
                        // 发邮件
                        EmailUtil.transmitMsg(sysEmployeeEhr.getNotesmail(),"举单预警","您好，请尽快进行举单操作！");
                        System.out.println(sysEmployeeEhr.getEmpSn()+":"+sysEmployeeEhr.getNotesmail());
                    } else if (endTime.getTime() < nowTime) {
                        //更新状态为2
                        z.setIsSend(2);
                        zWaringPeriodConfigDao.update(z);
                    }
            }
        }
    }


    @Override
    public void qdWarning() {
        ZWaringPeriodConfig zWaringPeriodConfig = new ZWaringPeriodConfig();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(new Date());
        zWaringPeriodConfig.setOpDate(dateStr);
        zWaringPeriodConfig.setType(1);//抢单的类型
        List<ZWaringPeriodConfig> zWaringPeriodConfigList = zWaringPeriodConfigDao.queryAll(zWaringPeriodConfig);
        for (ZWaringPeriodConfig z : zWaringPeriodConfigList) {
            //是否有合约
            //获取本月第一天：
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 0);
            Date tempDate = calendar.getTime();
            String oneTime = dateFormat2.format(tempDate);
            Date startTime = z.getStartDate();
            Date endTime = z.getEndDate();
            long nowTime = (new Date()).getTime();
            ZContracts zContracts = zContractsDao.selectByChainCode(z.getChainCode(), oneTime);
            //存在举单的情况下抢单开始
            if (zContracts != null) {
                //查询抢单的情况
                List<ZContracts> list = zContractsDao.selectUserList(zContracts.getId());
                List<String> cdList = new ArrayList<>();
                List<String> tyList = new ArrayList<>();
                //查询链群表，查看那些市场没有抢单
                List<ZNodeTargetPercentInfo> zNodeTargetPercentInfoList = zNodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>()
                        .eq("lq_code", zContracts.getChainCode()));
                for (ZContracts zContracts1 : list) {
                    if ("20".equals(zContracts1.getContractType())) {
                        tyList.add(zContracts1.getOrgCode());
                    } else if ("30".equals(zContracts1.getContractType())){
                        cdList.add(zContracts1.getOrgCode());
                    }
                }
                //第一天送邮件
                if (nowTime >= startTime.getTime() && nowTime < (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000)) && z.getIsSend() == 0) {
                    for (ZNodeTargetPercentInfo zNodeTargetPercentInfo : zNodeTargetPercentInfoList) {
                        if (!cdList.contains(zNodeTargetPercentInfo.getNodeCode())&& !tyList.contains(zNodeTargetPercentInfo.getNodeCode())) {
                            TOdsMinbu tOdsMinbu = tOdsMinbuDao.queryMinbuByOrgCode(zNodeTargetPercentInfo.getNodeCode());
                            //发送给创单最小单元主
                            SysEmployeeEhr sysEmployeeEhr = sysEmployeeEhrDao.selectInfo(tOdsMinbu.getLittleXwMasterCode());
                            //发邮件
                            EmailUtil.transmitMsg(sysEmployeeEhr.getNotesmail(),"抢单预警","您好，请尽快进行抢单操作！");
                            System.out.println(sysEmployeeEhr.getEmpSn()+":"+sysEmployeeEhr.getNotesmail());
                        }
                    }
                    //更新状态为1
                    z.setIsSend(1);
                    zWaringPeriodConfigDao.update(z);
                } else if (nowTime >= startTime.getTime() && nowTime < (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000)) && z.getIsSend() == 1) {
                    //更新状态为0
                    z.setIsSend(0);
                    zWaringPeriodConfigDao.update(z);
                } else
                    //5.结束时间对比
                    //两天
                    if (endTime.getTime() - ((long) 2 * 24 * 60 * 60 * 1000) <= nowTime && z.getIsSend() == 0) {
                        //发邮件给平台主
                        for (ZNodeTargetPercentInfo zNodeTargetPercentInfo : zNodeTargetPercentInfoList) {
                            if (!cdList.contains(zNodeTargetPercentInfo.getNodeCode())&& !tyList.contains(zNodeTargetPercentInfo.getNodeCode())) {
                                TOdsMinbu tOdsMinbu = tOdsMinbuDao.queryMinbuByOrgCode(zNodeTargetPercentInfo.getNodeCode());
                                //发送给平台主
                                SysEmployeeEhr sysEmployeeEhr = sysEmployeeEhrDao.selectInfo(tOdsMinbu.getXwMasterCode());
                                //发邮件
                                EmailUtil.transmitMsg(sysEmployeeEhr.getNotesmail(),"抢单预警","您好，请尽快进行抢单操作！");
                                System.out.println(sysEmployeeEhr.getEmpSn()+":"+sysEmployeeEhr.getNotesmail());
                            }
                        }
                    } else if (endTime.getTime() < nowTime) {
                        //更新状态为2
                        z.setIsSend(2);
                        zWaringPeriodConfigDao.update(z);
                    }
            }
        }

    }
}