package com.nekoimi.nk.framework.security.annotations;

import com.nekoimi.nk.framework.security.config.SecurityAccessConfiguration;
import com.nekoimi.nk.framework.security.config.SecurityComponentConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.lang.annotation.*;

/**
 * nekoimi  2021/12/24 15:06
 *
 * 启用安全访问控制
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import(value = {
        SecurityComponentConfiguration.class,
        SecurityAccessConfiguration.class
})
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public @interface EnableSecurityAccess {
}
