package com.stephen.zhihu.exception;

import com.stephen.zhihu.dto.BaseResponse;
import com.stephen.zhihu.dto.ErrorDetail;
import com.stephen.zhihu.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<BaseResponse> baseRuntimeException(BaseRuntimeException e) {
        return new ResponseEntity<BaseResponse>(e.getBaseResponse(), e.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> constraintViolation(ConstraintViolationException exception) {
        ErrorDetail ed = new ErrorDetail("Constraint not met", ConstraintViolationException.class, exception.getMessage());
        BaseResponse br = new BaseResponse(HttpStatus.NOT_ACCEPTABLE, ed);
        return new ResponseEntity<BaseResponse>(br, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> allException(Exception e) {
        ErrorDetail ed = new ErrorDetail("Exception happened", e.getClass(), e.getMessage());
        int statusCode = ExceptionUtils.getStatusCode(e.getClass());
        HttpStatus hs = HttpStatus.valueOf(statusCode);
        BaseResponse br = new BaseResponse(hs, ed);
        return new ResponseEntity<BaseResponse>(br, hs);
    }
}
