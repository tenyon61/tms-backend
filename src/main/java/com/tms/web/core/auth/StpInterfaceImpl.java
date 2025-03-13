package com.tms.web.core.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tms.web.exception.BusinessException;
import com.tms.web.exception.ErrorCode;
import com.tms.web.model.entity.sys.SysMenu;
import com.tms.web.model.entity.sys.SysUser;
import com.tms.web.model.enums.sys.UserRoleEnum;
import com.tms.web.service.sys.SysMenuService;
import com.tms.web.service.sys.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * sa-token
 *
 * @author tenyon
 * @date 2025/1/4
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysUser user = sysUserService.getById((Serializable) loginId);
        List<SysMenu> menuList;
        if (StrUtil.isNotBlank(user.getUserRole()) && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole())) {
            menuList = sysMenuService.list();
        } else {
            menuList = sysMenuService.getMenuByUserId(user.getId());
        }
        if (menuList.stream().noneMatch(Objects::nonNull)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "该用户对应的角色未分配菜单权限，请用管理员账号登录分配菜单");
        }
        // 获取菜单表的code字段
        // 设置返回值
        return Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> ObjectUtil.isNotNull(item) && StrUtil.isNotBlank(item.getCode()))
                .map(SysMenu::getCode)
                .toList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        return null;
    }
}
