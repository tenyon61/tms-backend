package com.tms.web.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户注册请求参数")
public class UserRegisterRequest implements Serializable {

    @Schema(description = "账号")
    private String userAccount;

    @Schema(description = "首次输入密码")
    private String userPassword;

    @Schema(description = "二次输入密码")
    private String checkPassword;

    @Serial
    private static final long serialVersionUID = 1L;
}
