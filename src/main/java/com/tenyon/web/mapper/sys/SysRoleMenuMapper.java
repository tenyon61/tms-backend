package com.tenyon.web.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tenyon.web.model.entity.sys.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tenyon
 * @description 针对表【sys_role_menu(角色菜单表)】的数据库操作Mapper
 * @createDate 2025-03-03 21:44:45
 * @Entity com.tenyon.web.model.entity.sys.SysRoleMenu
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 新增角色资源
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    void assignRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);
}




