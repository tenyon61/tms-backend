package com.tenyon.web.service.impl.sys;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tenyon.web.exception.ErrorCode;
import com.tenyon.web.exception.ThrowUtils;
import com.tenyon.web.model.entity.sys.SysRoleMenu;
import com.tenyon.web.service.sys.SysRoleMenuService;
import com.tenyon.web.mapper.sys.SysRoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tenyon
 * @description 针对表【sys_role_menu(角色菜单表)】的数据库操作Service实现
 * @createDate 2025-03-03 21:44:45
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Transactional
    @Override
    public void assignRoleMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(queryWrapper);
        List<SysRoleMenu> roleMenus = new ArrayList<>();
        if (CollUtil.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                roleMenus.add(sysRoleMenu);
            }
            boolean res2 = this.saveBatch(roleMenus);
            ThrowUtils.throwIf(!res2, ErrorCode.OPERATION_ERROR, "新增角色资源关联失败");
        }
    }
}




