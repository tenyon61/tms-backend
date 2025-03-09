package com.tms.web.exception;

import com.tms.web.common.BaseResponse;
import com.tms.web.common.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException ex) {
        logger.error("BusinessException", ex);
        return ResultUtils.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException ex) {
        logger.error("RuntimeException", ex);
        return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleException(Exception ex) {
        if (ex instanceof BindException) {
            String errorMsg = ((BindException) ex).getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).sorted().collect(Collectors.joining(","));
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, errorMsg);
        }
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

}
