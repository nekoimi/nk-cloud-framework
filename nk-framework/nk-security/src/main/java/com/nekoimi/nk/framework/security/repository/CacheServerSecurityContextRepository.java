package com.nekoimi.nk.framework.security.repository;

import com.nekoimi.nk.framework.redis.service.RedisService;
import com.nekoimi.nk.framework.security.exception.RequestAuthenticationException;
import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/19 19:22
 * <p>
 * 默认实现是基于session的
 *
 * @see org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
 * 鉴于微服务API全部使用JWT
 * 所以需要自定义实现Redis版本的
 * 以token为key
 * 认证后的AuthenticationToken为value
 * 保存在redis中
 */
@Slf4j
public class CacheServerSecurityContextRepository implements ServerSecurityContextRepository {
    private final static String SUB_KEY = "auth:sub:";
    private final RedisService redisService;

    public CacheServerSecurityContextRepository(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.just(context.getAuthentication())
                .cast(SubjectAuthenticationToken.class)
                .flatMap(subjectToken -> redisService
                        .set(SUB_KEY + subjectToken.getSub(), subjectToken.getDetails(), 10))
                .then(Mono.empty());
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest())
                .map(HttpMessage::getHeaders)
                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(HttpHeaders.AUTHORIZATION)))
                .switchIfEmpty(Mono.error(new RequestAuthenticationException()))
                .flatMap(token -> {
                    // TODO 解析Token，获取sub
                    return Mono.just("sub");
                }).flatMap(sub -> redisService.get(SUB_KEY + sub))
                .switchIfEmpty(Mono.error(new RequestAuthenticationException("Authorization expired")))
                .cast(UserDetails.class)
                .flatMap(userDetails -> {
                    SecurityContextImpl context = new SecurityContextImpl();
                    SubjectAuthenticationToken authentication = new SubjectAuthenticationToken(
                            userDetails.getUsername(), userDetails, userDetails.getAuthorities());
                    context.setAuthentication(authentication);
                    return Mono.just(context);
                });
    }
}
