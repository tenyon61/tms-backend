package com.tenyon.web.model.dto.sys.rolemenu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 新增资源分配请求参数
 *
 * @author tenyon
 * @date 2025/3/12
 */

@Data
@Schema(description = "新增资源分配请求参数")
public class AssignRoleMenuRequest {

    @NotNull(message = "角色id不为空")
    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "资源ids")
    private List<Long> menuIds;
}
