package com.backend.system.service;

import com.backend.common.ResultData;
import com.backend.system.dto.SysMenuDto;
import com.backend.system.dto.SysRoleMenuDto;
import com.backend.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
public interface ISysMenuService extends IService<SysMenu> {

    /* 首页获取菜单信息, 父子嵌套层级结构 */
    List<SysMenuDto> getAllMenusByTier(Long userId);

    /** 角色绑定菜单权限 -> 菜单树形结构 */
    List<SysRoleMenuDto> getAllMenusByTier();

    /* 系统菜单列表页 */
    List<SysMenuDto> getAllMenus(Long userId);

    /* 根据id获取菜单详情 */
    SysMenuDto getMenuById(Long id);

    ResultData saveOrUpdate(SysMenuDto dto);

    boolean updateSysMenu(SysMenuDto dto);

}
