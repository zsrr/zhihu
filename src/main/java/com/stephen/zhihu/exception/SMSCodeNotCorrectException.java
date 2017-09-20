package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class SMSCodeNotCorrectException extends BaseRuntimeException {
    private static final long serialVersionUID = -597746456091175354L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("SMS code is not correct", this.getClass(), "认证码不正确");
        return new BaseResponse(HttpStatus.BAD_REQUEST, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
