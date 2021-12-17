package com.nekoimi.nk.framework.security.contract;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 16:07
 */
public interface ReactiveJwtEncoder {

    /**
     * 生成token
     * @param subject
     * @return
     */
    Mono<String> encode(String subject);
}
