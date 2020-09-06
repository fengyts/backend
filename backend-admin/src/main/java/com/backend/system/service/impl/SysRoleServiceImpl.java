package com.backend.system.service.impl;

import com.backend.common.ResultData;
import com.backend.system.converter.SysRoleConverter;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.entity.SysRole;
import com.backend.system.mapper.SysRoleMapper;
import com.backend.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter converter;

    @Override
    public List<SysRoleDto> listRoles() {
        List<SysRole> roles = this.list();
        List<SysRoleDto> sysRoleDtos = converter.toEntityDto(roles);
        return sysRoleDtos;
    }

    @Override
    public SysRoleDto selectById(Long id) {
        SysRole sysRole = this.getById(id);
        SysRoleDto dto = converter.toEntityDto(sysRole);
        return dto;
    }

    @Override
    public ResultData saveOrUpdate(SysRoleDto dto) {
        Long id = dto.getId();
        String roleName = dto.getRoleName();

        SysRole sysRole = converter.toEntity(dto);
        LambdaQueryWrapper<SysRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysRole::getRoleName, roleName);
        if (null == id) {
            if(StringUtils.isBlank(roleName)){
                return ResultData.errParam();
            }
            List<SysRole> dbExits = this.list(lqw);
            if (CollectionUtils.isNotEmpty(dbExits)) {
                return ResultData.err("角色已存在");
            }
        }
        this.saveOrUpdate(sysRole);
        return ResultData.ok();
    }

}
