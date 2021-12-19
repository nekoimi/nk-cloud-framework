package com.nekoimi.nk.framework.security.contract;

import com.nekoimi.nk.framework.security.token.SubjectAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 15:22
 */
public interface AuthenticationSupportManager {
    /**
     * 支持的请求方式
     * @return
     */
    boolean support(Authentication authentication);

    /**
     * 执行验证
     * @param authentication
     * @return
     */
    Mono<SubjectAuthenticationToken> authenticate(Authentication authentication);
}
