package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.*;
import com.haier.hailian.contract.dto.SaveXwType3;
import com.haier.hailian.contract.dto.XwType3Info;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.ZHrChainInfoService;
import com.haier.hailian.contract.service.ZWaringPeriodConfigService;
import com.haier.hailian.contract.util.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Resource
    private ZHrChainInfoService zHrChainInfoService;

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

    @Override
    @Transactional
    public void quartzMinbuListByXwType3() {
        ZHrChainInfo chainInfo = new ZHrChainInfo();
        chainInfo.setDeleted(0);
        List<ZHrChainInfo> chainInfoList = zHrChainInfoDao.queryAll(chainInfo);
        for(ZHrChainInfo zHrChainInfo : chainInfoList){
            SaveXwType3 saveXwType3 = new SaveXwType3();
            List<XwType3Info> XwType3List = new ArrayList<>();
            // 去重后有的节点
            Map map = new HashMap();
            map.put("lqCode" , zHrChainInfo.getChainCode());
            List<ZNodeTargetPercentInfo> nodes = zNodeTargetPercentInfoDao.selectListByXwType3Code(map);
//            List<ZNodeTargetPercentInfo> nodes = zNodeTargetPercentInfoDao.selectList(new QueryWrapper<ZNodeTargetPercentInfo>()
//                    .eq("lq_code" , zHrChainInfo.getChainCode())
//                    .isNotNull("share_percent")
//                    .groupBy("xwType3Code"));
            for(ZNodeTargetPercentInfo zNodeTargetPercentInfo : nodes){
                XwType3Info xwType3Info = new XwType3Info();
                xwType3Info.setSharePercent(zNodeTargetPercentInfo.getSharePercent());
                xwType3Info.setXwType3(zNodeTargetPercentInfo.getXwType3());
                xwType3Info.setXwType3Code(zNodeTargetPercentInfo.getXwType3Code());
                XwType3List.add(xwType3Info);
            }
            saveXwType3.setXwType3List(XwType3List);
            saveXwType3.setPtCode(zHrChainInfo.getChainPtCode());
            saveXwType3.setLqName(zHrChainInfo.getChainName());
            saveXwType3.setLqCode(zHrChainInfo.getChainCode());
            saveXwType3.setParentChainCode(zHrChainInfo.getParentCode());

            // 删除再同步
            zHrChainInfoService.syncMinbuListByXwType3(saveXwType3);
            nodes = new ArrayList<>();
            saveXwType3 = null;
            map = new HashMap();
        }

    }

    @Override
    public void checkWarning() {
        //1.查询复核时间是明天截止的合约及链群主的邮箱
        List<ZContracts> list = zContractsDao.selectContractsForCheckWarning();
        //2.循环列表，发邮件给链群主，提醒复核
        for(ZContracts contracts : list){
            String contractName = contracts.getContractName();
            String chainName = contractName.substring(0,contractName.indexOf("-"));
            String masterCode = contracts.getCreateCode();
            SysEmployeeEhr sysEmployeeEhr = sysEmployeeEhrDao.selectInfo(masterCode);
            // 发邮件
            EmailUtil.transmitMsg(sysEmployeeEhr.getNotesmail(),"合约复核预警",chainName+"的链群主您好，系统检测到24小时后是您的链群复核分享比例的截止时间，请您及时登录系统进行复核，逾期未复核的链群系统将判定所有抢入节点抢入成功并且自动均分分享比例。\n" +
                    "            复核分享比例路径：链群合约→所有合约→我发起的→复核分享比例。注：所有抢入最小作战单元之和不得大于100%。");
        }
    }
}