package com.stephen.zhihu.exception;


import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends BaseRuntimeException {
    private static final long serialVersionUID = -5848500675896392898L;

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Permission denied", this.getClass(), "用户无权限访问资源");
        return new BaseResponse(getHttpStatus(), ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
