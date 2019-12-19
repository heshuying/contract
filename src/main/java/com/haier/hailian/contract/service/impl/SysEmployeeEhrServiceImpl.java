package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.dao.SysEmployeeEhrDao;
import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.entity.SysRole;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import com.haier.hailian.contract.service.SysEmployeeEhrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 19012964
 * @since 2019-12-16
 */
@Service
public class SysEmployeeEhrServiceImpl extends ServiceImpl<SysEmployeeEhrDao, SysEmployeeEhr> implements SysEmployeeEhrService {
    @Override
    public SysEmployeeEhr getEmployeeEhr(String empSn) {
        SysEmployeeEhr employeeEhr=baseMapper.selectOne(
                new QueryWrapper<SysEmployeeEhr>().eq("empSn",empSn)
        );
        if(employeeEhr!=null){
            List<SysRole> roles=baseMapper.selectRoleByUser(empSn);
            List<SysNodeEhr> nodes=baseMapper.selectNodeByNodeCode(empSn);

            List<SysXiaoweiEhr> xwEhr=baseMapper.selectXiaoweiByEmpId(empSn);
            employeeEhr.setRoles(roles);
            employeeEhr.setNodeEhrList(nodes);
            employeeEhr.setXiaoweiEhrList(xwEhr);
        }
        return  employeeEhr;
    }
}
