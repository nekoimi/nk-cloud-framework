package com.nekoimi.nk.framework.web.handler;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import com.nekoimi.nk.framework.core.utils.ClazzUtils;
import com.nekoimi.nk.framework.core.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * nekoimi  2021/12/19 0:33
 *
 * @see org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
 */
@Slf4j
//@Aspect
//@Configuration
//@ConditionalOnClass(value = org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.class)
public class RewriteResponseBodyResultHandler {

    @SneakyThrows
    @Around(value = "execution(* org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.handleResult(..)) && args(exchange, result)", argNames = "joinPoint,exchange,result")
    public Mono<Void> aroundHandleResult(ProceedingJoinPoint joinPoint, ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        Object sendReturnValue = Mono.empty();
        ResolvableType returnType = result.getReturnType();

        ParameterizedType parameterType = (ParameterizedType) result.getReturnTypeSource().getGenericParameterType();
        log.debug("returnTypeName: {}", parameterType.getTypeName());
        Type[] typeArguments = parameterType.getActualTypeArguments();

        if (returnValue != null) {
            log.debug("ValueRowClass: {}", returnValue.getClass().toString());
        }
        log.debug("TypeRowClass: {}", returnType.getRawClass());

        if (ClazzUtils.instanceOf(returnType.getRawClass(), CorePublisher.class)) {
            if (ClazzUtils.instanceOf(returnType.getRawClass(), Mono.class)) {
                log.debug("Mono.class");
                Mono<Object> objectMono = (Mono<Object>) returnValue;
                sendReturnValue = objectMono.map(JsonResp::ok);
            }
            if (ClazzUtils.instanceOf(returnType.getRawClass(), Flux.class)) {
                log.debug("Flux.class");
                Flux<Object> objectFlux = (Flux<Object>) returnValue;
                sendReturnValue = objectFlux.collectList().map(JsonResp::ok);
            }
        } else {
            log.debug("Object.class");
        }

        HandlerResult handlerResult = new HandlerResult(result.getHandler(), sendReturnValue, result.getReturnTypeSource());
        return (Mono<Void>) joinPoint.proceed(Arrays.asList(exchange, handlerResult).toArray());
//        final Mono responseMono = ((Mono) result.getReturnValue()).map(responseValue -> responseValue instanceof ResponseInfo ? responseValue : ResponseInfo.ok(responseValue));
//        return joinPoint.proceed(Arrays.asList(
//                exchange,
//                new HandlerResult(result.getHandler(), responseMono, result.getReturnTypeSource())
//        ).toArray());
    }
}
