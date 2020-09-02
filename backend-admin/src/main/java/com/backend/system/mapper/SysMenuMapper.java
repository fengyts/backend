package com.backend.system.mapper;

import com.backend.system.dto.SysMenuDto;
import com.backend.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuDto> selectAllMenusByTier(Long userId);

    List<SysMenuDto> selectAllMenus(Long userId);
}
