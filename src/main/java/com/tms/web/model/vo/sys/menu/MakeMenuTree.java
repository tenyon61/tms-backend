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

    public static List<SysMenuVO> makeTreeVO(List<SysMenuVO> menuList, long pid) {
        // 处理空列表并过滤空元素
        List<SysMenuVO> safeMenuList = Optional.ofNullable(menuList).orElseGet(ArrayList::new)
                .stream()
                .filter(Objects::nonNull)
                .toList();

        // 构建父节点到子节点的映射
        Map<Long, List<SysMenuVO>> parentMap = safeMenuList.stream()
                .collect(Collectors.groupingBy(SysMenuVO::getParentId));

        // 递归构建树结构
        return buildTreeVo(parentMap, pid);
    }

    private static List<SysMenuVO> buildTreeVo(Map<Long, List<SysMenuVO>> parentMap, long pid) {
        List<SysMenuVO> nodes = parentMap.getOrDefault(pid, Collections.emptyList());
        for (SysMenuVO node : nodes) {
            // 设置额外属性（若必须）
            node.setLabel(node.getName());
            node.setValue(node.getId());
            // 递归构建子树
            node.setChildren(buildTreeVo(parentMap, node.getId()));
        }
        return nodes;
    }

}
