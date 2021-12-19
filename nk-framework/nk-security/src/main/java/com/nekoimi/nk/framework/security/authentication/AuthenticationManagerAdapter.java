package com.nekoimi.nk.framework.security.authentication;

import com.nekoimi.nk.framework.security.contract.AuthenticationSupportManager;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 15:21
 *
 * 默认验证器适配器
 */
public class AuthenticationManagerAdapter implements AuthenticationSupportManager {
    private final Class<? extends Authentication> authenticationClazz;
    private final ReactiveAuthenticationManager manager;

    public AuthenticationManagerAdapter(Class<? extends Authentication> authentication,
                                        ReactiveAuthenticationManager manager) {
        this.authenticationClazz = authentication;
        this.manager = manager;
    }

    @Override
    public boolean support(Authentication authentication) {
        return authenticationClazz == authentication.getClass();
    }

    @Override
    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return manager.authenticate(authentication).cast(SubjectAuthenticationToken.class);
    }
}
