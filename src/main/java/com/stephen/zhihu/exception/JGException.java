package com.stephen.zhihu.exception;


import cn.jiguang.common.resp.APIConnectionException;
import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class JGException extends BaseRuntimeException {

    public JGException(Throwable cause) {
        super(cause);
    }

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Jiguang exception happened", this.getClass(), getCause().getMessage());
        return new BaseResponse(getHttpStatus(), ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        if (getCause() instanceof APIConnectionException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
