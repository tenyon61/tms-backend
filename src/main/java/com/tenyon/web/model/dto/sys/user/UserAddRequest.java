package com.tenyon.web.model.dto.sys.user;

import com.tenyon.web.constant.BmsConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "新增用户请求参数")
public class UserAddRequest implements Serializable {

    @Min(value = 4, message = "账号不能少于4位")
    @Schema(description = "账号")
    private String userAccount;

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "性别")
    private Integer sex;

    @Pattern(regexp = BmsConstant.REGEX_EMAIL, message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Pattern(regexp = BmsConstant.REGEX_PHONE, message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "角色")
    private String roleIds;

    @Serial
    private static final long serialVersionUID = 1L;
}