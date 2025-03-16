package com.tenyon.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tenyon.web.common.BaseResponse;
import com.tenyon.web.common.ResultUtils;
import com.tenyon.web.exception.BusinessException;
import com.tenyon.web.exception.ErrorCode;
import com.tenyon.web.exception.ThrowUtils;
import com.tenyon.web.model.dto.sys.menu.MenuAddRequest;
import com.tenyon.web.model.dto.sys.menu.MenuQueryRequest;
import com.tenyon.web.model.dto.sys.menu.MenuUpdateRequest;
import com.tenyon.web.model.entity.sys.SysMenu;
import com.tenyon.web.model.vo.sys.menu.MakeMenuTree;
import com.tenyon.web.model.vo.sys.menu.SysMenuVO;
import com.tenyon.web.service.sys.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述
 *
 * @author tenyon
 * @date 2025/3/10
 */
@Slf4j
@Tag(name = "MenuController", description = "菜单管理接口")
@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "添加菜单")
    @PostMapping("/add")
    public BaseResponse<Long> addMenu(@RequestBody MenuAddRequest menuAddRequest) {
        if (menuAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysMenu sysMenu = new SysMenu();
        BeanUtil.copyProperties(menuAddRequest, sysMenu);
        boolean res = sysMenuService.save(sysMenu);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(sysMenu.getId());
    }

    @Operation(summary = "更新菜单")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateMenu(@RequestBody MenuUpdateRequest menuUpdateRequest) {
        if (menuUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysMenu sysMenu = new SysMenu();
        BeanUtil.copyProperties(menuUpdateRequest, sysMenu);
        boolean res = sysMenuService.updateById(sysMenu);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteMenu(@PathVariable Long id) {
        boolean res = sysMenuService.removeById(id);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "分页获取菜单")
    @PostMapping("/listPageVO")
    public BaseResponse<Page<SysMenuVO>> listMenuVOByPage(@RequestBody MenuQueryRequest menuQueryRequest) {
        long current = menuQueryRequest.getCurrent();
        long size = menuQueryRequest.getPageSize();
        Page<SysMenu> menuPage = sysMenuService.page(new Page<>(current, size), sysMenuService.getQueryWrapper(menuQueryRequest));
        Page<SysMenuVO> menuVOPage = new Page<>(current, size, menuPage.getTotal());
        menuVOPage.setRecords(sysMenuService.getMenuVOList(menuPage.getRecords()));
        return ResultUtils.success(menuVOPage);
    }

    // endregion

    @Operation(summary = "获取菜单树")
    @GetMapping("/getMenuList")
    public BaseResponse<List<SysMenu>> getMenuList() {
        LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList = sysMenuService.list(queryWrapper);
        return ResultUtils.success(MakeMenuTree.makeTree(menuList, 0L));
    }

    @Operation(summary = "获取上级菜单树")
    @GetMapping("/getParentMenuList")
    public BaseResponse<List<SysMenu>> getParentMenuList() {
        return ResultUtils.success(sysMenuService.getParentMenuList());
    }
}
