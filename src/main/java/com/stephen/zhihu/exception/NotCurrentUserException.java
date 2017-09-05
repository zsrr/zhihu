package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class NotCurrentUserException extends BaseRuntimeException {
    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Not current user", NotCurrentUserException.class, "非传递的Token对应的用户");
        return new BaseResponse(HttpStatus.UNAUTHORIZED, ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
