package com.nekoimi.nk.framework.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * nekoimi  2021/12/19 19:32
 *
 * 统一不同的登录认证方式的认证结果
 */
public class SubjectAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 认证JWT唯一标识
     */
    private Serializable sub;

    /**
     * 认证实体信息
     */
    private UserDetails details;

    public SubjectAuthenticationToken(Serializable sub, UserDetails details, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.sub = sub;
        this.details = details;
        super.setAuthenticated(true);
    }

    public Serializable getSub() {
        return sub;
    }

    @Override
    public Object getCredentials() {
        return sub;
    }

    @Override
    public Object getPrincipal() {
        return details;
    }
}
