package com.cqbo.web.common;

import com.cqbo.web.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "通用响应类")
public class BaseResponse<T> implements Serializable {

    @Schema(description = "响应代码")
    private int code;

    @Schema(description = "响应内容")
    private T data;

    @Schema(description = "响应消息")
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
