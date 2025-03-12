package com.tms.web.model.vo.sys.menu;

import com.tms.web.model.entity.sys.SysMenu;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单树
 *
 * @author tenyon
 * @date 2025/3/10
 */
public class MakeMenuTree {

    public static List<SysMenu> makeTree(List<SysMenu> menuVOList, long pid) {
        // 处理空列表并过滤空元素
        List<SysMenu> safemenuVOList = Optional.ofNullable(menuVOList).orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .toList();

        // 构建父节点到子节点的映射
        Map<Long, List<SysMenu>> parentMap = safemenuVOList.stream()
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


}
