package com.haier.hailian.contract.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * Created by 19012964 on 2020/3/16.
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
    /**
     * 自定义Realm的分配策略
     * 通过realm.supports()方法匹配对应的Realm，因此才要在Realm中重写supports()方法
     * @param realms
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();

        // 通过supports()方法，匹配对应的Realm
        Realm uniqueRealm = null;
        for (Realm realm : realms) {
            if (realm.supports(token)) {
                uniqueRealm = realm;
                break;
            }
        }
        if (uniqueRealm == null) {
            throw new UnsupportedTokenException();
        }
        return uniqueRealm.getAuthenticationInfo(token);
    }
}
