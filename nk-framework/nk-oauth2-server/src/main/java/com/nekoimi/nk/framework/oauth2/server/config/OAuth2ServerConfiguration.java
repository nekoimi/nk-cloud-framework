package com.nekoimi.nk.framework.oauth2.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * nekoimi  2021/12/20 9:54
 */
@Slf4j
//@Configuration
//@Import(OAuth2AuthorizationServerConfiguration.class)
public class OAuth2ServerConfiguration {

    //  定义 spring security 拦击链规则
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .formLogin();
        return http.build();
    }

    // 创建默认的bean 登录客户端,基于 授权码、 刷新令牌的能力
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId("pig")
                .clientId("pig")
                .clientSecret("pig")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantTypes(authorizationGrantTypes -> {
                    authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .redirectUri("https://pig4cloud.com")
                .build();
        return new InMemoryRegisteredClientRepository(client);
    }
}
