package com.tenyon.web.service.impl.sys;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tenyon.web.exception.BusinessException;
import com.tenyon.web.exception.ErrorCode;
import com.tenyon.web.mapper.sys.SysRoleMapper;
import com.tenyon.web.model.dto.sys.role.RoleQueryRequest;
import com.tenyon.web.model.entity.sys.SysRole;
import com.tenyon.web.model.vo.sys.role.SysRoleVO;
import com.tenyon.web.service.sys.SysRoleService;
import com.tenyon.web.utils.SqlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tenyon
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2025-03-03 21:11:05
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    @Override
    public SysRoleVO getRoleVO(SysRole sysRole) {
        if (sysRole == null) {
            return null;
        }
        SysRoleVO sysRoleVO = new SysRoleVO();
        BeanUtil.copyProperties(sysRole, sysRoleVO);
        return sysRoleVO;
    }

    @Override
    public List<SysRoleVO> getRoleVOList(List<SysRole> sysRoleList) {
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return new ArrayList<>();
        }
        return sysRoleList.stream().map(this::getRoleVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<SysRole> getQueryWrapper(RoleQueryRequest roleQueryRequest) {
        if (roleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = roleQueryRequest.getId();
        String roleName = roleQueryRequest.getRoleName();
        String type = roleQueryRequest.getType();
        String remark = roleQueryRequest.getRemark();
        String sortField = roleQueryRequest.getSortField();
        String sortOrder = roleQueryRequest.getSortOrder();
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(id), "id", id);
        queryWrapper.like(StrUtil.isNotBlank(roleName), "roleName", roleName);
        queryWrapper.eq(ObjectUtil.isNotEmpty(type), "type", type);
        queryWrapper.like(StrUtil.isNotBlank(remark), "remark", remark);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

}




