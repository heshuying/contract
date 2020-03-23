package com.haier.hailian.contract.config.shiro;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.hailian.contract.dto.RException;
import com.haier.hailian.contract.dto.ZHrChainInfoDto;
import com.haier.hailian.contract.entity.*;
import com.haier.hailian.contract.service.*;
import com.haier.hailian.contract.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class HacLoginRealm extends AuthorizingRealm {
    public static final String REALM_NAME = "hac_login_realm";

    @Autowired
    private SysEmployeeEhrService sysEmployeeEhrService;
    @Autowired
    private SysEmployeeZService sysEmployeeZService;
    @Autowired
    private SysNetService sysNetService;
    @Autowired
    private TOdsMinbuService minbuService;
    @Autowired
    private SysXwRegionService xwRegionService;

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
        List<TOdsMinbu> minBues=minbuService.queryMinbuByEmp(empSn);
        //格式话xwtype5
        if(minBues!=null&&minBues.size()>0) {
            for (TOdsMinbu bu :
                    minBues) {
                //优先创单
                if(StringUtils.isBlank(bu.getXwType5Code())){
                    bu.setXwType5Code(Constant.EmpRole.CD.getValue());
                }else if(bu.getXwType5Code().contains(Constant.EmpRole.CD.getValue())){
                    bu.setXwType5Code(Constant.EmpRole.CD.getValue());
                }else{
                    bu.setXwType5Code(Constant.EmpRole.TY.getValue());
                }
                if(Constant.EmpRole.TY.getValue().equals(bu.getXwType5Code())){
                    //当前体验链群对应的区域
                    List<SysXwRegion> xwRegion = xwRegionService.list(new QueryWrapper<SysXwRegion>()
                            .eq("xw_code", bu.getXwCode()));
                    if (xwRegion != null && xwRegion.size() > 0) {
                        bu.setRegionCode(xwRegion.get(0).getRegionCode());
                        bu.setRegionName(xwRegion.get(0).getRegionName());
                        bu.setIn42Center(true);
                    }else{
                        bu.setIn42Center(false);
                    }
                }

            }
            TOdsMinbu defaultBu=minBues.get(0);
            sysEmployee.setMinbu(defaultBu);

        }
        boolean isChainMaster=minbuService.isChainMaster(empSn);
        sysEmployee.setChainMaster(isChainMaster);
        sysEmployee.setMinbuList(minBues);

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

