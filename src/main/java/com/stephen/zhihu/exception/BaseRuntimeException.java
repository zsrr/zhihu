package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import org.springframework.http.HttpStatus;

public abstract class BaseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 8760289831588818899L;

    public BaseRuntimeException() {
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public abstract BaseResponse getBaseResponse();

    public abstract HttpStatus getHttpStatus();
}
