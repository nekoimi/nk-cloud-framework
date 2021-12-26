package com.nekoimi.nk.auth.authentication;

import com.nekoimi.nk.auth.token.UsernamePasswordAuthenticationToken;
import com.nekoimi.nk.framework.security.contract.AuthenticationSupportManager;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/26 23:03
 */
@Component
public class UsernamePasswordReactiveAuthenticationManager implements AuthenticationSupportManager {
    @Override
    public boolean support(Authentication authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication.getClass());
    }

    @Override
    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return Mono.just(authentication).cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(token -> {

                    return Mono.empty();
                });
    }
}
