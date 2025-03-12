package com.tms.web.service.impl.sys;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tms.web.exception.BusinessException;
import com.tms.web.exception.ErrorCode;
import com.tms.web.mapper.sys.SysMenuMapper;
import com.tms.web.model.dto.sys.menu.MenuQueryRequest;
import com.tms.web.model.entity.sys.SysMenu;
import com.tms.web.model.vo.sys.menu.MakeMenuTree;
import com.tms.web.model.vo.sys.menu.SysMenuVO;
import com.tms.web.service.sys.SysMenuService;
import com.tms.web.utils.SqlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tenyon
 * @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
 * @createDate 2025-03-03 21:44:45
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public SysMenuVO getMenuVO(SysMenu sysMenu) {
        if (sysMenu == null) {
            return null;
        }
        SysMenuVO sysMenuVO = new SysMenuVO();
        BeanUtil.copyProperties(sysMenu, sysMenuVO);
        return sysMenuVO;
    }

    @Override
    public List<SysMenuVO> getMenuVOList(List<SysMenu> sysMenuList) {
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return new ArrayList<>();
        }
        return sysMenuList.stream().map(this::getMenuVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<SysMenu> getQueryWrapper(MenuQueryRequest menuQueryRequest) {

        if (menuQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = menuQueryRequest.getId();
        String title = menuQueryRequest.getTitle();
        String name = menuQueryRequest.getName();
        String path = menuQueryRequest.getPath();
        String url = menuQueryRequest.getUrl();
        Integer type = menuQueryRequest.getType();
        String parentName = menuQueryRequest.getParentName();
        String sortField = menuQueryRequest.getSortField();
        String sortOrder = menuQueryRequest.getSortOrder();
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(id), "id", id);
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(path), "path", path);
        queryWrapper.like(StrUtil.isNotBlank(url), "url", url);
        queryWrapper.eq(ObjectUtil.isNotEmpty(type), "type", type);
        queryWrapper.like(StrUtil.isNotBlank(parentName), "parentName", parentName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public List<SysMenu> getParentMenuList() {
        List<Integer> menuTypeList = Arrays.asList(0, 1);
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(SysMenu::getType, menuTypeList).orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> list = this.list(lambdaQueryWrapper);
        // 组装根节点
        SysMenu menu = new SysMenu();
        menu.setTitle("根节点");
        menu.setLabel("根节点");
        menu.setParentId(-1L);
        menu.setId(0L);
        menu.setValue(0L);
        list.add(menu);
        // 组装树
        return MakeMenuTree.makeTree(list, -1L);
    }

    @Override
    public List<SysMenu> getMenuByUserId(Long userId) {
        return baseMapper.getMenuByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenuByRoleId(Long roleId) {
        return baseMapper.getMenuByRoleId(roleId);
    }

}




