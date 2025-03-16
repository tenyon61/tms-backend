package com.tenyon.web.model.dto.sys.user;

import com.tenyon.web.constant.BmsConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户登录请求参数")
public class UserLoginRequest implements Serializable {

    @Size(min = 4, message = "账号不能少于4位")
    @Schema(description = "账号")
    private String userAccount;

    @Pattern(regexp = BmsConstant.REGEX_PASSWORD, message = "密码格式错误(8-18位含数字字母)")
    @Schema(description = "密码")
    private String userPassword;

    @Schema(description = "角色")
    private String roleIds;

    @Serial
    private static final long serialVersionUID = 1L;

}
