package com.cqbo.web.model.vo.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "已登录用户信息（脱敏）")
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户昵称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "用户简介")
    private String userProfile;

    @Schema(description = "用户角色")
    private String userRole;

    @Schema(description = "会员编号")
    private Long vipNumber;

    @Schema(description = "编辑时间")
    private Date editTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}