package com.nekoimi.nk.framework.base.error;

import com.nekoimi.nk.framework.base.contract.error.ErrorExceptionHandler;
import com.nekoimi.nk.framework.base.contract.error.ErrorExceptionHandlerResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * nekoimi  2021/12/13 22:43
 */
@Slf4j
@Component
public class DefaultErrorExceptionHandlerResolver implements ErrorExceptionHandlerResolver {
    private final Map<Class<?>, ErrorExceptionHandler> handlerMap;
    private final ErrorExceptionHandler<Error> rootErrorHandler;
    private final ErrorExceptionHandler<Exception> rootExceptionHandler;

    public DefaultErrorExceptionHandlerResolver(
            List<ErrorExceptionHandler> handlers,
            ErrorExceptionHandler<Error> rootErrorHandler,
            ErrorExceptionHandler<Exception> rootExceptionHandler) {
        this.rootErrorHandler = rootErrorHandler;
        this.rootExceptionHandler = rootExceptionHandler;
        this.handlerMap = handlers.stream().filter(this::ignoreHandler).collect(Collectors.toMap(ErrorExceptionHandler::getType, handler -> handler));
    }

    private boolean ignoreHandler(ErrorExceptionHandler handler) {
        return !(handler.getType() == Error.class || handler.getType() == Exception.class);
    }

    @Override
    public Mono<ErrorExceptionHandler> resolve(Throwable e) {
        ErrorExceptionHandler<? extends Throwable> handler = handlerMap.get(e.getClass());
        if (handler == null) {
            for (Map.Entry<Class<?>, ErrorExceptionHandler> handlerEntry : handlerMap.entrySet()) {
                Class<?> entryKey = handlerEntry.getKey();
                if (entryKey.isInstance(e)) {
                    handler = handlerEntry.getValue();
                }
            }
        }
        if (handler == null) {
            if (e.getClass() == Error.class) {
                handler = rootErrorHandler;
            } else {
                handler = rootExceptionHandler;
            }
        }
        return Mono.just(handler);
    }
}
