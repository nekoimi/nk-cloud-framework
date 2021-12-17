package com.nekoimi.nk.auth.service;

import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 22:57
 */
public interface UaUserService {
    /**
     *
     * @param id
     * @return
     */
    Mono<Boolean> existsBy(String id);
}
