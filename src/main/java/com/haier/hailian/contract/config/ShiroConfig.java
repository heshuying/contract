
package com.haier.hailian.contract.config;

import com.haier.hailian.contract.config.shiro.CustomModularRealmAuthenticator;
import com.haier.hailian.contract.config.shiro.HacLoginRealm;
import com.haier.hailian.contract.config.shiro.MySessionManager;
import com.haier.hailian.contract.config.shiro.PhoneRealm;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return authenticator;
    }

    @Bean
    public HacLoginRealm hacLoginRealm(){
        HacLoginRealm realm = new HacLoginRealm();
        // 不需要加密，直接返回
        return realm;
    }

    @Bean
    public PhoneRealm phoneRealm() {
        PhoneRealm userRealm = new PhoneRealm();
        return userRealm;
    }

    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置验证器为自定义验证器
        securityManager.setAuthenticator(modularRealmAuthenticator());
        // 设置Realms
        List<Realm> realms = new ArrayList<>(2);
        realms.add(phoneRealm());
        realms.add(hacLoginRealm());
        securityManager.setRealms(realms);

        securityManager.setSessionManager(sessionManager());
        //使用缓存
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/register", "anon");
        filterMap.put("/hasCellphone", "anon");
        filterMap.put("/phoneLogin", "anon");
        filterMap.put("/resetPwd", "anon");
        filterMap.put("/sms/validSmsCode", "anon");
        filterMap.put("/sms/sendSMS", "anon");
        filterMap.put("/loginMagic", "anon");
        filterMap.put("/ihaierLogin", "anon");
        filterMap.put("/iHaierDingLogin", "anon");
        filterMap.put("/appStatistic", "anon");

        filterMap.put("/zHrChainInfo/searchUsers", "anon");
        filterMap.put("/zHrChainInfo/getDepVCode", "anon");
        filterMap.put("/zHrChainInfo/updateAllGroupId", "anon");//更新groupId不需要校验
        filterMap.put("/zHrChainInfo/updateChainTYInfo", "anon");//插入体验节点不需要校验
        filterMap.put("/zHrChainInfo/updateTargetNodesXwType3Code", "anon");//更新xwTypeCode3不需要校验
        filterMap.put("/talk/getA", "anon");
        filterMap.put("/talk/savePlan", "anon");
        filterMap.put("/test/**", "anon");
        filterMap.put("/contractData", "anon"); // 外部系统获取数据暂时不用校验
        filterMap.put("/chainData", "anon"); // 外部系统获取数据暂时不用校验
        filterMap.put("/updateContractsShareSpace", "anon"); // 外部系统更新数据暂时不用校验
        filterMap.put("/updateChainShareMoney", "anon"); // 外部系统更新数据暂时不用校验
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        //未登录页面
        shiroFilterFactoryBean.setLoginUrl("/unauthorized");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/forbidden");
        filterMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setGlobalSessionTimeout(10800000L);
        mySessionManager.setCacheManager(new MemoryConstrainedCacheManager());
        return mySessionManager;
    }

    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    /**
     * 缓存管理
     *
     * @return
     */
    @Bean
    protected CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


}
