package com.backend.system.service.impl;

import com.backend.common.ResultData;
import com.backend.enums.SysMenuShowStatusEnum;
import com.backend.system.converter.SysRoleConverter;
import com.backend.system.dto.SysRoleDto;
import com.backend.system.dto.SysRoleMenuDto;
import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysRoleMenu;
import com.backend.system.mapper.SysRoleMapper;
import com.backend.system.service.ISysMenuService;
import com.backend.system.service.ISysRoleMenuService;
import com.backend.system.service.ISysRoleService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        List<SysRoleMenuDto> sysRoleMenus = Lists.newArrayList();
        LambdaQueryWrapper<SysMenu> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysMenu::getIsShow, SysMenuShowStatusEnum.VALID.getCode());
        List<SysMenu> menuList = sysMenuService.list(lqw);

        LambdaQueryWrapper<SysRoleMenu> lqwRM = Wrappers.lambdaQuery();
        lqwRM.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(lqwRM);

        if (CollectionUtils.isEmpty(roleMenus)) {
            sysRoleMenus = menuList.stream().map(temp -> {
                SysRoleMenuDto dto = new SysRoleMenuDto();
                BeanUtils.copyProperties(temp, dto);
                return dto;
            }).collect(Collectors.toList());
        } else {
            sysRoleMenus = menuList.stream().map(temp -> {
                SysRoleMenuDto dto = new SysRoleMenuDto();
                BeanUtils.copyProperties(temp, dto);

                Long menuId = temp.getId();
                roleMenus.forEach(rm -> {
                    Long mid = rm.getMenuId();
                    if (menuId.longValue() == mid.longValue()) {
                        dto.setChecked(true);
                        return;
                    }
                });
                return dto;
            }).collect(Collectors.toList());
        }
        return sysRoleMenus;
    }

    @Override
    public ResultData associateRoleMenu(Long roleId, List<String> menus) {
        LambdaQueryWrapper<SysRoleMenu> lwq = Wrappers.lambdaQuery();
        lwq.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuService.remove(lwq);
        List<SysRoleMenu> records = menus.stream().map(m -> {
            SysRoleMenu record = new SysRoleMenu();
            record.setRoleId(roleId);
            record.setMenuId(Long.valueOf(m));
            return record;
        }).collect(Collectors.toList());
        sysRoleMenuService.saveBatch(records);
        return ResultData.ok();
    }

}
