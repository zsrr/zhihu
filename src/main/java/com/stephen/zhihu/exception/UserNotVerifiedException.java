package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class UserNotVerifiedException extends BaseRuntimeException {
    private static final long serialVersionUID = -1786691005798866662L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("User not verified", this.getClass(), "用户未通过验证");
        return new BaseResponse(HttpStatus.NOT_FOUND, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
