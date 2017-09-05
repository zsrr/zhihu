package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class ActionResolveException extends BaseRuntimeException {
    public ActionResolveException(String message) {
        super(message);
    }

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Action cannot be resolved", this.getClass(), getMessage());
        return new BaseResponse(HttpStatus.BAD_REQUEST, ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
