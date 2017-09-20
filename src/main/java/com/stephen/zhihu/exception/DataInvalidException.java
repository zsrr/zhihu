package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class DataInvalidException extends BaseRuntimeException {
    private static final long serialVersionUID = -5031105734288584441L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail errorDetail = new ErrorDetail("Post data is invalid", this.getClass(), "数据不符合规范");
        return new BaseResponse(HttpStatus.BAD_REQUEST, errorDetail);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
