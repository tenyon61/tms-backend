package com.tms.web.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tms.web.model.dto.sys.user.UserQueryRequest;
import com.tms.web.model.entity.sys.SysUser;
import com.tms.web.model.vo.sys.menu.AssignTreeVO;
import com.tms.web.model.vo.sys.user.LoginUserVO;
import com.tms.web.model.vo.sys.user.SysUserVO;

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
    SysUserVO getUserVO(SysUser sysUser);

    /**
     * 获取脱敏的用户信息
     *
     * @param sysUserList
     * @return
     */
    List<SysUserVO> getUserVOList(List<SysUser> sysUserList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<SysUser> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 保存用户和关联角色
     *
     * @param sysUser
     * @return
     */
    Long saveUser(SysUser sysUser);

    /**
     * 删除用户和关联角色
     *
     * @param id
     */
    void removeUser(Long id);

    /**
     * 更新用户和关联角色
     *
     * @param sysUser
     */
    void updateUser(SysUser sysUser);

    /**
     * 获取用户对应角色
     * @param id
     * @return
     */
    List<Long> getRoleList(Long id);



    /**
     * 获取分配的资源树
     *
     * @param userId
     * @param roleId
     * @return
     */
    AssignTreeVO getAssignTreeVO(Long userId, Long roleId);

}
