package com.nekoimi.nk.auth.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * nekoimi  2021/12/26 22:31
 *
 * 短信验证码认证Token
 */
public class MobileAuthenticationToken implements Authentication {
    private final String mobile;
    private final String smsCode;
    private final Collection<GrantedAuthority> authorities;
    private boolean authenticated = false;

    public MobileAuthenticationToken(String mobile, String smsCode) {
        this.mobile = mobile;
        this.smsCode = smsCode;
        this.authorities = new ArrayList<>();
        setAuthenticated(false);
    }

    public MobileAuthenticationToken(String mobile, String smsCode, Collection<GrantedAuthority> authorities) {
        this.mobile = mobile;
        this.smsCode = smsCode;
        this.authorities = authorities;
        setAuthenticated(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return smsCode;
    }

    @Override
    public Object getDetails() {
        return mobile;
    }

    @Override
    public Object getPrincipal() {
        return mobile;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return mobile;
    }
}
