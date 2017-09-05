package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class PasswordIncorrectException extends BaseRuntimeException {
    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("Password is incorrect", PasswordIncorrectException.class, "用户密码错误");
        return new BaseResponse(HttpStatus.UNAUTHORIZED, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
