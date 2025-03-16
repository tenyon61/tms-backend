package com.tenyon.web.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tenyon.web.model.entity.sys.SysRoleMenu;

import java.util.List;

/**
 * @author tenyon
 * @description 针对表【sys_role_menu(角色菜单表)】的数据库操作Service
 * @createDate 2025-03-03 21:44:45
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 新增角色资源
     *
     * @param roleId
     * @param menuIds
     */
    void assignRoleMenu(Long roleId, List<Long> menuIds);
}
