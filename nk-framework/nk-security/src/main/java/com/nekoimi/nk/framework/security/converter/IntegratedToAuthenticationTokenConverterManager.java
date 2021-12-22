package com.nekoimi.nk.framework.security.converter;

import com.nekoimi.nk.framework.core.constant.CustomHeaderConstants;
import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.constant.AuthType;
import com.nekoimi.nk.framework.security.contract.RequestToAuthenticationTokenConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * nekoimi  2021/12/16 22:21
 * <p>
 * 将不同类型的登录方式的请求参数封装成对应的AuthenticationToken
 * request -> AuthenticationToken
 */
@Slf4j
@Component
public class IntegratedToAuthenticationTokenConverterManager implements ServerAuthenticationConverter, BeanPostProcessor {
    private final static List<RequestToAuthenticationTokenConverter> converters = new CopyOnWriteArrayList<>();

    static {
        converters.add(new AuthenticationConverterAdapter(
                AuthType.USERNAME_PASSWORD, new ServerFormLoginAuthenticationConverter()));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestToAuthenticationTokenConverter) {
            converters.add((RequestToAuthenticationTokenConverter) bean);
        }
        return bean;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        log.debug("------------- request convert to token -------------");
        return Mono.just(exchange.getRequest().getHeaders())
                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(CustomHeaderConstants.AUTH_TYPE_REQUEST_HEADER)))
                .switchIfEmpty(Mono.error(new RequestValidationException("Headers is missing `%s` parameter", CustomHeaderConstants.AUTH_TYPE_REQUEST_HEADER)))
                .map(Integer::parseInt)
                .flatMap(AuthType::valueOf)
                .switchIfEmpty(Mono.error(new RequestValidationException("Illegal authentication type")))
                .flatMap(authType -> Flux.fromIterable(converters)
                        .filter(c -> c.support(authType))
                        .switchIfEmpty(Mono.error(new RequestValidationException("Authentication type is not supported")))
                        .last()
                        .flatMap(c -> c.convert(exchange)));
    }
}
