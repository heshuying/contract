package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.dao.SysXiaoweiEhrDao;
import com.haier.hailian.contract.dao.TargetBasicDao;
import com.haier.hailian.contract.dao.ZHrChainInfoDao;
import com.haier.hailian.contract.dto.QueryBottomDTO;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.TargetBasicInfo;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import com.haier.hailian.contract.entity.TargetBasic;
import com.haier.hailian.contract.entity.XiaoweiEhr;
import com.haier.hailian.contract.entity.ZHrChainInfo;
import com.haier.hailian.contract.service.TargetBasicService;
import com.haier.hailian.contract.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 01431594
 * @since 2019-12-18
 */
@Service
public class TargetBasicServiceImpl extends ServiceImpl<TargetBasicDao, TargetBasic> implements TargetBasicService {

    @Autowired
    private TargetBasicDao targetBasicDao;
    @Autowired
    private ZHrChainInfoDao zHrChainInfoDao;
    @Autowired
    private SysXiaoweiEhrDao sysXiaoweiEhrDao;

    @Override
    public List<TargetBasic> selectBottom(QueryBottomDTO dto){
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setChainCode(dto.getChainCode());
        targetBasic.setTargetDiffType("000");
        String date = dto.getStartDate();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMM");
        try {
            targetBasic.setPeriodCode(sf2.format(sf.parse(date)));
        } catch (ParseException e) {
            throw new RException("日期类型不正确", Constant.CODE_VALIDFAIL);
        }
        List<TargetBasic> list = targetBasicDao.selectTarget(targetBasic);
        for(TargetBasic targetBasic1:list){
            if(null != targetBasic1.getTargetBottomLine() && !"".equals(targetBasic1.getTargetBottomLine())){
                String bottom = targetBasic1.getTargetBottomLine();
                int position = bottom.length() - bottom.indexOf(".") - 1;
                if(position==1) bottom = bottom.replaceAll("\\.0","");
                targetBasic1.setTargetBottomLine(bottom);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
            if(null != targetBasic1.getTargetJdLine() && !"".equals(targetBasic1.getTargetJdLine())){
                String e2e = targetBasic1.getTargetJdLine();
                int position = e2e.length() - e2e.indexOf(".") - 1;
                if(position==1) e2e = e2e.replaceAll("\\.0","");
                targetBasic1.setTargetJdLine(e2e);
            }else{
                throw new RException("链群的底线和E2E目标未维护，无法举单",Constant.CODE_VALIDFAIL);
            }
        }
        return list;
    }

    @Override
    public List<TargetBasic> selectContractsFirstTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("003");
        targetBasic.setChainCode(dto.getChainCode());
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    public List<TargetBasic> selectContractsSecondTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("004");
        targetBasic.setChainCode(dto.getChainCode());
        return targetBasicDao.selectTarget(targetBasic);
    }

    @Override
    public int updateContractsTarget(List<TargetBasic> targetBasicList) {
        int num = 0;
        for(TargetBasic targetBasic : targetBasicList){
            targetBasicDao.updateById(targetBasic);
            num++;
        }
        return num;
    }

    @Override
    public int insertContractsTarget(List<TargetBasic> targetBasicList) {
        int num = 0;
        for(TargetBasic targetBasic : targetBasicList){
            targetBasic.setChainCode("LQ" + getNum());
            targetBasicDao.insert(targetBasic);
            num++;
        }
        return num;
    }

    @Override
    public List<TargetBasicInfo> selectContractsTarget(QueryBottomDTO dto) {
        TargetBasic targetBasic = new TargetBasic();
        targetBasic.setTargetDiffType("003");
        targetBasic.setChainCode(dto.getChainCode());
        List<TargetBasic> targetList = targetBasicDao.selectTarget(targetBasic);
        List<TargetBasicInfo> infos = new ArrayList<>();
        for(TargetBasic basic : targetList){
            targetBasic.setTargetDiffType("004");
            targetBasic.setJudanFlag(String.valueOf(basic.getId()));
            List<TargetBasic> childTargetList = targetBasicDao.selectTarget(targetBasic);

            TargetBasicInfo info = new TargetBasicInfo();
            BeanUtils.copyProperties(basic , info);
            info.setChildTargetBasicList(childTargetList);
            infos.add(info);

        }
        return infos;
    }

    @Override
    public int deleteContractsTarget(Integer id) {
        TargetBasic targetBasic = targetBasicDao.selectById(id);
        if("0".equals(targetBasic.getJudanFlag())){ // 是一级单
            targetBasicDao.delete(new QueryWrapper<TargetBasic>()
                    .eq("judan_flag" , String.valueOf(id)));

        }
        int num = targetBasicDao.deleteById(id);
        return num;
    }

    @Override
    public List<SysXiaoweiEhr> selectXwAll(XiaoweiEhr xiaoweiEhr) {
        List<SysXiaoweiEhr> list = sysXiaoweiEhrDao.queryAll(xiaoweiEhr);
        return list;
    }

    @Override
    public int getNum() {
        // 获取计数器
        int num = zHrChainInfoDao.getNum();
        // 更新计数器表
        zHrChainInfoDao.updateNum();
        return num;
    }


}
