package com.cqbo.web.controller;

import com.cqbo.web.common.BaseResponse;
import com.cqbo.web.common.DeleteRequest;
import com.cqbo.web.common.ResultUtils;
import com.cqbo.web.annotation.AuthCheck;
import com.cqbo.web.constant.BmsConstant;
import com.cqbo.web.constant.UserConstant;
import com.cqbo.web.exception.BusinessException;
import com.cqbo.web.exception.ErrorCode;
import com.cqbo.web.exception.ThrowUtils;
import com.cqbo.web.model.dto.user.UserAddRequest;
import com.cqbo.web.model.dto.user.UserQueryRequest;
import com.cqbo.web.model.dto.user.UserUpdateMyRequest;
import com.cqbo.web.model.dto.user.UserUpdateRequest;
import com.cqbo.web.model.entity.User;
import com.cqbo.web.model.vo.user.UserVO;
import com.cqbo.web.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    // region 增删改查

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "创建用户")
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 11111
        String defaultPassword = "11111";
        String encryptPassword = DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);

        boolean res = userService.save(user);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean res = userService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(res);
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean res = userService.updateById(user);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "根据 id 获取用户（仅管理员）")
    @GetMapping("/get")
    public BaseResponse<User> getUserById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @Operation(summary = "根据 id 获取包装类")
    @GetMapping("/getVO")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "分页获取用户列表（仅管理员）")
    @PostMapping("/listPage")
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
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
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVOList(userPage.getRecords());
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
        User loginUser = userService.getLoginUser();
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean res = userService.updateById(user);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
