package com.cqbo.web.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户更新个人信息请求参数")
public class UserUpdateMyRequest implements Serializable {

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "简介")
    private String userProfile;

    @Serial
    private static final long serialVersionUID = 1L;
}