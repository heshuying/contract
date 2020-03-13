package com.haier.hailian.contract.config.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import java.io.Serializable;

/**
 * <p>
 *   手机登陆Token
 * </p>
 */
public class PhoneToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {

    /**
     * 手机号码
     */
    private String phone;
    private String pwd;
    private boolean rememberMe;
    private String host;

    /**
     * 重写getPrincipal方法
     */
    @Override
    public Object getPrincipal() {
        return phone;
    }

    /**
     * 重写getCredentials方法
     */
    @Override
    public Object getCredentials() {
        return phone;
    }

    public PhoneToken() { this.rememberMe = false; }

    public PhoneToken(String phone, String pwd) { this(phone,pwd, false, null); }

    public PhoneToken(String phone,String pwd, boolean rememberMe) { this(phone,pwd, rememberMe, null); }

    public PhoneToken(String phone,String pwd, boolean rememberMe, String host) {
        this.phone = phone;
        this.pwd=pwd;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
