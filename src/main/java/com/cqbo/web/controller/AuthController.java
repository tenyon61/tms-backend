package com.cqbo.web.controller;

import cn.hutool.core.util.StrUtil;
import com.cqbo.web.common.BaseResponse;
import com.cqbo.web.common.ResultUtils;
import com.cqbo.web.exception.BusinessException;
import com.cqbo.web.exception.ErrorCode;
import com.cqbo.web.exception.ThrowUtils;
import com.cqbo.web.model.dto.user.UserLoginRequest;
import com.cqbo.web.model.dto.user.UserRegisterRequest;
import com.cqbo.web.model.entity.User;
import com.cqbo.web.model.vo.user.LoginUserVO;
import com.cqbo.web.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    private UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.login(userAccount, userPassword);
        return ResultUtils.success(loginUserVO);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.register(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userId);
    }

    @Operation(summary = "用户注销")
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout() {
        boolean res = userService.logout();
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(res);
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser() {
        User loginUser = userService.getLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

}
