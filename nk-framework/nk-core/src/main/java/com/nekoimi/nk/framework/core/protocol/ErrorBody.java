package com.nekoimi.nk.framework.core.protocol;

import com.nekoimi.nk.framework.core.error.ErrorContract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2021/12/13 22:35
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ErrorBody implements ErrorContract {
    private Integer code;
    private String message;
    private String trace;
    private String exception;

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String trace() {
        return trace;
    }

    @Override
    public String exception() {
        return exception;
    }
}
