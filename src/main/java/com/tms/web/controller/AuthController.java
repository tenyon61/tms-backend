package com.tms.web.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.tms.web.common.BaseResponse;
import com.tms.web.common.ResultUtils;
import com.tms.web.exception.BusinessException;
import com.tms.web.exception.ErrorCode;
import com.tms.web.exception.ThrowUtils;
import com.tms.web.model.dto.sys.user.UserLoginRequest;
import com.tms.web.model.dto.sys.user.UserRegisterRequest;
import com.tms.web.model.entity.sys.SysMenu;
import com.tms.web.model.entity.sys.SysUser;
import com.tms.web.model.vo.sys.menu.MakeMenuTree;
import com.tms.web.model.vo.sys.menu.RouterVO;
import com.tms.web.model.vo.sys.user.LoginUserVO;
import com.tms.web.service.sys.SysMenuService;
import com.tms.web.service.sys.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 授权管理
 *
 * @author tenyon
 * @date 2025/1/6
 */
@Tag(name = "AuthController", description = "通用授权接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = sysUserService.login(userAccount, userPassword);
        return ResultUtils.success(loginUserVO);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long userId = sysUserService.register(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userId);
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout() {
        boolean res = sysUserService.logout();
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(res);
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser() {
        SysUser loginSysUser = sysUserService.getLoginUser();
        return ResultUtils.success(sysUserService.getLoginUserVO(loginSysUser));
    }

    @Operation(summary = "获取权限路由菜单")
    @GetMapping("/getAuthMenuList")
    public BaseResponse<List<RouterVO>> getAuthMenuList(long userId) {
        List<SysMenu> menus = sysMenuService.getMenuByUserId(userId);
        ThrowUtils.throwIf(CollUtil.isEmpty(menus), ErrorCode.OPERATION_ERROR, "没有权限菜单");
        List<SysMenu> collect = menus.stream().filter(menu -> ObjectUtil.isNotNull(menu) && menu.getType() != 2).toList();
        List<RouterVO> routerVOS = MakeMenuTree.makeRouter(collect, 0L);
        return ResultUtils.success(routerVOS);
    }

}
