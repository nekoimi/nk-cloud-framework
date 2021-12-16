package com.nekoimi.nk.framework.security.converter.result;

import com.nekoimi.nk.framework.security.contract.AuthenticationTokenToResultConverter;
import com.nekoimi.nk.framework.security.result.AuthenticationResult;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 20:23
 */
@Component
public class UsernamePasswordResultConverter implements AuthenticationTokenToResultConverter {
    @Override
    public boolean support(Authentication authentication) {
        return UsernamePasswordAuthenticationToken.class == authentication.getClass();
    }

    @Override
    public Mono<AuthenticationResult> transform(Authentication authentication) {
        return Mono.just(authentication)
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(authenticationToken -> {
                    Object principal = authenticationToken.getPrincipal();
                    // TODO
                    String token = "";
                    return Mono.just(AuthenticationResult.of(token, null));
                });
    }
}
