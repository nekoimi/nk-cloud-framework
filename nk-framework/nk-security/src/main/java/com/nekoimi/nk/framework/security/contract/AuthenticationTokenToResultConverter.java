package com.nekoimi.nk.framework.security.contract;

import com.nekoimi.nk.framework.security.result.AuthenticationResult;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 20:15
 *
 * 验证结果转换器
 */
public interface AuthenticationTokenToResultConverter {
    /**
     * 是否支持类型
     * @param authentication
     * @return
     */
    boolean support(Authentication authentication);

    /**
     * 将不同验证方式的结果转换成统一的JWT结果返回
     * @param authentication
     * @return
     */
    Mono<AuthenticationResult> transform(Authentication authentication);
}
