package com.tms.web.model.vo.sys.menu;

import lombok.Data;

import java.util.List;

/**
 * 分配资源树
 *
 * @author tenyon
 * @date 2025/3/12
 */

@Data
public class AssignTreeVO {
    private List<SysMenuVO> menuList;
    private Object[] checkList;
}
