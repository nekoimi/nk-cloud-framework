package com.nekoimi.nk.framework.web.handler;

import com.nekoimi.nk.framework.core.protocol.JsonResp;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.service.ResponseMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * nekoimi  2021/12/19 0:33
 *
 * @see org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
 */
@Slf4j
public class RewriteResponseBodyResultHandler extends ResponseBodyResultHandler {
    private static final String REWRITE_PARAMETER_METHOD_NAME = "methodParameterForRewrite";
    private static MethodParameter methodParameter;
    static {
        try {
            methodParameter = new MethodParameter(RewriteResponseBodyResultHandler.class.getDeclaredMethod(REWRITE_PARAMETER_METHOD_NAME), -1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Setter
    @Getter
    private Set<String> excludes = new HashSet<>();

    public RewriteResponseBodyResultHandler(List<HttpMessageWriter<?>> writers,
                                            RequestedContentTypeResolver resolver,
                                            ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
    }

    private static Mono<JsonResp<?>> methodParameterForRewrite() {
        return Mono.empty();
    }

    @Override
    public boolean supports(HandlerResult result) {
        if (!CollectionUtils.isEmpty(excludes) && result.getHandler() instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) result.getHandler();
            String typeName = method.getMethod().getDeclaringClass().getName() + "." + method.getMethod().getName();
            for (String exclude : excludes) {
                if (typeName.startsWith(exclude)) {
                    return false;
                }
            }
        }

        Class<?> gen = result.getReturnType().resolveGeneric(0);
        boolean isAlreadyResponse = (gen == ResponseMessage.class || gen == ResponseEntity.class);
        boolean isStream = result.getReturnType().resolve() == Mono.class
                || result.getReturnType().resolve() == Flux.class;

        RequestMapping mapping = result.getReturnTypeSource().getMethodAnnotation(RequestMapping.class);
        if (mapping == null) {
            return false;
        }

        for (String produce : mapping.produces()) {
            MimeType mimeType = MimeType.valueOf(produce);
            if (MediaType.TEXT_EVENT_STREAM.includes(mimeType) || MediaType.APPLICATION_STREAM_JSON.includes(mimeType)) {
                return false;
            }
        }

        return isStream && !isAlreadyResponse && super.supports(result);
    }

    @Override
    @SuppressWarnings("all")
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object body = result.getReturnValue();

        if (exchange.getRequest().getHeaders().getAccept().contains(MediaType.TEXT_EVENT_STREAM)) {
            return writeBody(body, methodParameter, exchange);
        }

        if (body instanceof Mono) {
            body = ((Mono) body).map(JsonResp::ok).switchIfEmpty(Mono.just(JsonResp.ok()));
        }

        if (body instanceof Flux) {
            body = ((Flux) body).collectList().map(JsonResp::ok).switchIfEmpty(Mono.just(JsonResp.ok()));
        }

        if (body == null) {
            body = Mono.just(JsonResp.ok());
        }

        return writeBody(body, methodParameter, exchange);
    }
}
