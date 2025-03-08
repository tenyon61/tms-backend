package com.tms.web.service;

import com.tms.web.model.dto.user.UserQueryRequest;
import com.tms.web.model.entity.SysUser;
import com.tms.web.model.vo.user.LoginUserVO;
import com.tms.web.model.vo.user.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户服务
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    LoginUserVO login(String userAccount, String userPassword);

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long register(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    SysUser getLoginUser();

    /**
     * 用户注销
     *
     * @return
     */
    boolean logout();

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(SysUser sysUser);

    /**
     * 获取脱敏的用户信息
     *
     * @param sysUser
     * @return
     */
    UserVO getUserVO(SysUser sysUser);

    /**
     * 获取脱敏的用户信息
     *
     * @param sysUserList
     * @return
     */
    List<UserVO> getUserVOList(List<SysUser> sysUserList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<SysUser> getQueryWrapper(UserQueryRequest userQueryRequest);

}
