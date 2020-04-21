package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.VJdxp;
import com.haier.hailian.contract.dao.VJdxpDao;
import com.haier.hailian.contract.service.VJdxpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.util.DateFormatUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2020-03-02
 */
@Service
public class VJdxpServiceImpl extends ServiceImpl<VJdxpDao, VJdxp> implements VJdxpService {
    @Autowired
    VJdxpDao vJdxpDao;

    @Override
    public List<StarDTO> getStarList(Map<String, Object> paraMap){
        if(paraMap == null) paraMap = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        if(paraMap.get("empCode") == null){
            paraMap.put("empCode", sysUser.getEmpSn());
        }

        List<StarDTO> list = vJdxpDao.getStarList(paraMap);
        if(list == null || list.isEmpty()){
            return new ArrayList<>();
        }

        int day = DateFormatUtil.getDAYOfDate(new Date());
        String yearmonth = DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YM);
        for(StarDTO item : list){
            String periodCode = "";
            // 合约日期是当前月
            if(yearmonth.equals(item.getMonthStr())){
                if(day == 1){
                    // 1号取当天
                    periodCode = DateFormatUtil.format(new Date(), DateFormatUtil.DATE_PATTERN_YMD);
                }else{
                    // 取前一天
                    periodCode = DateFormatUtil.format(DateFormatUtil.addDateDays(new Date(), -1), DateFormatUtil.DATE_PATTERN_YMD);
                }
            }else {
                // 历史取月末最后一天
                periodCode = item.getMonthEnd();
            }
            List<VJdxp> starList = vJdxpDao.selectList(new QueryWrapper<VJdxp>().eq("LQ_CODE", item.getChainCode()).eq("CONTRACT_ID",item.getParentId())
                    .eq("JD_CODE", item.getOrgCode()).eq("PERIOD_CODE", periodCode));
            if(starList != null && !starList.isEmpty()){
                item.setPjM(starList.get(0).getPjM());
                item.setPjY(starList.get(0).getPjY());
            }

        }

        return list;
    }
}
