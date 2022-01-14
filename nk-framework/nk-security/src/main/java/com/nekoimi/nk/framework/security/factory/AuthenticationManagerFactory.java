package com.nekoimi.nk.framework.security.factory;

import com.nekoimi.nk.framework.core.exception.http.RequestValidationException;
import com.nekoimi.nk.framework.security.contract.AuthenticationSupportManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * nekoimi  2021/12/16 22:51
 * <p>
 * AuthType多类型综合验证管理器
 * 根据Token类型选择对应的验证器
 */
@Slf4j
@Component
public class AuthenticationManagerFactory implements ReactiveAuthenticationManager, BeanPostProcessor {
    private final static List<AuthenticationSupportManager> supportManagers = new CopyOnWriteArrayList<>();

//    static {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        supportManagers.add(new AuthenticationManagerAdapter(
//                UsernamePasswordAuthenticationToken.class,
//                new UserDetailsRepositoryReactiveAuthenticationManager(new MapReactiveUserDetailsService(user))
//        ));
//    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AuthenticationSupportManager) {
            supportManagers.add((AuthenticationSupportManager) bean);
        }
        return bean;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.debug("IntegratedAuthenticationManager::resolve");
        log.debug("Authentication: {}", authentication);
        return Flux.fromIterable(supportManagers)
                .filter(supportManager -> supportManager.support(authentication))
                .switchIfEmpty(Mono.error(new RequestValidationException("Authentication type is not supported")))
                .last()
                .flatMap(manager -> manager.authenticate(authentication));
    }
}
