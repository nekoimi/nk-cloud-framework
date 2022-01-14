package com.nekoimi.nk.framework.security.factory;

import com.nekoimi.nk.framework.security.contract.AuthenticationType;
import com.nekoimi.nk.framework.security.contract.AuthenticationTypeRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * nekoimi  2022/1/14 22:28
 */
@Component
public class AuthenticationTypeFactory implements BeanPostProcessor {
    private static final ConcurrentMap<String, AuthenticationType> authenticationTypeMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AuthenticationTypeRegister) {
            AuthenticationType authenticationType = ((AuthenticationTypeRegister) bean).register();
            authenticationTypeMap.putIfAbsent(beanName, authenticationType);
        }
        return bean;
    }

    /**
     * @param code
     * @return
     */
    public static Mono<AuthenticationType> make(Integer code) {
        return Flux.fromIterable(authenticationTypeMap.values())
                .filter(at -> at.matches(code))
                .last()
                .switchIfEmpty(Mono.empty());
    }
}
