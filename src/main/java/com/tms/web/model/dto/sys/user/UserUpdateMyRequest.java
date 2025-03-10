package com.tms.web.model.dto.sys.user;

import com.tms.web.constant.BmsConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户更新个人信息请求参数")
public class UserUpdateMyRequest implements Serializable {

    @Schema(description = "账号")
    private String userAccount;

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "简介")
    private String userProfile;

    @Schema(description = "性别")
    private int sex;

    @Pattern(regexp = BmsConstant.REGEX_EMAIL, message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Pattern(regexp = BmsConstant.REGEX_PHONE, message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;


    @Serial
    private static final long serialVersionUID = 1L;
}