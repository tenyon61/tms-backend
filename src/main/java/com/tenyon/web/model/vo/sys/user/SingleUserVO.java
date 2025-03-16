package com.tenyon.web.model.vo.sys.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息（脱敏）
 *
 * @author tenyon
 * @date 2025/3/13
 */
@Data
@Schema(description = "单用户信息（脱敏）")
public class SingleUserVO {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "权限集合")
    private Object[] permissions;
}
