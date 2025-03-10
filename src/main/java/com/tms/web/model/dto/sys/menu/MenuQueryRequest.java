package com.tms.web.model.dto.sys.menu;

import com.tms.web.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "菜单查询请求参数")
public class MenuQueryRequest extends PageRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "路由name")
    private String name;

    @Schema(description = "路由path")
    private String path;

    @Schema(description = "组件路径")
    private String url;

    @Schema(description = "0：目录 1：菜单 2：按钮")
    private Integer type;

    @Schema(description = "上级菜单名称")
    private String parentName;

    @Serial
    private static final long serialVersionUID = 1L;

}