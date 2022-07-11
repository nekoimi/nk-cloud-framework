package com.nekoimi.nk.gateway.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * nekoimi  2021/12/21 16:45
 * <p>
 * 自定义路由操作，添加数据库持久化
 */
@Slf4j
//@Repository
public class DatabaseRouteDefinitionRepository implements RouteDefinitionRepository {

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.debug("------------- getRouteDefinitions ------------- ");
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("nk-uc-service");
        routeDefinition.setUri(URI.create("lb://nk-uc-service"));
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        predicateDefinition.setArgs(Map.of("Path", "/uc/**"));
        routeDefinition.setPredicates(Collections.singletonList(predicateDefinition));
        FilterDefinition filterDefinition = new FilterDefinition();
        filterDefinition.setName("RewritePath");
        Map<String, String> args = new HashMap<>();
        args.put("regexp", "/uc/(?<target>.*)");
        args.put("replacement", "/$\\{target}");
        filterDefinition.setArgs(args);
        routeDefinition.setFilters(Collections.singletonList(filterDefinition));
        return Flux.just(routeDefinition);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        log.debug("------------- save RouteDefinition ------------- ");
        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        log.debug("------------- delete RouteDefinition ------------- ");
        return Mono.empty();
    }
}
