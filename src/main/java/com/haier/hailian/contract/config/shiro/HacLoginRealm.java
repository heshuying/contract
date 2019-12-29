package com.haier.hailian.contract.config.shiro;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.entity.SysEmployeeEhr;
import com.haier.hailian.contract.entity.SysEmployeeZ;
import com.haier.hailian.contract.entity.SysNet;
import com.haier.hailian.contract.entity.TOdsMinbu;
import com.haier.hailian.contract.service.SysEmployeeEhrService;
import com.haier.hailian.contract.service.SysEmployeeZService;
import com.haier.hailian.contract.service.SysNetService;
import com.haier.hailian.contract.service.TOdsMinbuService;
import com.haier.hailian.contract.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HacLoginRealm extends AuthorizingRealm {
    @Autowired
    private SysEmployeeEhrService sysEmployeeEhrService;
    @Autowired
    private SysEmployeeZService sysEmployeeZService;
    @Autowired
    private SysNetService sysNetService;
    @Autowired
    private TOdsMinbuService minbuService;

    /**
     * 认证(登录时调用)
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        HacLoginToken token =  (HacLoginToken) authenticationToken;
        String empSn = (String) token.getPrincipal();
        SysEmployeeEhr sysEmployee = sysEmployeeEhrService.getEmployeeEhr(empSn);
        if (sysEmployee == null) {
            //查网格
            SysEmployeeZ sysEmployeeZ = sysEmployeeZService.getOne(
                    new QueryWrapper<SysEmployeeZ>().eq("empSn",empSn)
            );
            if(sysEmployeeZ == null){
                throw new RException("用户"+ Constant.MSG_DATA_NOTFOUND,Constant.CODE_DATA_NOTFOUND);
            }else{
                sysEmployee = new SysEmployeeEhr();
                sysEmployee.setEmpSn(sysEmployeeZ.getEmpSN());
                sysEmployee.setEmpName(sysEmployeeZ.getEmpName());
            }
        }
        List<SysNet> sysNetList = sysNetService.list(
                new QueryWrapper<SysNet>().eq("empSN",empSn));
        List<TOdsMinbu> minBu=minbuService.queryMinbuByEmp(empSn);

        if(minBu!=null||minBu.size()>0){
            sysEmployee.setMinbu(minBu.get(0));
        }
        sysEmployee.setWanggeList(sysNetList);
        return new SimpleAuthenticationInfo(sysEmployee, empSn, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof HacLoginToken;
    }
}

