package com.tms.web.model.vo.sys.menu;

import cn.hutool.core.collection.CollUtil;
import com.tms.web.model.entity.sys.SysMenu;
import com.tms.web.model.enums.sys.MenuTypeEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单树
 *
 * @author tenyon
 * @date 2025/3/10
 */
public class MakeMenuTree {

    public static List<SysMenu> makeTree(List<SysMenu> menuList, long pid) {
        // 处理空列表并过滤空元素
        List<SysMenu> safemenuList = Optional.ofNullable(menuList).orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .toList();

        // 构建父节点到子节点的映射
        Map<Long, List<SysMenu>> parentMap = safemenuList.stream()
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        // 递归构建树结构
        return buildTree(parentMap, pid);
    }

    private static List<SysMenu> buildTree(Map<Long, List<SysMenu>> parentMap, long pid) {
        List<SysMenu> nodes = parentMap.getOrDefault(pid, Collections.emptyList());
        for (SysMenu node : nodes) {
            // 设置额外属性（若必须）
            node.setLabel(node.getName());
            node.setValue(node.getId());
            // 递归构建子树
            node.setChildren(buildTree(parentMap, node.getId()));
        }
        return nodes;
    }

    public static List<RouterVO> makeRouter(List<SysMenu> menuList, Long pid) {
        // 处理空值并创建分组Map
        List<SysMenu> nonNullMenus = Optional.ofNullable(menuList).orElseGet(ArrayList::new);
        Map<Long, List<SysMenu>> menuGroup = nonNullMenus.stream()
                .filter(menu -> menu != null && menu.getParentId() != null)
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        return buildRouterTree(pid, menuGroup);
    }

    private static List<RouterVO> buildRouterTree(Long pid, Map<Long, List<SysMenu>> menuGroup) {
        List<RouterVO> routers = new ArrayList<>();

        menuGroup.getOrDefault(pid, Collections.emptyList()).forEach(menu -> {
            RouterVO router = createBaseRouter(menu);

            // 递归构建子路由
            List<RouterVO> children = buildRouterTree(menu.getId(), menuGroup);
            router.setChildren(children);

            // 处理组件类型和特殊布局
            if (isRootMenu(menu)) {
                handleRootMenu(router, menu);
            } else {
                router.setComponent(menu.getUrl());
            }

            // 设置路由元信息
            router.setMeta(buildMetaInfo(menu));
            routers.add(router);
        });

        return routers;
    }

    private static RouterVO createBaseRouter(SysMenu menu) {
        RouterVO router = new RouterVO();
        router.setName(menu.getName());
        router.setPath(menu.getPath());
        return router;
    }

    private static boolean isRootMenu(SysMenu menu) {
        return menu.getParentId() != null && menu.getParentId() == 0L;
    }

    private static void handleRootMenu(RouterVO router, SysMenu menu) {
        router.setComponent("ProLayout");

        if (menu.getType() == MenuTypeEnum.DIRECT.getValue()) {
            if (CollUtil.isNotEmpty(router.getChildren())) {
                router.setRedirect(router.getChildren().get(0).getPath());
            }
        } else if (menu.getType() == MenuTypeEnum.MENU.getValue()) {
            // 创建自动跳转子路由
            RouterVO defaultChild = createAutoRedirectChild(menu);
            // 保留原有子路由并添加默认跳转路由
            List<RouterVO> mergedChildren = new ArrayList<>(router.getChildren());
            mergedChildren.add(defaultChild);
            router.setChildren(mergedChildren);

            router.setRedirect(menu.getPath());
            router.setPath(menu.getPath() + "/parent");
            router.setName(menu.getName() + "_parent");
        }
    }

    private static RouterVO createAutoRedirectChild(SysMenu menu) {
        RouterVO child = new RouterVO();
        child.setName(menu.getName());
        child.setPath("");
        child.setComponent(menu.getUrl());
        child.setMeta(buildMetaInfo(menu));
        return child;
    }

    private static RouterVO.Meta buildMetaInfo(SysMenu menu) {
        String[] codes = Optional.ofNullable(menu.getCode())
                .map(s -> s.split(","))
                .orElse(new String[0]);

        return new RouterVO().new Meta(
                menu.getTitle(),
                menu.getIcon(),
                codes
        );
    }

}
