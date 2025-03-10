package com.tms.web.model.dto.sys.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "下拉角色")
public class RoleSelectItem implements Serializable {

    @Schema(description = "角色id")
    private Long value;

    @Schema(description = "角色名称")
    private String label;

    @Schema(description = "是否选中")
    private Boolean checked;

    @Serial
    private static final long serialVersionUID = 1L;
}
