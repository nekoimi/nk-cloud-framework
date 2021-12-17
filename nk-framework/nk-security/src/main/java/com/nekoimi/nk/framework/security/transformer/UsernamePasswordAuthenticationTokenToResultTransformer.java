package com.nekoimi.nk.framework.security.transformer;

import com.nekoimi.nk.framework.security.contract.AuthenticationTokenToResultTransformer;
import com.nekoimi.nk.framework.security.result.AuthenticationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 20:23
 */
@Slf4j
@Component
public class UsernamePasswordAuthenticationTokenToResultTransformer
        implements AuthenticationTokenToResultTransformer {
    @Override
    public boolean support(Authentication authentication) {
        log.debug("ResultTransformer support: UsernamePasswordAuthenticationToken.class");
        return UsernamePasswordAuthenticationToken.class == authentication.getClass();
    }

    @Override
    public Mono<AuthenticationResult> transform(Authentication authentication) {
        return Mono.just(authentication)
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(authenticationToken -> {
                    Object principal = authenticationToken.getPrincipal();
                    System.out.println(principal);
                    // TODO
                    String token = "token";
                    return Mono.just(AuthenticationResult.of(token, null));
                });
    }
}
