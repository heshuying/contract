package com.haier.hailian.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.dao.SysEmployeeEhrDao;
import com.haier.hailian.contract.entity.SysNodeEhr;
import com.haier.hailian.contract.entity.SysRole;
import com.haier.hailian.contract.entity.SysXiaoweiEhr;
import com.haier.hailian.contract.service.SysEmployeeEhrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haier.hailian.contract.util.Constant;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.List;

import static org.web3j.crypto.Hash.sha256;

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
        //防止推数时一个工号对应多个记录
        List<SysEmployeeEhr> employeeEhres=baseMapper.selectList(
                new QueryWrapper<SysEmployeeEhr>().eq("empSn",empSn)
        );

        if(employeeEhres!=null&&employeeEhres.size()>0){
            SysEmployeeEhr employeeEhr=employeeEhres.get(0);
            List<SysRole> roles=baseMapper.selectRoleByUser(empSn);
            List<SysNodeEhr> nodes=baseMapper.selectNodeByNodeCode(empSn);

            List<SysXiaoweiEhr> xwEhr=baseMapper.selectXiaoweiByEmpId(empSn);
            employeeEhr.setRoles(roles);
            employeeEhr.setNodeEhrList(nodes);
            employeeEhr.setXiaoweiEhrList(xwEhr);
            return employeeEhr;
        }else{
            return  null;
        }

    }


}
