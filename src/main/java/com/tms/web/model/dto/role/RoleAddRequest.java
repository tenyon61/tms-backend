package com.tms.web.model.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "新增角色请求参数")
public class RoleAddRequest implements Serializable {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "扩展字段")
    private String type;

    @Schema(description = "备注")
    private String remark;

    @Serial
    private static final long serialVersionUID = 1L;
}