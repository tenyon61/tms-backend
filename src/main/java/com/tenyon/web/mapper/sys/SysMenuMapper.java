package com.tenyon.web.mapper.sys;

import com.tenyon.web.model.entity.sys.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author tenyon
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2025-03-03 21:44:45
* @Entity com.tms.web.model.entity.sys.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户 id查询菜单
     * @param userId
     * @return
     */
    List<SysMenu> getMenuByUserId(@Param("userId") Long userId);

    List<SysMenu> getMenuByRoleId(@Param("roleId") Long roleId);
}




