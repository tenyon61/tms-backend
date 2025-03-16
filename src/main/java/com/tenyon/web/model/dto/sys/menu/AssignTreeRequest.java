package com.tenyon.web.model.dto.sys.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分配资源树请求参数
 *
 * @author tenyon
 * @date 2025/3/12
 */
@Data
@Schema(description = "分配资源树请求参数")
public class AssignTreeRequest {

    @NotNull(message = "用户id不为空")
    @Schema(description = "用户id")
    private Long userId;

    @NotNull(message = "角色id不为空")
    @Schema(description = "角色id")
    private Long roleId;
}
