package com.haier.hailian.contract.service.impl;

import com.haier.hailian.contract.dao.TOdsMinbuDao;
import com.haier.hailian.contract.dto.LittleXWEmpDTO;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.service.EmployeeCardService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    @Autowired
    TOdsMinbuDao minbuDao;

    @Override
    public List<LittleXWEmpDTO> getLittleXWEmp(Map<String,Object> paraMap){
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        SysEmployeeEhr sysUser = (SysEmployeeEhr) subject.getPrincipal();

        paraMap.put("xwMasterCode", sysUser.getEmpSn());
        List<LittleXWEmpDTO> result = minbuDao.getLittleXWEmp(paraMap);
        return result;
    }
}
