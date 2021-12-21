package com.nekoimi.nk.framework.security.authentication;

import com.nekoimi.nk.framework.security.contract.AuthenticationSupportManager;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * nekoimi  2021/12/17 15:21
 * <p>
 * 默认验证器适配器
 */
public class AuthenticationManagerAdapter implements AuthenticationSupportManager {
    private final Class<? extends Authentication> authenticationClazz;
    private final ReactiveAuthenticationManager manager;
    private final Function<Authentication, SubjectAuthenticationToken> castSubject;

    public AuthenticationManagerAdapter(Class<? extends Authentication> authenticationClazz,
                                        ReactiveAuthenticationManager manager) {
        this(authenticationClazz, manager, authentication -> (SubjectAuthenticationToken) authentication);
    }

    public AuthenticationManagerAdapter(Class<? extends Authentication> authenticationClazz,
                                        ReactiveAuthenticationManager manager,
                                        Function<Authentication, SubjectAuthenticationToken> castSubject) {
        this.authenticationClazz = authenticationClazz;
        this.manager = manager;
        this.castSubject = castSubject;
    }

    @Override
    public boolean support(Authentication authentication) {
        return authenticationClazz == authentication.getClass();
    }

    @Override
    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return manager.authenticate(authentication).map(castSubject);
    }
}
