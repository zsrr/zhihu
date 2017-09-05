package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseRuntimeException {
    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("User not found", UserNotFoundException.class, "用户不存在");
        return new BaseResponse(HttpStatus.NOT_FOUND, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
