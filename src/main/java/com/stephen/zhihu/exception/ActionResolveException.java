package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class ActionResolveException extends BaseRuntimeException {

    private static final long serialVersionUID = -3271247385282121150L;
    private String actionName;

    public ActionResolveException(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Unknown action", this.getClass(), "Action named " + actionName + " cannot be resolved");
        return new BaseResponse(getHttpStatus(), ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
