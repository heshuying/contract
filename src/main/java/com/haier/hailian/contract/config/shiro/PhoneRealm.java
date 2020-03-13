package com.haier.hailian.contract.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.ehr.common.utils.MD5Util;
import com.haier.hailian.contract.entity.SysUser;
import com.haier.hailian.contract.service.SysUserService;
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

/**
 * <p>
 * ${desc}
 * </p>
 */
@Slf4j
@Component
public class PhoneRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        PhoneToken token =  (PhoneToken) authenticationToken;
        String phone = (String) token.getPrincipal();
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq("cellphone",phone));
        if (user == null) {
            throw new AuthenticationException("用户名或密码错误");
        }
        if(!user.getPassword().equals(MD5Util.getMD5(token.getPwd()))){
            throw new AuthenticationException("用户名或密码错误");
        }

        return new SimpleAuthenticationInfo(user, phone, this.getName());
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken var1){
        return var1 instanceof PhoneToken;
    }
}

