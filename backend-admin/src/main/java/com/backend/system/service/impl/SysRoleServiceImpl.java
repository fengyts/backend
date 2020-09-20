package com.backend.system.service.impl;

import com.backend.common.ResultData;
import com.backend.system.converter.SysRoleConverter;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.dto.SysRoleMenuDto;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysRoleMenu;
import com.backend.system.mapper.SysRoleMapper;
import com.backend.system.service.ISysMenuService;
import com.backend.system.service.ISysRoleMenuService;
import com.backend.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

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
    @Transactional
    public ResultData saveOrUpdate(SysRoleDto dto) {
        Long id = dto.getId();
        String roleName = dto.getRoleName();

        SysRole sysRole = converter.toEntity(dto);
        LambdaQueryWrapper<SysRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysRole::getRoleName, roleName);
        if (null == id) {
            if (StringUtils.isBlank(roleName)) {
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

    @Override
    public List<SysRoleMenuDto> getSysRoleMenus(Long roleId) {
        List<SysRoleMenuDto> sysRoleMenus = sysMenuService.getAllMenusByTier();

        LambdaQueryWrapper<SysRoleMenu> lqwRM = Wrappers.lambdaQuery();
        lqwRM.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(lqwRM);

        if (CollectionUtils.isNotEmpty(roleMenus)) {
            List<Long> roleMenuIds = roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            setRoleBoundedMenu(sysRoleMenus, roleMenuIds);
        }

        return sysRoleMenus;
    }

    private List<SysRoleMenuDto> setRoleBoundedMenu(List<SysRoleMenuDto> sysRoleMenus, List<Long> roleMenuIds) {
        List<SysRoleMenuDto> temp = sysRoleMenus.stream().peek(sm -> {
            List<SysRoleMenuDto> subRoleMenus = sm.getSubRoleMenus();
            if (CollectionUtils.isNotEmpty(subRoleMenus)) {
                setRoleBoundedMenu(subRoleMenus, roleMenuIds);
                // eleTree的坑：如果存在子节点，父节点checked状态不能设置为true，否则子节点都会是选中状态
                sm.setChecked(false);
                sm.setIsLeaf(false);
            } else {
                sm.setChecked(roleMenuIds.contains(sm.getId()));
                sm.setIsLeaf(true);
            }
        }).collect(Collectors.toList());
        return temp;
    }

    @Override
    @Transactional
    public ResultData associateRoleMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenu> lwq = Wrappers.lambdaQuery();
        lwq.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> records = menuIds.stream().map(id -> {
            SysRoleMenu record = new SysRoleMenu();
            record.setRoleId(roleId);
            record.setMenuId(id);
            return record;
        }).collect(Collectors.toList());
        sysRoleMenuService.remove(lwq); // 删除roleId下所有关联菜单，然后重新关联
        sysRoleMenuService.saveBatch(records);
        return ResultData.ok();
    }

}
