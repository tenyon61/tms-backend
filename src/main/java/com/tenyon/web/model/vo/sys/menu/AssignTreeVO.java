package com.tenyon.web.model.vo.sys.menu;

import com.tenyon.web.model.entity.sys.SysMenu;
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
    private List<SysMenu> menuList;
    private Object[] checkList;
}
