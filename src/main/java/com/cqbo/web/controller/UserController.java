package com.cqbo.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqbo.web.annotation.AuthCheck;
import com.cqbo.web.common.BaseResponse;
import com.cqbo.web.common.DeleteRequest;
import com.cqbo.web.common.ResultUtils;
import com.cqbo.web.constant.BmsConstant;
import com.cqbo.web.constant.UserConstant;
import com.cqbo.web.exception.BusinessException;
import com.cqbo.web.exception.ErrorCode;
import com.cqbo.web.exception.ThrowUtils;
import com.cqbo.web.model.dto.user.UserAddRequest;
import com.cqbo.web.model.dto.user.UserQueryRequest;
import com.cqbo.web.model.dto.user.UserUpdateMyRequest;
import com.cqbo.web.model.dto.user.UserUpdateRequest;
import com.cqbo.web.model.entity.SysUser;
import com.cqbo.web.model.vo.user.UserVO;
import com.cqbo.web.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
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
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userAddRequest, sysUser);
        // 默认密码 11111
        String defaultPassword = "11111";
        String encryptPassword = DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + defaultPassword).getBytes());
        sysUser.setUserPassword(encryptPassword);

        boolean res = sysUserService.save(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(sysUser.getId());
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean res = sysUserService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(res);
    }

    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateRequest, sysUser);
        boolean res = sysUserService.updateById(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
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
    public BaseResponse<UserVO> getUserVOById(long id) {
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
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<SysUser> userPage = sysUserService.page(new Page<>(current, size),
                sysUserService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = sysUserService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    @Operation(summary = "更新个人信息")
    @PostMapping("/updateMy")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SysUser loginSysUser = sysUserService.getLoginUser();
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateMyRequest, sysUser);
        sysUser.setId(loginSysUser.getId());
        boolean res = sysUserService.updateById(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
