package com.nekoimi.nk.framework.security.annotations;

import com.nekoimi.nk.framework.security.config.SecurityAccessAuthServerConfiguration;
import com.nekoimi.nk.framework.security.config.SecurityAccessConfiguration;
import com.nekoimi.nk.framework.security.config.SecurityComponentConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.lang.annotation.*;

/**
 * nekoimi  2022/1/12 20:32
 * <p>
 * 启用认证服务器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import({
        SecurityComponentConfiguration.class,
        SecurityAccessConfiguration.class,
        SecurityAccessAuthServerConfiguration.class
})
public @interface EnableSecurityAccessAuthServer {
}
