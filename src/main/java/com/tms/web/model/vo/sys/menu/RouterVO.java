package com.tms.web.model.vo.sys.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态路由实体
 *
 * @author tenyon
 * @date 2025/3/12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "路由视图")
public class RouterVO {
    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "重定向地址")
    private String redirect;

    @Schema(description = "元数据")
    private Meta meta;

    @Data
    @AllArgsConstructor
    public class Meta {
        @Schema(description = "标题")
        private String title;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "所需角色s")
        private Object[] roles;
    }

    @Schema(description = "子路由")
    private List<RouterVO> children = new ArrayList<>();
}
