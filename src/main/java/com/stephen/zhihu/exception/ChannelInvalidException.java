package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import org.springframework.http.HttpStatus;

public class ChannelInvalidException extends BaseRuntimeException {

    String channelName;

    public ChannelInvalidException(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Invalid channel", this.getClass(), "名为 " + channelName + " 的channel是非法的");
        return new BaseResponse(getHttpStatus(), ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
