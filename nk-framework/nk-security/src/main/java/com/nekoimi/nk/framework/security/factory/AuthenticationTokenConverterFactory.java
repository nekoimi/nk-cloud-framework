package com.nekoimi.nk.framework.security.factory;

import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.constant.SecurityRequestHeaders;
import com.nekoimi.nk.framework.security.contract.RequestToAuthenticationTokenConverter;
import com.nekoimi.nk.framework.security.factory.AuthenticationTypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
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
public class AuthenticationTokenConverterFactory implements ServerAuthenticationConverter, BeanPostProcessor {
    private final static List<RequestToAuthenticationTokenConverter> converters = new CopyOnWriteArrayList<>();

//    static {
//        converters.add(new AuthenticationConverterAdapter(
//                AuthType.USERNAME_PASSWORD, new ServerFormLoginAuthenticationConverter()));
//    }

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
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();
        if (contentType == null || !contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            return Mono.error(new RequestValidationException("The content type is not supported"));
        }
        return Mono.just(exchange.getRequest().getHeaders())
                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(SecurityRequestHeaders.AUTH_TYPE)))
                .switchIfEmpty(Mono.error(new RequestValidationException("Headers is missing `%s` parameter", SecurityRequestHeaders.AUTH_TYPE)))
                .map(Integer::parseInt)
                .flatMap(AuthenticationTypeFactory::make)
                .switchIfEmpty(Mono.error(new RequestValidationException("Illegal authentication type")))
                .flatMap(authType -> Flux.fromIterable(converters)
                        .filter(c -> c.support(authType))
                        .switchIfEmpty(Mono.error(new RequestValidationException("Authentication type is not supported")))
                        .last()
                        .flatMap(c -> c.convert(exchange))
                        .switchIfEmpty(Mono.error(new RequestValidationException("Authentication request is not supported"))));
    }
}
