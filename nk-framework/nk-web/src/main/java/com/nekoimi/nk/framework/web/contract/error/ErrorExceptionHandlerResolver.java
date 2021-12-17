package com.nekoimi.nk.framework.web.contract.error;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/13 22:08
 *
 * 异常处理器解析器
 */
public interface ErrorExceptionHandlerResolver {
    /**
     * 根据异常获取相应的处理handler
     * @param e
     * @return
     */
    Mono<ErrorExceptionHandler> resolve(Throwable e);
}
