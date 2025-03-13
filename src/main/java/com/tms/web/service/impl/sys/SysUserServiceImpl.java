package com.tms.web.service.impl.sys;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tms.web.constant.BmsConstant;
import com.tms.web.constant.UserConstant;
import com.tms.web.core.auth.StpKit;
import com.tms.web.exception.BusinessException;
import com.tms.web.exception.ErrorCode;
import com.tms.web.exception.ThrowUtils;
import com.tms.web.mapper.sys.SysUserMapper;
import com.tms.web.model.dto.sys.user.UserQueryRequest;
import com.tms.web.model.entity.sys.SysMenu;
import com.tms.web.model.entity.sys.SysUser;
import com.tms.web.model.entity.sys.SysUserRole;
import com.tms.web.model.enums.sys.UserRoleEnum;
import com.tms.web.model.vo.sys.menu.AssignTreeVO;
import com.tms.web.model.vo.sys.menu.MakeMenuTree;
import com.tms.web.model.vo.sys.menu.SysMenuVO;
import com.tms.web.model.vo.sys.user.LoginUserVO;
import com.tms.web.model.vo.sys.user.SingleUserVO;
import com.tms.web.model.vo.sys.user.SysUserVO;
import com.tms.web.service.sys.SysMenuService;
import com.tms.web.service.sys.SysUserRoleService;
import com.tms.web.service.sys.SysUserService;
import com.tms.web.utils.SqlUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public long register(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 1.账户不能重复
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2.加密
            String encryptPassword = DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + userPassword).getBytes());
            // 3.插入数据
            SysUser sysUser = new SysUser();
            sysUser.setUserAccount(userAccount);
            sysUser.setUserPassword(encryptPassword);
            sysUser.setUserRole(UserRoleEnum.USER.getValue());
            boolean saveResult = this.save(sysUser);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return sysUser.getId();
        }
    }

    @Override
    public LoginUserVO login(String userAccount, String userPassword) {
        // 1. 校验
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((BmsConstant.ENCRYPT_SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        SysUser sysUser = this.getOne(queryWrapper);
        // 用户不存在
        if (sysUser == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        StpKit.BMS.login(sysUser.getId());
        StpKit.BMS.getSession().set(UserConstant.USER_LOGIN_STATE, sysUser);
        return this.getLoginUserVO(sysUser);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @Override
    public SysUser getLoginUser() {
        // 先判断是否已登录
        ThrowUtils.throwIf(!StpKit.BMS.isLogin(), ErrorCode.NOT_LOGIN_ERROR);
        Object userObj = StpKit.BMS.getSession().get(UserConstant.USER_LOGIN_STATE);
        SysUser currentSysUser = (SysUser) userObj;
        if (currentSysUser == null || currentSysUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        long userId = currentUser.getId();
//        currentUser = userService.getById(userId);
//        if (currentUser == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
//        }
        return currentSysUser;
    }

    @Override
    public boolean logout() {
        ThrowUtils.throwIf(!StpKit.BMS.isLogin(), ErrorCode.NOT_LOGIN_ERROR, "暂未登录");
        // 移除登录态
        StpKit.BMS.getSession().removeTokenSign(UserConstant.USER_LOGIN_STATE);
        StpKit.BMS.logout();
        return true;
    }

    @Transactional
    @Override
    public Long saveUser(SysUser sysUser) {
        boolean res = this.save(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR, "新增用户失败！");
        String roleIdStr = sysUser.getRoleIds();
        if (StrUtil.isBlank(roleIdStr)) {
            return sysUser.getId();
        }
        String[] roleIds = roleIdStr.split(",");
        List<SysUserRole> userRoles = new ArrayList<>();
        for (String roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(Long.parseLong(roleId));
            userRoles.add(sysUserRole);
        }
        boolean res2 = sysUserRoleService.saveBatch(userRoles);
        ThrowUtils.throwIf(!res2, ErrorCode.OPERATION_ERROR, "级联新增用户角色失败！");
        return sysUser.getId();
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        boolean res = this.removeById(id);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR, "删除用户失败！");
        LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRole::getUserId, id);
        sysUserRoleService.remove(queryWrapper);
    }

    @Transactional
    @Override
    public void updateUser(SysUser sysUser) {
        boolean res = this.updateById(sysUser);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        // 删除用户原来的角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRole::getUserId, sysUser.getId());
        sysUserRoleService.remove(queryWrapper);
        // 把前端逗号分割的字符串转成数组
        String roleIdStr = sysUser.getRoleIds();
        if (StrUtil.isBlank(roleIdStr)) {
            return;
        }
        String[] roleIds = roleIdStr.split(",");
        // 重新插入
        List<SysUserRole> userRoles = new ArrayList<>();
        for (String roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        }
        // 保存到用户角色表
        boolean res2 = sysUserRoleService.saveBatch(userRoles);
        ThrowUtils.throwIf(!res2, ErrorCode.OPERATION_ERROR, "级联更新用户角色失败！");

    }

    @Override
    public List<Long> getRoleList(Long id) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(SysUserRole::getRoleId).eq(SysUserRole::getUserId, id);
        return sysUserRoleService.listObjs(queryWrapper);
    }

    @Override
    public LoginUserVO getLoginUserVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(sysUser, loginUserVO);
        // 组装token
        SaTokenInfo tokenInfo = StpKit.BMS.getTokenInfo();
        loginUserVO.setToken(tokenInfo.tokenValue);
        return loginUserVO;
    }

    @Override
    public SysUserVO getUserVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        SysUserVO sysUserVO = new SysUserVO();
        BeanUtil.copyProperties(sysUser, sysUserVO);
        return sysUserVO;
    }

    @Override
    public List<SysUserVO> getUserVOList(List<SysUser> sysUserList) {
        if (CollectionUtils.isEmpty(sysUserList)) {
            return new ArrayList<>();
        }
        return sysUserList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public AssignTreeVO getAssignTreeVO(Long userId, Long roleId) {
        SysUser user = this.getById(userId);
        List<SysMenu> menuList;
        if (StrUtil.isNotBlank(user.getUserRole()) && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole())) {
            menuList = sysMenuService.list();
        } else {
            menuList = sysMenuService.getMenuByUserId(userId);
        }
        List<SysMenu> treeList = MakeMenuTree.makeTree(menuList, 0L);
        // 查询原来的菜单
        List<SysMenu> roleMenuList = sysMenuService.getMenuByRoleId(roleId);
        List<Long> ids = new ArrayList<>();
        if (CollUtil.isNotEmpty(roleMenuList)) {
            roleMenuList.stream().filter(ObjectUtil::isNotNull).forEach(roleMenu -> {
                ids.add(roleMenu.getId());
            });
        }
        AssignTreeVO assignTreeVO = new AssignTreeVO();
        assignTreeVO.setCheckList(ids.toArray());
        assignTreeVO.setMenuList(treeList);
        return assignTreeVO;
    }

    @Override
    public SingleUserVO getSingleUser(Long id) {
        SysUser user = this.getById(id);
        List<SysMenu> menuList;
        if (StrUtil.isNotBlank(user.getUserRole()) && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole())) {
            menuList = sysMenuService.list();
        } else {
            menuList = sysMenuService.getMenuByUserId(id);
        }
        // 获取菜单的code
        List<String> collect = Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> ObjectUtil.isNotNull(item) && StrUtil.isNotBlank(item.getCode()))
                .map(SysMenu::getCode)
                .toList();
        // 转换为数组
        //String[] strings = collect.toArray(new String[collect.size()]);
        // 设置返回值
        SingleUserVO singleUserVO = new SingleUserVO();
        singleUserVO.setUserName(user.getUserName());
        singleUserVO.setId(id);
        singleUserVO.setPermissions(collect.toArray());
        return singleUserVO;
    }

    @Override
    public QueryWrapper<SysUser> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String email = userQueryRequest.getEmail();
        String phone = userQueryRequest.getPhone();
        String userProfile = userQueryRequest.getUserProfile();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StrUtil.isNotBlank(email), "email", email);
        queryWrapper.like(StrUtil.isNotBlank(phone), "phone", phone);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

}
