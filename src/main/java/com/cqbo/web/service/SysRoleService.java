package com.cqbo.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqbo.web.model.dto.role.RoleQueryRequest;
import com.cqbo.web.model.entity.SysRole;

/**
 * @author tenyon
 * @description 针对表【sys_role(角色表)】的数据库操作Service
 * @createDate 2025-03-03 21:11:05
 */
public interface SysRoleService extends IService<SysRole> {

    QueryWrapper<SysRole> getQueryWrapper(RoleQueryRequest roleQueryRequest);
}
