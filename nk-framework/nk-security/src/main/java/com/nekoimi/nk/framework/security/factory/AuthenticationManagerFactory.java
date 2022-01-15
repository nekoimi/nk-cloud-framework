package com.nekoimi.nk.framework.security.factory;

import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.constant.SecurityRequestHeaders;
import com.nekoimi.nk.framework.security.contract.ReactiveAuthenticationSupportProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * nekoimi  2021/12/16 22:51
 * <p>
 * 多类型综合验证管理器
 * 根据Token类型选择对应的验证器
 */
@Slf4j
@Component
public class AuthenticationManagerFactory implements ReactiveAuthenticationManager, ServerAuthenticationConverter, BeanPostProcessor {
    private final static List<ReactiveAuthenticationSupportProvider> supportProviders = new CopyOnWriteArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ReactiveAuthenticationSupportProvider) {
            supportProviders.add((ReactiveAuthenticationSupportProvider) bean);
        }
        return bean;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        if (contentType == null || !contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return Mono.error(new RequestValidationException("The content type is not supported"));
        }
        return Mono.just(exchange.getRequest().getHeaders())
                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(SecurityRequestHeaders.AUTH_TYPE)))
                .switchIfEmpty(Mono.error(new RequestValidationException("Headers is missing `%s` parameter", SecurityRequestHeaders.AUTH_TYPE)))
                .flatMap(authType -> Flux.fromIterable(supportProviders)
                        .filter(supportProvider -> supportProvider.support(authType))
                        .switchIfEmpty(Mono.error(new RequestValidationException("Authentication type is not supported")))
                        .last()
                        .flatMap(supportProvider -> supportProvider.convert(exchange))
                        .switchIfEmpty(Mono.error(new RequestValidationException("Authentication request is not supported"))));
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Flux.fromIterable(supportProviders)
                .filter(supportProvider -> supportProvider.support(authentication))
                .switchIfEmpty(Mono.error(new RequestValidationException("Authentication type is not supported")))
                .last()
                .flatMap(supportProvider -> supportProvider.authenticate(authentication));
    }
}
