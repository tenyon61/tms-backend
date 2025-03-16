package com.tenyon.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tenyon.web.common.BaseResponse;
import com.tenyon.web.common.ResultUtils;
import com.tenyon.web.exception.BusinessException;
import com.tenyon.web.exception.ErrorCode;
import com.tenyon.web.exception.ThrowUtils;
import com.tenyon.web.model.dto.sys.role.RoleAddRequest;
import com.tenyon.web.model.dto.sys.role.RoleQueryRequest;
import com.tenyon.web.model.dto.sys.role.RoleSelectItem;
import com.tenyon.web.model.dto.sys.role.RoleUpdateRequest;
import com.tenyon.web.model.dto.sys.rolemenu.AssignRoleMenuRequest;
import com.tenyon.web.model.entity.sys.SysRole;
import com.tenyon.web.model.vo.sys.role.SysRoleVO;
import com.tenyon.web.service.sys.SysRoleMenuService;
import com.tenyon.web.service.sys.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理接口
 *
 * @author tenyon
 * @date 2025/3/3
 */
@Slf4j
@Tag(name = "RoleController", description = "角色管理接口")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    // region 增删改查

    @Operation(summary = "添加角色")
    @PostMapping("/add")
    public BaseResponse<Long> addRole(@RequestBody RoleAddRequest roleAddRequest) {
        if (roleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(roleAddRequest, sysRole);
        boolean res = sysRoleService.save(sysRole);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(sysRole.getId());
    }

    @Operation(summary = "更新角色")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(roleUpdateRequest, sysRole);
        boolean res = sysRoleService.updateById(sysRole);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteRole(@PathVariable Long id) {
        boolean res = sysRoleService.removeById(id);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "分页获取角色")
    @PostMapping("/listPageVO")
    public BaseResponse<Page<SysRoleVO>> listRoleVOByPage(@RequestBody RoleQueryRequest roleQueryRequest) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        Page<SysRole> rolePage = sysRoleService.page(new Page<>(current, size), sysRoleService.getQueryWrapper(roleQueryRequest));
        Page<SysRoleVO> roleVOPage = new Page<>(current, size, rolePage.getTotal());
        roleVOPage.setRecords(sysRoleService.getRoleVOList(rolePage.getRecords()));
        return ResultUtils.success(roleVOPage);
    }
    // endregion

    @Operation(summary = "获取下拉角色列表")
    @GetMapping("/selectList")
    public BaseResponse<List<RoleSelectItem>> selectRoleList() {
        List<SysRole> list = sysRoleService.list();
        List<RoleSelectItem> result = list.stream().map(role -> {
            RoleSelectItem item = new RoleSelectItem();
            item.setValue(role.getId());
            item.setLabel(role.getRoleName());
            item.setChecked(false);
            return item;
        }).collect(Collectors.toList());
        return ResultUtils.success(result);
    }

    @Operation(summary = "分配角色资源")
    @PostMapping("/assignRoleMenu")
    public BaseResponse<Boolean> assignRoleMenu(@Valid @RequestBody AssignRoleMenuRequest assignRoleMenuRequest) {
        if (assignRoleMenuRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        sysRoleMenuService.assignRoleMenu(assignRoleMenuRequest.getRoleId(), assignRoleMenuRequest.getMenuIds());
        return ResultUtils.success(true);
    }
}
