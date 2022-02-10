package com.nekoimi.nk.auth.provider;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import com.nekoimi.nk.auth.enums.AuthType;
import com.nekoimi.nk.auth.token.UsernamePasswordAuthenticationToken;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.provider.AbstractReactiveAuthenticationSupportProvider;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/26 23:03
 */
@Component
public class UsernamePasswordReactiveAuthenticationSupportProvider extends AbstractReactiveAuthenticationSupportProvider {
    private final String usernameParameter = "username";
    private final String passwordParameter = "password";

    @Override
    protected Integer authType() {
        return AuthType.USERNAME_PASSWORD.code();
    }

    @Override
    protected Class<? extends Authentication> authenticationToken() {
        return UsernamePasswordAuthenticationToken.class;
    }

    @Override
    protected Mono<? extends Authentication> doConvert(Dict requestParameters) {
        return Mono.just(requestParameters)
                .flatMap(dict -> {
                    String username = dict.getStr(usernameParameter);
                    Assert.notBlank(username, "Username can not be empty");
                    String password = dict.getStr(passwordParameter);
                    Assert.notBlank(password, "Password can not be empty");
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
                }).onErrorResume(error -> Mono.error(new RequestValidationException(error.getMessage())));
    }

    @Override
    protected Mono<SubjectAuthenticationToken> doAuthenticate(Authentication authentication) {
        return Mono.just(authentication)
                .cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(token -> Mono.empty());
    }
}
