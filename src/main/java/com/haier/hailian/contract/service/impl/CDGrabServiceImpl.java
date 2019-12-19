package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dao.ZSharePercentDao;
import com.haier.hailian.contract.dto.CurrentUser;
import com.haier.hailian.contract.dto.grab.CDGrabInfoRequestDto;
import com.haier.hailian.contract.dto.grab.CDGrabInfoResponseDto;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.ZSharePercent;
import com.haier.hailian.contract.service.CDGrabService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 19033323
 */
@Service
public class CDGrabServiceImpl implements CDGrabService {
    @Autowired
    ZSharePercentDao sharePercentDao;

    @Override
    public CDGrabInfoResponseDto queryCDGrabInfo(CDGrabInfoRequestDto requestDto){
        CDGrabInfoResponseDto responseDto = new CDGrabInfoResponseDto();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();
        //获取用户首页选中的用户
        CurrentUser currentUser = sysUser.getCurrentUser();
        String xwCode = currentUser.getXwCode();

        List<ZSharePercent> resultList = sharePercentDao.selectList(new QueryWrapper<ZSharePercent>().eq("xw_code", xwCode).eq("period_code", requestDto.getYearMonth()));
        if(resultList!=null && !resultList.isEmpty()){
            responseDto.setSharePercent(resultList.get(0).getPercent());
        }
        return responseDto;
    }
}
