package com.tms.web.model.dto.user;

import com.tms.web.constant.BmsConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户注册请求参数")
public class UserRegisterRequest implements Serializable {

    @Size(min = 4, message = "账号不能少于4位")
    @Schema(description = "账号")
    private String userAccount;

    @Pattern(regexp = BmsConstant.REGEX_PASSWORD,message = "密码格式错误(8-18位含数字字母)")
    @Schema(description = "首次输入密码")
    private String userPassword;

    @Pattern(regexp = BmsConstant.REGEX_PASSWORD,message = "密码格式错误(8-18位含数字字母)")
    @Schema(description = "二次输入密码")
    private String checkPassword;

    @Serial
    private static final long serialVersionUID = 1L;
}
