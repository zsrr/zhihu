package com.stephen.zhihu.exception;


import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseRuntimeException {
    private static final long serialVersionUID = 2177661977044964903L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("Authorization error", UnAuthorizedException.class, "用户身份验证失败");
        return new BaseResponse(HttpStatus.UNAUTHORIZED, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
