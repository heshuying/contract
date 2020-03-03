package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dto.StarDTO;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.VJdxp;
import com.haier.hailian.contract.dao.VJdxpDao;
import com.haier.hailian.contract.service.VJdxpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        paraMap.put("empCode", sysUser.getEmpSn());

        return vJdxpDao.getStarList(paraMap);
    }
}