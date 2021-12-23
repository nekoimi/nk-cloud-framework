package com.nekoimi.nk.framework.security.customizer;

import com.nekoimi.nk.framework.core.constant.SecurityConstants;
import com.nekoimi.nk.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * nekoimi  2021/12/23 22:11
 */
@Slf4j
public class SwaggerSecurityAuthorizeExchangeCustomizer implements SecurityAuthorizeExchangeCustomizer {
    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec exchange) {
        log.debug("swagger security customizer ...");
        SecurityConstants.getSwaggerResourcesAll().forEach(s -> exchange.pathMatchers(s).permitAll());
    }
}
