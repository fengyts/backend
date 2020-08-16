package com.backend.system.mapper;

import com.backend.system.entity.SysMenu;
import com.backend.system.entity.SysRole;
import com.backend.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author fengyts
 * @since 2020-08-01
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectByUserName(@Param("userName") String userName);

    List<SysRole> findRolesByUserName(@Param("userName") String userName);

    List<SysMenu> selectMenusByRoleId(@Param("roleId") Long roleId);

}
