package com.cqbo.web.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "新增用户请求参数")
public class UserAddRequest implements Serializable {

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "账号")
    private String userAccount;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "用户角色")
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}