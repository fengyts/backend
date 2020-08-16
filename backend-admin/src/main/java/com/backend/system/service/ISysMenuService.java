package com.backend.system.service;

import com.backend.system.dto.SysMenuDto;
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

    List<SysMenuDto> getAllMenus(Long userId);

}
