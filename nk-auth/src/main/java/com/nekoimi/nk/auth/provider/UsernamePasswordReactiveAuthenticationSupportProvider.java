package com.nekoimi.nk.auth.provider;

import com.nekoimi.nk.auth.token.UsernamePasswordAuthenticationToken;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.provider.AbstractReactiveAuthenticationSupportProvider;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * nekoimi  2021/12/26 23:03
 */
@Component
public class UsernamePasswordReactiveAuthenticationSupportProvider extends AbstractReactiveAuthenticationSupportProvider {
    private final String usernameParameter = "username";
    private final String passwordParameter = "password";

    @Override
    protected Serializable authType() {
        return "1";
    }

    @Override
    protected Class<? extends Authentication> authenticationToken() {
        return UsernamePasswordAuthenticationToken.class;
    }

    @Override
    protected Mono<? extends Authentication> doConvert(Map<String, Object> requestParameters) {
        return Mono.just(requestParameters).flatMap(map -> {
            String username = MapUtils.getString(map, usernameParameter);
            if (StringUtils.isBlank(username)) {
                return Mono.error(new RequestValidationException("Username can not be empty"));
            }
            String password = MapUtils.getString(map, passwordParameter);
            if (StringUtils.isBlank(username)) {
                return Mono.error(new RequestValidationException("Password can not be empty"));
            }
            return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
        });
    }

    @Override
    protected Mono<SubjectAuthenticationToken> doAuthenticate(Authentication authentication) {
        return Mono.just(authentication).cast(UsernamePasswordAuthenticationToken.class)
                .flatMap(token -> Mono.empty());
    }
}
