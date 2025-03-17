package com.tenyon.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tenyon.web.common.BaseResponse;
import com.tenyon.web.common.ResultUtils;
import com.tenyon.web.constant.BmsConstant;
import com.tenyon.web.exception.BusinessException;
import com.tenyon.web.exception.ErrorCode;
import com.tenyon.web.exception.ThrowUtils;
import com.tenyon.web.model.dto.sys.menu.AssignTreeRequest;
import com.tenyon.web.model.dto.sys.user.UserAddRequest;
import com.tenyon.web.model.dto.sys.user.UserQueryRequest;
import com.tenyon.web.model.dto.sys.user.UserUpdateMyRequest;
import com.tenyon.web.model.dto.sys.user.UserUpdateRequest;
import com.tenyon.web.model.entity.sys.SysUser;
import com.tenyon.web.model.vo.sys.menu.AssignTreeVO;
import com.tenyon.web.model.vo.sys.user.SysUserVO;
import com.tenyon.web.service.sys.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户接口
 *
 * @author tenyon
 * @date 2025/1/6
 */
@Tag(name = "UserController", description = "用户管理接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    // region 增删改查

    @Operation(summary = "创建用户")
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(userAddRequest, sysUser);
        // 默认密码 11111
        String encryptPassword = DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + "11111").getBytes());
        sysUser.setUserPassword(encryptPassword);
        return ResultUtils.success(sysUserService.saveUser(sysUser));
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable long id) {
        sysUserService.removeUser(id);
        return ResultUtils.success(true);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(userUpdateRequest, sysUser);
        sysUserService.updateUser(sysUser);
        return ResultUtils.success(true);
    }

    @Operation(summary = "根据 id 获取用户（仅管理员）")
    @GetMapping("/get")
    public BaseResponse<SysUser> getUserById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser sysUser = sysUserService.getById(id);
        ThrowUtils.throwIf(sysUser == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(sysUser);
    }

    @Operation(summary = "根据 id 获取包装类")
    @GetMapping("/getVO")
    public BaseResponse<SysUserVO> getUserVOById(long id) {
        BaseResponse<SysUser> response = getUserById(id);
        SysUser sysUser = response.getData();
        return ResultUtils.success(sysUserService.getUserVO(sysUser));
    }

    @Operation(summary = "分页获取用户列表（仅管理员）")
    @PostMapping("/listPage")
    public BaseResponse<Page<SysUser>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<SysUser> userPage = sysUserService.page(new Page<>(current, size),
                sysUserService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    @Operation(summary = "分页获取用户封装列表（仅管理员）")
    @PostMapping("/listPageVO")
    public BaseResponse<Page<SysUserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<SysUser> userPage = sysUserService.page(new Page<>(current, size),
                sysUserService.getQueryWrapper(userQueryRequest));
        Page<SysUserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<SysUserVO> sysUserVO = sysUserService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(sysUserVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    @Operation(summary = "更新个人信息")
    @PutMapping("/updateMy")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser loginSysUser = sysUserService.getLoginUser();
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(userUpdateMyRequest, sysUser);
        sysUser.setId(loginSysUser.getId());
        boolean res = sysUserService.updateById(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "获取用户角色列表")
    @GetMapping("/getUserRoleList")
    public BaseResponse<List<Long>> getUserRoleList(long id) {
        return ResultUtils.success(sysUserService.getRoleList(id));
    }

    @Operation(summary = "重置密码")
    @PutMapping("/resetPwd/{id}")
    public BaseResponse<Boolean> resetPwd(@PathVariable long id) {
        SysUser sysUser = sysUserService.getById(id);
        sysUser.setUserPassword(DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + "11111").getBytes()));
        boolean res = sysUserService.updateById(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR, "密码重置失败");
        return ResultUtils.success(true);
    }

    @Operation(summary = "获取分配菜单树")
    @PostMapping("/getAssignTree")
    public BaseResponse<AssignTreeVO> getAssignTreeVO(@Valid @RequestBody AssignTreeRequest assignTreeRequest) {
        if (assignTreeRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(sysUserService.getAssignTreeVO(assignTreeRequest.getUserId(), assignTreeRequest.getRoleId()));
    }

//    @Operation(summary = "获取单用户信息")
//    @GetMapping("/getUserInfo")
//    public BaseResponse<SingleUserVO> getSingleUser(@RequestParam Long id) {
//        return ResultUtils.success(sysUserService.getSingleUser(id));
//    }
}
