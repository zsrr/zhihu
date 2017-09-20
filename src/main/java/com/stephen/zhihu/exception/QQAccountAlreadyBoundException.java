package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class QQAccountAlreadyBoundException extends BaseRuntimeException {
    private static final long serialVersionUID = 5816354354659147294L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("QQ account has been bound", QQAccountAlreadyBoundException.class, "QQ账户已被绑定");
        return new BaseResponse(HttpStatus.CONFLICT, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
