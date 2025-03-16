package com.tenyon.web.model.vo.sys.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Schema(description = "菜单视图（脱敏）")
public class SysMenuVO implements Serializable {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父级菜单id")
    private Long parentId;

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "权限字段")
    private String code;

    @Schema(description = "路由name")
    private String name;

    @Schema(description = "路由path")
    private String path;

    @Schema(description = "组件路径")
    private String url;

    @Schema(description = "0：目录 1：菜单 2：按钮")
    private Integer type;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "上级菜单名称")
    private String parentName;

    @Schema(description = "序号")
    private Integer orderNum;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "子菜单")
    private List<SysMenuVO> children = new ArrayList<>();

    @Schema(description = "itemValue")
    private long value;

    @Schema(description = "itemLable")
    private String label;

    @Serial
    private static final long serialVersionUID = 1L;

}